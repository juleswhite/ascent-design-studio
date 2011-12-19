package org.ascent.schedule.optimizer;

import java.util.ArrayList;
import java.util.List;

import org.ascent.VectorSolution;


public class Schedule {
	private List<Application> applications_ = new ArrayList();
	private List<SchedulableTask> tasks_ = new ArrayList();
	private VectorSolution solution_;
	private ScheduleConfig scheduleConfiguration_;
	
	public Schedule(ScheduleConfig config, VectorSolution solution) {
		super();
		solution_ = solution;
		scheduleConfiguration_ = config;
		tasks_ = scheduleConfiguration_.getTaskList_();
		applications_ = scheduleConfiguration_.getAppList_();
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
}
