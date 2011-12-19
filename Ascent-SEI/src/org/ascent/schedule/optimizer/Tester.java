package org.ascent.schedule.optimizer;

public class Tester {

	public static void main(String args[]){
		
		Application redApp = new Application(1,"redApp");
		
		SchedulableTask task1 = new SchedulableTask(1 , "Task1",  new int []{0,0,0}, redApp);
		ScheduleConfig sc = new ScheduleConfig( new SchedulableTask[] {task1},new Application [] {redApp});
		
		
		
		
		
	}
}
