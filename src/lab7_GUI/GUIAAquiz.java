package lab7_GUI;
import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIAAquiz extends JFrame
{
	private final static String[] SHORT_NAMES = 
		{
		"A","R", "N", "D", "C", "Q", "E", 
		"G",  "H", "I", "L", "K", "M", "F", 
		"P", "S", "T", "W", "Y", "V" 
		};
	
	private final static String[] FULL_NAMES = 
		{
		"alanine","arginine", "asparagine", 
		"aspartic acid", "cysteine",
		"glutamine",  "glutamic acid",
		"glycine" ,"histidine","isoleucine",
		"leucine",  "lysine", "methionine", 
		"phenylalanine", "proline", 
		"serine","threonine","tryptophan", 
		"tyrosine", "valine"
		};
	
	private Integer numRight = 0;
	private Integer numWrong = 0;
	private static final long serialVersionUID = 1L;
	private String currentAminoAcid;
	private String currentShortAA;
	private Random random = new Random();
	private final static int INTERVAL = 1000;
	private Timer timer;
	private final Long tlimit = (long) 30;
	long startTime = System.currentTimeMillis();

	
	//all the buttons
	private JButton cancelButton = new JButton("cancel");
	private JButton startButton = new JButton("start quiz");
	private JButton submitButton = new JButton("enter");
	private JButton restartButton = new JButton("restart");

	
	//the labels for everything in the panels
	private JLabel questionField = new JLabel();
	private JLabel aminoacid = new JLabel();	
	private JLabel rightField = new JLabel();
	private JLabel wrongField = new JLabel();
	private JLabel timeField = new JLabel();
	
	//the text field for taking input
	private JTextField answerField = new JTextField(5);//this is the field that takes user input
	
	//my different panels
	private JPanel mypanel = new JPanel();
	private JPanel answerpanel = new JPanel();
	private JPanel statepanel = new JPanel();
	private JPanel contentpanel = new JPanel();
	private JPanel resultpanel = new JPanel();
	private JPanel timepanel = new JPanel();
	
	
	//the following are panels in the interface
	private JPanel getBottomPanel(JPanel panel) // panel for the buttons to press
	{
		
		panel.setLayout(new GridLayout(0,1));
		panel.add(startButton);
		startButton.addActionListener(new startQuizActionListener());
		return panel;
	}
	
	private JPanel answerPanel(JPanel panel) // panel to input the questions
	{

		aminoacid.setLabelFor(answerField);
		panel.add(aminoacid);
		panel.add(answerField);
		return panel;
	}
	
	private JPanel statePanel(JPanel panel)// panel to display the number of right and wrong answers
	{
		rightField.setText("RIGHT: "+"\n"+numRight);
		rightField.setHorizontalAlignment(SwingConstants.CENTER);
		wrongField.setText("WRONG: "+"\n"+numWrong);
		wrongField.setHorizontalAlignment(SwingConstants.CENTER);
		panel.setLayout(new GridLayout(0,2));
		panel.add(rightField);
		panel.add(wrongField);
		return panel;
	}
	private JPanel resultPanel()
	{
		JLabel right = new JLabel();
		JLabel wrong = new JLabel();
		right.setText("RIGHT: "+"\n"+numRight);
		right.setFont(new Font("American Typewriter", Font.PLAIN, 20));
		right.setHorizontalAlignment(SwingConstants.CENTER);
		wrong.setText("WRONG: "+"\n"+numWrong);
		wrong.setHorizontalAlignment(SwingConstants.CENTER);
		wrong.setFont(new Font("American Typewriter", Font.PLAIN, 20));
		resultpanel.removeAll();
		resultpanel.setLayout(new GridLayout(0,2));
		resultpanel.add(right);
		resultpanel.add(wrong);
		return resultpanel;
	}
	private JPanel timePanel(JPanel panel)
	{
		panel.add(timeField);
		return panel;
	}
	//the following are the action listeners for the various buttons
	private class startQuizActionListener implements ActionListener// thread for timer is launched with this action
	{
		public void actionPerformed(ActionEvent arg0)
		{
			mypanel.remove(startButton);
			mypanel.setLayout(new GridLayout(0,2));
			mypanel.add(submitButton);
			mypanel.add(cancelButton);
			mypanel.doLayout();
			revalidate();
			repaint();
			timeField.setText(tlimit.toString());
			timeField.setFont(new Font("American Typewriter", Font.PLAIN, 60));
			questionField.setHorizontalAlignment(SwingConstants.CENTER);
			contentpanel.setLayout(new BoxLayout(contentpanel, BoxLayout.Y_AXIS));
			contentpanel.add(questionField);
			contentpanel.add(answerPanel(answerpanel));
			contentpanel.add(statePanel(statepanel));
			contentpanel.add(timePanel(timepanel));
			getContentPane().add(contentpanel, BorderLayout.NORTH);
			updateQuestion();
			startTime = System.currentTimeMillis();
			timer.restart();
		}
	}
	
	private class timeActionListener implements ActionListener
	{
			
			public void actionPerformed(ActionEvent evt) 
			{
				Long time = tlimit - (System.currentTimeMillis() - startTime)/1000;
				timeField.setText(time.toString());
				timepanel.revalidate();

		       if (time <= (long)0) {
		    	   ((Timer)evt.getSource()).stop();
		    	   reset();
		    	   return;
		       }
		    }    
		;
	}
	private class enterActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			String ans = answerField.getText();
			if (ans.equalsIgnoreCase(currentShortAA))
			{
				numRight++;
			}
			else
			{
				numWrong++;
			}

			updateQuestion();
			updateState();
		}
	}
	
	
	private class cancelActionListener implements ActionListener//The cancel button action
	{
		public void actionPerformed(ActionEvent arg0)
		{
			reset();
		}

	}
	
	private void reset()
	{
		contentpanel.removeAll();
		getContentPane().remove(contentpanel);
		getContentPane().add(resultPanel(), BorderLayout.CENTER);
		mypanel.remove(submitButton);
		mypanel.remove(cancelButton);
		mypanel.setLayout(new GridLayout(0,1));
		mypanel.add(restartButton);
		mypanel.doLayout();
		revalidate();
		repaint();
		timer.stop();
	}
	
	private class restartActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			numRight = 0;
			numWrong = 0;
			mypanel.remove(restartButton);
			mypanel.add(startButton);
			mypanel.setLayout(new GridLayout(0,1));
			mypanel.doLayout();
			getContentPane().remove(resultpanel);
			revalidate();
			repaint();
		}
	}
	
	//the following are the updates for the questions
	private void updateQuestion()//update the question each time
	{
		Integer num = random.nextInt(20);
		currentAminoAcid = FULL_NAMES[num];
		currentShortAA= SHORT_NAMES[num];
		questionField.setText("What is the ONE letter name for:");
		aminoacid.setText(currentAminoAcid);
		answerField.setText("");
		validate();
	}

	private void updateState()
	{
		rightField.setText("RIGHT: "+"\n"+numRight);
		wrongField.setText("WRONG: "+"\n"+numWrong);
		validate();
		
	}
	
	public GUIAAquiz(String title)
	{
		super(title);
		setSize(300,300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getBottomPanel(this.mypanel), BorderLayout.SOUTH);
		ActionListener enterAction = new enterActionListener();
		
		restartButton.addActionListener(new restartActionListener());
		cancelButton.addActionListener(new cancelActionListener());
		submitButton.addActionListener(enterAction);
		answerField.addActionListener(enterAction);
		startButton.addActionListener(new startQuizActionListener());
		timer = new Timer(INTERVAL, null);
		ActionListener timeAction = new timeActionListener();
		timer.addActionListener(timeAction);
		setVisible(true);
	}
	
	public static void main(String [] args)
	{
		new GUIAAquiz("Amino Acid Quiz");
	}
}
