package org.ascent.schedule.optimizer;

import java.util.ArrayList;

import org.ascent.VectorSolution;
import org.ascent.binpacking.ValueFunction;

public class PeriodicScheduleConfig extends ScheduleConfig {

	private ValueFunction<VectorSolution> scoringFunction_ = new ValueFunction<VectorSolution>() {

		public double getValue(VectorSolution src) {
			System.out.println(" In get value of PeriodicScheduleConfig");
			if (src.getArtifact() == null) {
				int score = scoreSchedule(getPeriodicSchedule(src));
				src.setArtifact(score);
			}
			return (Integer) src.getArtifact();
		}
	};
	
	protected ArrayList<PeriodicTask> pTasks_ = new ArrayList();
	
	public PeriodicScheduleConfig(){
		super();
	}
	public PeriodicSchedule getPeriodicSchedule(VectorSolution sol) {
		System.out.println(" VS = "+ sol);
		return new PeriodicSchedule(this, sol);
	}
	
	public PeriodicScheduleConfig(PeriodicTask[] tasks, Application[] applications) {
		super(tasks,applications);
		tasks_ = tasks;
		taskList_ = new ArrayList();
		appList_ = new ArrayList();
		applications_ = applications;
		
		addTasks(tasks);
		addApplications(applications);
		orderElements();
		System.out.println(" Applist size = " + appList_.size());
		System.out.println(" Tasklistsize  = " + taskList_.size() );
	}
	public ArrayList<PeriodicTask> getpTasks_() {
		return pTasks_;
	}
	public void setpTasks_(ArrayList<PeriodicTask> pTasks_) {
		this.pTasks_ = pTasks_;
	}
}
