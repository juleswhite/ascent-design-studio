package org.ascent.schedule.optimizer;

import java.util.List;

import org.ascent.VectorSolution;
import org.ascent.binpacking.ValueFunction;
import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.DeploymentPlan;



public class CAMSSchedulePlanner extends ScheduleConfig {
	
	private ValueFunction<VectorSolution> fitnessFunction_ = new ValueFunction<VectorSolution>() {

		public double getValue(VectorSolution src) {
			if (src.getArtifact() == null) {
				Schedule sched = new Schedule(
						CAMSSchedulePlanner.this, src);
				int score = scoreSchedule(sched);
				src.setArtifact(score);
			}
			return (Integer) src.getArtifact();
		}

		
	};
	
	
	
	private int cacheSizeKB_ = 4096;
	private ScheduleConfig sched_;
	
	public CAMSSchedulePlanner(ScheduleConfig sched){
		sched_ = sched;
		
	}
	
	public int scoreSchedule(Schedule sched) {
		// TODO Auto-generated method stub
		return calculateCAMSM(sched);
		
	}
	
	public ScheduleConfig getSchedule(){
		return sched_;
	}
	private int calculateCAMSM(Schedule sched) {//Calculates the Cache Aware MetaSchedule Metric for the given schedule
		// TODO Auto-generated method stub
		List<SchedulableTask> tasks =  sched.getTasks_();
		System.out.println(" in Calculate CAMSM");
		int start = 0;
		int CAMSM = 0;
		//System.out.println(" Tasks length = " + tasks.size());
		for(SchedulableTask task : tasks){
			int dataWritten = 0; 
			double sharingPercentage  = task.getApplication_().getSharedPercentage();
			System.out.println("####Analyzing task " + task.getLabel());
			int oldCAMSM = CAMSM;
			for(int i = start; i < tasks.size()-1; i++){
				if(dataWritten > cacheSizeKB_){
					break;
				}
				SchedulableTask currentTask = tasks.get(i);
				if(task.getApplication_().getName_().equalsIgnoreCase(currentTask.getApplication_().getName_())){
					
					int additionalHits = (int) (sharingPercentage * currentTask.getDataRead_());
					System.out.println(" Applications match and data is shared. Adding  " + additionalHits + " to CAMSM");
					CAMSM = CAMSM + additionalHits;
				}
				else{
					System.out.println(" Applications do not match, no data shared");
				}
				dataWritten = dataWritten+ (int) currentTask.getDataWritten_();		
				
			}
			System.out.println(" Total dataWritten = " + dataWritten);
			System.out.println("Total CAMSM found " + (CAMSM - oldCAMSM));
			start++;
		}
		return CAMSM;
	}

	public ValueFunction<VectorSolution> getFitnessFunction() {
		return fitnessFunction_;
	}

	public void setFitnessFunction(ValueFunction<VectorSolution> fitnessFunction) {
		fitnessFunction_ = fitnessFunction;
	}
	public int scoreSchedule(VectorSolution sol){
		return scoreSchedule(new Schedule(this,sol));
	}

}
