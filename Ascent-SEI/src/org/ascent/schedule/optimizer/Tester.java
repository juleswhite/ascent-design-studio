package org.ascent.schedule.optimizer;

import org.ascent.VectorSolution;
import org.ascent.deployment.PSODeployer;
import org.ascent.pso.Pso;
import org.ascent.schedule.optimizer.*;
public class Tester {

	public static void main(String args[]){
		
		Application redApp = new Application(1,"redApp");
		Application blueApp = new Application(2,"blueApp");
		
		redApp.setSharedPercentage(0.25);
		blueApp.setSharedPercentage(0.5);
		
		SchedulableTask task1 = new SchedulableTask(1 , "Task1",  new int []{0,0,0}, redApp, 1175,2300);
		SchedulableTask task2 = new SchedulableTask(2 , "Task2",  new int []{0,0,0}, blueApp,1000, 675);
		SchedulableTask task3 = new SchedulableTask(3 , "Task3",  new int []{0,0,0}, redApp, 2900, 2300);
		SchedulableTask task4 = new SchedulableTask(4 , "Task4",  new int []{0,0,0}, blueApp, 900, 750);
		SchedulableTask task5 = new SchedulableTask(5 , "Task5",  new int []{0,0,0}, redApp, 3000, 2227);
		SchedulableTask task6 = new SchedulableTask(6 , "Task6",  new int []{0,0,0}, blueApp, 5949, 2257);
		SchedulableTask task7 = new SchedulableTask(7 , "Task7",  new int []{0,0,0}, redApp, 2986, 2106);
		SchedulableTask task8 = new SchedulableTask(8 , "Task8",  new int []{0,0,0}, blueApp, 1893,1275);
		ScheduleConfig sc = new ScheduleConfig( new SchedulableTask[] {task1, task2, task3, task4, task5, task6, task7, task8},new Application [] {redApp,blueApp});
		System.out.println(" SC = " + sc.getTaskList_());
		CAMSSchedulePlanner cms = new CAMSSchedulePlanner(sc);
		System.out.println(" cms tasklist = " + cms.getTaskList_().size());
		
		PSOScheduler pso = new PSOScheduler();
		
		//System.out.println("cms tasklist = " + cms.getTaskList_());
		Schedule sched = pso.schedule(cms); 
		System.out.println(" Schedule = " + sched);
		
		//PSOScheduler pso = new PSOScheduler();
		//pso.schedule(cms);
		
		
		System.out.println(" CAMS result = " + cms.scoreSchedule(sched));
		
	}
}
