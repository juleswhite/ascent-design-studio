package org.ascent.schedule.optimizer;

import java.util.ArrayList;
import java.util.List;

import org.ascent.VectorSolution;

public class PeriodicSchedule extends Schedule {
	
	private List<PeriodicTask> pTasks_ = new ArrayList();
	
	public PeriodicSchedule(PeriodicScheduleConfig sched, VectorSolution vs){
		super(sched, vs);
		pTasks_ = sched.getpTasks_();
		
	}
	
	public List<PeriodicTask> getpTasks_(){
		return pTasks_;
	}
}
