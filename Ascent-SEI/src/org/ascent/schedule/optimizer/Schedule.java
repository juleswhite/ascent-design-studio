package org.ascent.schedule.optimizer;

import java.util.ArrayList;
import java.util.List;

import org.ascent.VectorSolution;


public class Schedule {
	private List<Application> applications_ = new ArrayList();
	private List<SchedulableTask> tasks_ = new ArrayList();
	private Application [] appArray_;
	private SchedulableTask [] taskArray_;
	

	private VectorSolution solution_;
	private ScheduleConfig scheduleConfiguration_;
	
	/*public Schedule(ScheduleConfig config, VectorSolution solution) {
		super();
		solution_ = solution;
		scheduleConfiguration_ = config;
		tasks_ = scheduleConfiguration_.getTaskList_();
		applications_ = scheduleConfiguration_.getAppList_();
		fillArrays();
	}*/
	
	public Schedule(ScheduleConfig sched, VectorSolution vs){
		super();
		solution_ = vs;
		scheduleConfiguration_ = sched;
		//applications_ = scheduleConfiguration_.getAppList_();
		setApplicationList_(scheduleConfiguration_.getApplications_());
		int [] vsPositions = vs.getPosition();
		
		System.out.println(" VS Positions = " + vs);
		
		SchedulableTask [] sts = new SchedulableTask [vsPositions.length];
		tasks_ = new ArrayList<SchedulableTask>();
		for(int i = 0; i < vsPositions.length; i++){
			taskArray_[i] = sched.getTaskList_().get(vsPositions[i]);
			System.out.println("taskArray [i] = "+ taskArray_[i]);
			tasks_.add(sts[i]);
		}
	    
	}
	private void fillArrays(){
		
		int index = 0;
		appArray_ = new Application[applications_.size()];
		taskArray_ = new SchedulableTask[tasks_.size()];
		for( Application app : applications_){
			appArray_[index] = app;
			index++;
		}
		index =0; 
		
		for( SchedulableTask st : tasks_){
			taskArray_[index] = st;
			index++;
		}
		
	}
	
	public void setApplicationList_(Application [] aa){
		applications_ = new ArrayList();
		for(int i=0;  i < aa.length; i++){
			applications_.add(aa[i]);
		}
	}
	
	public ScheduleConfig getScheduleConfiguration() {
		return scheduleConfiguration_;
	}
	
	public VectorSolution getSolution() {
		return solution_;
	}
	
	public List<SchedulableTask> getTasks_() {
		return tasks_;
	}

	public void setTasks_(List<SchedulableTask> tasks_) {
		this.tasks_ = tasks_;
	}
	
	public Application[] getAppArray_() {
		return appArray_;
	}

	public void setAppArray_(Application[] appArray_) {
		this.appArray_ = appArray_;
	}

	public SchedulableTask[] getTaskArray_() {
		return taskArray_;
	}

	public void setTaskArray_(SchedulableTask[] taskArray_) {
		this.taskArray_ = taskArray_;
	}
}
