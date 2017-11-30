package lab10;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class PrimeNumGen extends JFrame
{
	
	private static final long serialVersionUID = 1L;
	private final JTextArea aTextField = new JTextArea();
	private final JButton primeButton = new JButton("Start");
	private final JButton cancelButton = new JButton("Cancel");
	private volatile boolean cancel = false;
	private final PrimeNumGen thisFrame;
	
	public static void main(String[] args)
	{
		PrimeNumGen png = new PrimeNumGen("Primer Number Generator");
		
		// don't add the action listener from the constructor
		png.addActionListeners();
		png.setVisible(true);
		
	}
	
	private PrimeNumGen(String title)
	{
		super(title);
		this.thisFrame = this;
		cancelButton.setEnabled(false);
		aTextField.setEditable(false);
		setSize(200, 200);
		setLocationRelativeTo(null);
		//kill java VM on exit
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(primeButton,  BorderLayout.SOUTH);
		getContentPane().add(cancelButton,  BorderLayout.EAST);
		getContentPane().add( new JScrollPane(aTextField),  BorderLayout.CENTER);
	}
	
	private class CancelOption implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			cancel = true;
		}
	}
	
	private void addActionListeners()
	{
		cancelButton.addActionListener(new CancelOption());
	
		primeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{			
					String num = JOptionPane.showInputDialog("Enter a large integer");
					Integer max =null;		
					try
					{
						max = Integer.parseInt(num);
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(
								thisFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
					
					if( max != null)
					{
						aTextField.setText("");
						primeButton.setEnabled(false);
						cancelButton.setEnabled(true);
						cancel = false;
						new Thread(new UserInput(max)).start();

					}
				}});
		}
	//Naive way of finding primes, easily speed up using 4 threads vs only using 1
	public static boolean isPrime( int i) 
	{
		for( int x = 2; x < i; x++)
			if( i % x == 0  )
				return false;
		
		return true;
	}
	
	/*
	 * Finds primes by looping over a known list of integers that is being updated by other threads at the same time
	 * ( difficult to implement using only 4 threads)
	 * Thread 1 might be working on a prime that is the only factor of Thread 3, in which case Thread 3 will fail to be identified as non-prime
	 */
	public static boolean isPrimeSmart( Integer i, List<Integer> s)
	{
		Integer y = 1;	
		for( Integer x : s)
			{
				if(x > y)
					y=x;
				if(  i % x == 0  )
				{			
					return false;
				}
			}
		while (y < i-1)
		{
			y++;
			if(  i % y == 0  )
				return false;
		}
		

		return true;
	}
	
	
	private class UserInput implements Runnable
	{
		private final int max;
		private List<Integer> ccset = new CopyOnWriteArrayList<Integer>();
		private  final Integer NUM_WORKERS = 1;
		private Semaphore semaphore = new Semaphore(NUM_WORKERS);
		private UserInput(int num)
		{
			this.max = num;
		}
		
		public void run()
		{
			try {		
				long lastUpdate = System.currentTimeMillis();
				ccset.add(2);
				long startTime = System.currentTimeMillis();
				int j=1;
				for (int i = 1; i <= NUM_WORKERS && ! cancel; i++) 
				{
					/*
					 *PROBLEM:
					Usage of Semaphore with a single thread is considerably slower
					than that of simply looping over the list of known primes	
					WHY?			
					*/
					j=j+2;				
					this.semaphore.acquire();
					PrimeFinder p = new PrimeFinder(ccset, semaphore, j, max, NUM_WORKERS);
					new Thread(p).start();	
					
					
					/*
					The following uncommented code loops over a 
					list of known primes without the use of semaphores
					Thus is single threaded by default
					*/
					
					/*
					if (PrimeNumGen.isPrimeSmart(i, this.ccset))
					{
						ccset.add(i);
					}
					*/
				}	
				

				while(!this.ccset.contains(-1))			
				{
					if( System.currentTimeMillis() - lastUpdate > 500)
					{
						final String outString= "Found " + ccset.size() + " Primes in " +  max;
						
						SwingUtilities.invokeLater( new Runnable()
						{
							@Override
							public void run()
							{
								aTextField.setText(outString);
							}
						});
						
						lastUpdate = System.currentTimeMillis();	
					}
				}
				
				
				int numAcquired= 0;
				while (numAcquired < NUM_WORKERS)
				{
					this.semaphore.acquire();
					numAcquired++;
				}
				
				System.out.println(ccset.size()-1);
				System.out.println("Time "+((System.currentTimeMillis() - startTime)/1000f));
				final StringBuffer buff = new StringBuffer();
				for( Object i2 : ccset)
					buff.append(i2 + "\n");
			
				if( cancel)
					buff.append("cancelled");
			
				SwingUtilities.invokeLater
				(new Runnable()
				{
				@Override
					public void run()
					{
						cancel = false;
						primeButton.setEnabled(true);
						cancelButton.setEnabled(false);
						aTextField.setText( (cancel ? "cancelled " : "") +  buff.toString());
					}
				});				
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
		  // end UserInput
	}
}


/*
 * Worker class for factoring integers
*/
class PrimeFinder implements Runnable
{
	private List<Integer> ccset;
	private Semaphore semaphore;
	private Integer num;
	private Integer threads;
	private Integer max;
	private static AtomicLong current;
	public PrimeFinder(List<Integer> knownprimes, Semaphore s, Integer anumber, Integer max, Integer threads)
	{
		this.ccset = knownprimes;
		this.semaphore = s;
		this.num = anumber;
		this.max = max;
		this.threads = threads;
		
	}

	private boolean isPrimeSmart( Integer i)// still a working progress, need workaround 
	{
		return PrimeNumGen.isPrimeSmart(i, this.ccset);
	}
	
	//brute force finding prime
	private boolean isPrime( int i)
	{
		return PrimeNumGen.isPrime(i);
	}
	
	public void run()
	{
		try 
		{
			while (this.num <= this.max)
			{
				if (isPrime(this.num))
				{
					this.ccset.add(this.num);
				}
				this.num=this.num+2*this.threads;
				if( this.num >= this.max & !this.ccset.contains(-1))
				{
					this.ccset.add(-1);
				}
			}	
			this.semaphore.release();
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}

}

