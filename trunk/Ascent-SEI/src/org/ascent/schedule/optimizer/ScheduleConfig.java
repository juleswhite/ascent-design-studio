package org.ascent.schedule.optimizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ascent.ProblemConfigImpl;
import org.ascent.VectorSolution;
import org.ascent.binpacking.ValueFunction;

public class ScheduleConfig extends ProblemConfigImpl {
	
	private ValueFunction<VectorSolution> scoringFunction_ = new ValueFunction<VectorSolution>() {

		public double getValue(VectorSolution src) {
			if (src.getArtifact() == null) {
				int score = scoreSchedule(getSchedule(src));
				src.setArtifact(score);
			}
			return (Integer) src.getArtifact();
		}
	};
	
	protected SchedulableTask [] tasks_;
	protected Application[] applications_;
	private List<Application> appList_ = new ArrayList();
	
	private List<SchedulableTask> taskList_ = new ArrayList();
	
	public ScheduleConfig(){
		super(0,0,0);
	}
	public ScheduleConfig(SchedulableTask[] tasks, Application[] applications) {
		super(tasks.length-1, 0, tasks.length-1);
		tasks_ = tasks;
		applications_ = applications;
		addTasks(tasks);
		addApplications(applications);
		orderElements();
	}
	
	public List<SchedulableTask> addTasks (SchedulableTask[] tasks){
		for(int i= 0; i < tasks.length-1; i++){
			taskList_.add(tasks[i]);
		}
		return taskList_;
	}
	
	public List<Application> addApplications (Application[] apps){
		for(int i= 0; i < apps.length-1; i++){
			appList_.add(apps[i]);
		}
		return appList_;
	}
	
	public Application addApplication(int id, String name) {
		Application app = new Application(id, name);
		appList_.add(app);
		return app;
	}

	public SchedulableTask addTask(String name, int id) {
		int [] dummy = {};
		SchedulableTask task= new SchedulableTask(id, name, dummy );
		taskList_.add(task);
		return task;
	}
	
	protected void orderElements() {
		//Arrays.sort(tasks_);
		//Arrays.sort(applications_);
		
	}
	public int scoreSchedule(Schedule plan) {
		return 0;
	}
	
	public Schedule getSchedule(VectorSolution sol) {
		return new Schedule(this, sol);
	}
	
	public List<SchedulableTask> getTaskList_() {
		return taskList_;
	}
	public void setTaskList_(List<SchedulableTask> taskList_) {
		this.taskList_ = taskList_;
	}
	public List<Application> getAppList_() {
		return appList_;
	}
	public void setAppList_(List<Application> appList_) {
		this.appList_ = appList_;
	}
	

}
