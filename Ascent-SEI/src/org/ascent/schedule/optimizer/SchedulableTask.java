package org.ascent.schedule.optimizer;

import org.ascent.deployment.ModelElement;
import org.ascent.deployment.Schedulable;

public class SchedulableTask extends ModelElement{
	
	private Application application_ ; 
	private double dataWritten_ = 0;
	private double dataRead_ = 0; 
	private double size_ =0 ;
	private int taskID_ = -1;
	private String taskName_;
	
	public SchedulableTask(int id, String label, int[] resources) {
		super(id, label, resources);
		taskID_ = id;
		taskName_ = label;
	}
	
	public SchedulableTask(int id, String name, int [] iarray, Application app){
		super(id,name, iarray);
		taskID_ = id;
		taskName_ = name;
		application_ = app;
	}
	
}
