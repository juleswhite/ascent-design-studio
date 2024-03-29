package org.ascent.schedule.optimizer;

import java.util.ArrayList;
import java.util.List;

import org.ascent.VectorSolution;
import org.ascent.binpacking.ValueFunction;
import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.DeploymentPlan;



public class CAMSSchedulePlanner extends ScheduleConfig {
	
	private ValueFunction<VectorSolution> fitnessFunction_ = new ValueFunction<VectorSolution>() {

		public double getValue(VectorSolution src) {
			//System.out.println(" In get value of CAMSSChedulePlanner");
			if (src.getArtifact() == null) {
			//	System.out.println(" Got an artifact");
				//System.out.println("VectorSolution = " + src);
				Schedule sched = new Schedule(
						CAMSSchedulePlanner.this, src);
				int score = scoreSchedule(sched);
				src.setArtifact(score);
			}
			return (Integer) src.getArtifact();
		}

		
	};
	
	
	
	private int cacheSize_ = 250000;
	private ScheduleConfig sched_;
	
	public CAMSSchedulePlanner(ScheduleConfig sched){
		super( sched.getTasks_(), sched.getApplications_());
		sched_ = sched;
		
		
	}
	
	
	
	public int scoreSchedule(Schedule sched) {
		// TODO Auto-generated method stub
		int score = calculateCAMSM(sched);
		//System.out.println(" $$$$$$$ SCORE IS " +score );
		return score;
	}
	
	
	private int calculateCAMSM(Schedule sched) {//Calculates the Cache Aware MetaSchedule Metric for the given schedule
		
		/*
		 * Scoring Mechanism should give a large penalty when solution is infeasible. 
		 * 
		 */
		// TODO Auto-generated method stub
		List<SchedulableTask> tasks =  sched.getTasks_();
		//System.out.println(" in Calculate CAMSM");
		int start = 0;
		int CAMSM = 0;
		int repeats =1;
		ArrayList added = new ArrayList();
		for(SchedulableTask st : tasks){
			if(!added.contains(st.getId())){
				added.add(st.getId());
			}
			else{
				repeats++;
			}
		}
		//System.out.println(" Tasks are " + tasks);
		for(SchedulableTask task : tasks){
			int dataWritten = 0; 
			double sharingPercentage  = task.getApplication_().getSharedPercentage();
			//System.out.println("sharing percentage = " +sharingPercentage);
			
			//System.out.println("####Analyzing task " + task.getLabel());
			int oldCAMSM = CAMSM;
			for(int i = start; i < tasks.size()-1; i++){
				if(dataWritten > cacheSize_){
					break;
				}
				SchedulableTask currentTask = tasks.get(i);
				//System.out.println(" Task.getApplication.getName = "+ task.getApplication_().getName_() + " and currentTask.getApplication_().getName_() is " + currentTask.getApplication_().getName_());
				if(task.getApplication_().getName_().trim().equalsIgnoreCase(currentTask.getApplication_().getName_().trim())){
					
					int additionalHits = (int) (sharingPercentage * currentTask.getDataRead_());
					//System.out.println(" Applications match and data is shared. Adding  " + additionalHits + " to CAMSM");
					CAMSM = CAMSM + additionalHits;
				}
				else{
					//System.out.println(" Applications do not match, no data shared");
				}
				dataWritten = dataWritten+ (int) currentTask.getDataWritten_();		
				
			}
			//System.out.println(" Total dataWritten = " + dataWritten);
			//System.out.println("Total CAMSM found " + (CAMSM - oldCAMSM));
			start++;
		}
		//System.out.println(" Final CAMSM/repeats = " + CAMSM/repeats);
		return CAMSM/repeats;
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
