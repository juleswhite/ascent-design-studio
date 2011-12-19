package org.ascent.schedule.optimizer;

import java.util.ArrayList;
import java.util.List;

import org.ascent.VectorSolution;
import org.ascent.deployment.DeploymentConfig;

public class Schedule {
	private List<Application> applications_ = new ArrayList();
	private List<SchedulableTask> tasks_ = new ArrayList();
	private VectorSolution solution_;
	private ScheduleConfig scheduleConfiguration_;
	
	public Schedule(ScheduleConfig config, VectorSolution solution) {
		super();
		solution_ = solution;
		scheduleConfiguration_ = config;
	}
	
	public ScheduleConfig getScheduleConfiguration() {
		return scheduleConfiguration_;
	}
	
	public VectorSolution getSolution() {
		return solution_;
	}
}
