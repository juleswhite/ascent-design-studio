package org.ascent.schedule.optimizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BuildPeriodicScheduleConfigFromFile extends BuildScheduleConfigFromFile{

	//private HashMap<String,PeriodicTask> pasks_;
	
	private HashMap<String, PeriodicTask> periodicTasks_ = new HashMap();

	public BuildPeriodicScheduleConfigFromFile(String directory){
		super(directory);
	}
	
	public void addRates(double baseRate, String baseScheduleFileName){
		try {
			ArrayList<String> fileLines = readFileLines(directory_ + "/" + baseScheduleFileName);
			System.out.println("Tasks = " + tasks_);
			for(String fileLine: fileLines){
				if( fileLine.contains("//TASK_TAG")){
					String taskLabel = fileLine.split("\\(")[0].split("\\.")[1];
					String rate = fileLine.split("\\/N\\/")[1];
					Double dRate = Double.valueOf(rate);
					System.out.println(" Looking up tag "+ taskLabel +" to add "+ dRate);
					if(tasks_.containsKey(taskLabel)){
						System.out.println("Tasks " + taskLabel +" FOUND");
						SchedulableTask st = tasks_.get(taskLabel);
						PeriodicTask pt = new PeriodicTask(st);
						pt.setRate_(baseRate/dRate);
						periodicTasks_.put(taskLabel, pt);
					}
					
					
				}
				else{
				//	System.out.println("NO TASK TAG FOUND IN FILE");
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print(" Error reading file" + e);
			e.printStackTrace();
		}
		
	}

	public HashMap<String, PeriodicTask> getPeriodicTasks_() {
		return periodicTasks_;
	}

	public void setPeriodicTasks_(HashMap<String, PeriodicTask> pTasks) {
		this.periodicTasks_ = pTasks;
	}
}
