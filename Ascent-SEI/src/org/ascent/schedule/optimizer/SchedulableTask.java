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
	
	public SchedulableTask(int id, String name, int [] iarray, Application app, double dr, double dw){
		super(id,name, iarray);
		taskID_ = id;
		taskName_ = name;
		application_ = app;
		dataWritten_ = dw;
		dataRead_ = dr;
	}
	
	public double getDataWritten_() {
		return dataWritten_;
	}

	public void setDataWritten_(double dataWritten_) {
		this.dataWritten_ = dataWritten_;
	}
	
	public double getDataRead_() {
		return dataRead_;
	}

	public void setDataRead_(double dataRead_) {
		this.dataRead_ = dataRead_;
	}
	
	public Application getApplication_() {
		return application_;
	}

	public void setApplication_(Application application_) {
		this.application_ = application_;
	}
	
	public String toString(){
		String s = "Name:"+taskName_ + " App: " + application_.getName_() + " DataRead: " + dataRead_ + " DataWritten: " + dataWritten_;
		return s;
	}

	public double getSize_() {
		return size_;
	}

	public void setSize_(double size_) {
		this.size_ = size_;
	}

	public int getTaskID_() {
		return taskID_;
	}

	public void setTaskID_(int taskID_) {
		this.taskID_ = taskID_;
	}

	public String getTaskName_() {
		return taskName_;
	}

	public void setTaskName_(String taskName_) {
		this.taskName_ = taskName_;
	}

}
