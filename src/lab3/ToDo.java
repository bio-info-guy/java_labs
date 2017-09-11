package lab3;

import java.util.Timer;
import java.util.TimerTask;

public class ToDo  {
  Timer timer;
  public ToDo ( int seconds )   
  {
    timer = new Timer (  ) ;
    timer.schedule ( new ToDoTask () , seconds*1000 ) ;
  }
  

  class ToDoTask extends TimerTask  
  {  
    public void run (  )   
    {
    	  System.out.println ( "\nTIME FOR RECKONING!" );
    	  System.out.println("you got this many right: "+ AA_quiz.num_right);  
      System.exit(0);
     // timer.cancel (  ) ; //Terminate the thread
    }
  }


}