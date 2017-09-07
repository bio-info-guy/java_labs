package lab3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lab3.ToDo;

public class AA_quiz 
{
	int num_right = 0; 
	public static String[] SHORT_NAMES = 
		{
		"A","R", "N", "D", "C", "Q", "E", 
		"G",  "H", "I", "L", "K", "M", "F", 
		"P", "S", "T", "W", "Y", "V" 
		};
	
	public static String[] FULL_NAMES = 
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
	
public boolean quiz_mode() throws Exception
	{
		System.out.println("which mode would you like (custom or sudden_death) #custom allows 30s of play by default");
		String aString = System.console().readLine().toUpperCase();
		if(aString.equalsIgnoreCase("sudden_death")) {
			return true;
		}
		else if(aString.equalsIgnoreCase("custom")) {
			return false;
		}
		else {
			throw new Exception("method not available");
		}
	}

	private static class ShutDownHook extends Thread {  
	List<String> questions = new ArrayList<String>(); 
	List<String> user_ans = new ArrayList<String>();
	List<String> ans = new ArrayList<String>();
	List<String> state = new ArrayList<String>();	
	
    public ShutDownHook( List<String> questions, List<String> user_ans, List<String> ans, List<String> state) {
    	this.questions = questions;
    	this.user_ans= user_ans;
    	this.ans = ans;
    	this.state = state;  

    }
	public void run() {  
 		System.out.println("you got this many right: "+ Collections.frequency(this.state,"CORRECT"));
 		System.out.println("here are your results ");
 		System.out.println("question\tyours\tcorrect ");
		for (int x =0; x < this.questions.size(); x++) {
			System.out.println(this.questions.get(x)+"\t"+ this.user_ans.get(x)+"\t"+this.ans.get(x)+"\t"+this.state.get(x));
		}
    }  
} 
	
	public void sud_death(int tlimit) 
	{
		
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		long elapsed = now - start;
		boolean correct = true; boolean wrong = false;
		while (correct == true) {
			if (wrong == true || elapsed > tlimit*1000) {
				System.out.println("you got this many right: "+ num_right);
				System.exit(0);
			}
		else {
			Random random = new Random();
			int index = random.nextInt(20);
			String aa = FULL_NAMES[index];
			System.out.println("what is the one character for "+ aa);
			String aString = System.console().readLine().toUpperCase();
			if (aString.length() > 1 ) 
			{
				if(aString.equals("QUIT")) 
				{
					System.exit(0);
				}
				else 
				{
					System.out.println("the question asked for one character");
				}
			}
			else 
			{
				String aChar = "" + aString.charAt(0);
				if( aChar.equalsIgnoreCase(SHORT_NAMES[index]))
				{
					this.num_right++;;
				}
				else
				{
					wrong = true;
				}
				now = System.currentTimeMillis();
				elapsed = now - start;

			}
		}
	}
}
	
	
public void custom_quiz(int tlimit, int nquestions ) 
{
		List<String> questions = new ArrayList<String>(); 
		List<String> user_ans = new ArrayList<String>();
		List<String> ans = new ArrayList<String>();
		List<String> state = new ArrayList<String>();
		int total=0;
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		long elapsed = now - start;
	    ShutDownHook jvmShutdownHook = new ShutDownHook( questions, user_ans,  ans, state);  
	    Runtime.getRuntime().addShutdownHook(jvmShutdownHook); 

		while (total <= nquestions) 
		{
			if ( elapsed > tlimit || total == nquestions)
			{
				System.exit(0);
			}
		else 
		{
			Random random = new Random();
			int index = random.nextInt(20);
			String aa = FULL_NAMES[index];
			System.out.println("what is the one character for "+ aa);
			String aString = System.console().readLine().toUpperCase();
			if (aString.length() > 1 ) 
			{
				if(aString.equals("QUIT")) 
				{
					System.exit(0);
				}
				else 
				{
					System.out.println("the question asked for one character");
				}
			}
			else 
			{
				String aChar = "" + aString.charAt(0);
				questions.add(aa);
				user_ans.add(aChar);
				ans.add(SHORT_NAMES[index]);
				if( aChar.equalsIgnoreCase(SHORT_NAMES[index])) 
				{			
					state.add("CORRECT");
				}
				else 
				{
					state.add("WRONG");
				}
				now = System.currentTimeMillis();
				elapsed = now - start;
				total++;
			}

		}
	}
	}
	
public static void main(String[] args) throws Exception
	{
		AA_quiz quiz = new AA_quiz();
		boolean sudden_death = quiz.quiz_mode();
		int tlimit = 30;

		if (!sudden_death) 
		{
			System.out.println("how long do you want to play:  #custom allows 30s of play by default");
			tlimit = Integer.valueOf(System.console().readLine());
			System.out.println("how many questions would you like: ");
			int qlimit = Integer.valueOf(System.console().readLine());
			new ToDo ( tlimit, quiz.num_right);
			quiz.custom_quiz(tlimit*1000, qlimit);
		}
		else 
		{
			new ToDo ( tlimit , quiz.num_right) ;
			quiz.sud_death(tlimit);

		}
		
	}
}

