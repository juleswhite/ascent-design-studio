package org.ascent.schedule.optimizer;

import java.util.ArrayList;
import java.util.List;

public class Application {

	protected List<SchedulableTask> taskList_ = new ArrayList();
	private String name_ = "NOT NAMED";
	private double size_ = 0;
	private int id_ = -1;
	private double sharedPercentage = 0;
	
	public Application(){
		
	}
	
	public Application( int id , String name){
		id_ = id;
		name_ = name; 
	}
	
	public String getName_() {
		return name_;
	}
	
	public void setName_(String name_) {
		this.name_ = name_;
	}
	public double getSharedPercentage() {
		return sharedPercentage;
	}

	public void setSharedPercentage(double sharedPercentage) {
		this.sharedPercentage = sharedPercentage;
	}
}
