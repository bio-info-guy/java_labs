package lab10;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

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
	//
	public static boolean isPrime( int i)
	{
		for( int x = 2; x < i; x++)
			if( i % x == 0  )
				return false;
		
		return true;
	}
	
	/*
	 * Finds primes by looping over a known list of integers that is being updated by other threads at the same time
	 */
	public static boolean isPrimeSmart( Integer i, List<Integer> s)
	{
		Integer y = 0;	
		for( Integer x : s)
			{
				if (x > i)//if current integer in list is larger than integer being queried, move to next integer
				{
					continue;
				}
				y = x;
				//cache.add(y);
				if(  i % x == 0  )
				{			
					return false;
				}
			}
		/*
		 Within single threads, the following codes aren't necessary because the list of primes will always be up to date
		*/	
		//starting from the last known integer that is smaller than queried integer i
		//perform modular operation up to i-1
		while (y < i-1)
		{

			y++;
			//cache.add(y);
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
				for (int i = 3; i < max && ! cancel; i++) 
				{
					/*
					 *PROBLEM:
					Usage of Semaphore with a single thread is considerably slower
					than that of simply looping over the list of known primes	
					WHY?			
					*/
					
					
					this.semaphore.acquire();
					PrimeFinder p = new PrimeFinder(ccset, semaphore, i);
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
					
					
					if( System.currentTimeMillis() - lastUpdate > 500)
					{
						final String outString= "Found " + ccset.size() + " in " + i + " of " + max;
						
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
				System.out.println(ccset.size());
				System.out.println("Time "+((System.currentTimeMillis() - startTime)/1000f));
				final StringBuffer buff = new StringBuffer();
			
				for( Integer i2 : ccset)
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
	public PrimeFinder(List<Integer> knownprimes, Semaphore s, Integer anumber)
	{
		this.ccset = knownprimes;
		this.semaphore = s;
		num = anumber;
		
	}

	private boolean isPrimeSmart( Integer i)
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
		try {
			if (isPrimeSmart(this.num))
			{
				//synchronized(this.ccset)
				//{
					this.ccset.add(this.num);
				//}
			}
			this.semaphore.release();
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}

}

