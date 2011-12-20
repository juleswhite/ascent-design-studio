package org.ascent.schedule.optimizer;


import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.DeploymentPlan;

public interface SchedulePlanner {

	public Schedule schedule(ScheduleConfig conf);
}

