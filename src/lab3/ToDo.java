package lab3;

import java.util.Timer;
import java.util.TimerTask;

public class ToDo  {
  Timer timer;
  int num_right;
  public ToDo ( int seconds, int num )   
  {
	this.num_right = num;
    timer = new Timer (  ) ;
    timer.schedule ( new ToDoTask ( this.num_right ) , seconds*1000 ) ;
  }

  class ToDoTask extends TimerTask  
  {
	 int num_right;	  
	 public ToDoTask ( int num ) 
	 {
		 this.num_right = num;
	 }
    public void run (  )   
    {
    	  System.out.println("you got this many right: "+ this.num_right);
      System.out.println ( "\nTIME FOR RECKONING!" ) ;
      System.exit(0);
     // timer.cancel (  ) ; //Terminate the thread
    }
  }


}