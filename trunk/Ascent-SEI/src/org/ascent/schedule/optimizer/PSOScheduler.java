package org.ascent.schedule.optimizer;


import org.ascent.VectorSolution;
import org.ascent.pso.Pso;

public class PSOScheduler implements SchedulePlanner {

	private double globalLearningRate_ = 2;
	private double localLearningRate_ = 0.5;
	private double intertia_ = 1;
	private int maxVelocity_ = 4;
	private int totalParticles_ = 200;
	private int searchIterations_ = 1000;
	private boolean useNetworkGravity_ = false;

	
	public Schedule schedule(ScheduleConfig problem) {

		System.out.println(" ScheduleConfig problem taskList size() = " + problem.getSchedule().getTaskList_());
		Pso pso = new Pso(problem);
		pso.setTotalParticles(totalParticles_);
		pso.setVelocityMax(maxVelocity_);
		pso.setLocalLearningRate(localLearningRate_);
		pso.setGlobalLearningRate(globalLearningRate_);
		pso.setIterations(searchIterations_);
		pso.setGlobalBestMustBeFeasible(true);
		
		VectorSolution sol = pso.solve(problem.getFitnessFunction());
		
		System.out.println(" Returned solution = " + sol);
		Schedule schedule = problem.getSchedule(sol);
		System.out.println(" in PSOScheduler and config tasklist size = " + problem.getTaskList_().size());
		/*if(useNetworkGravity_){
			NetworkGravityOptimizer opt = new NetworkGravityOptimizer();
			opt.optimize(plan);
		}*/
		
		return schedule;
	}

	public double getGlobalLearningRate() {
		return globalLearningRate_;
	}

	public void setGlobalLearningRate(double globalLearningRate) {
		globalLearningRate_ = globalLearningRate;
	}

	public double getLocalLearningRate() {
		return localLearningRate_;
	}

	public void setLocalLearningRate(double localLearningRate) {
		localLearningRate_ = localLearningRate;
	}

	public double getIntertia() {
		return intertia_;
	}

	public void setIntertia(double intertia) {
		intertia_ = intertia;
	}

	public int getMaxVelocity() {
		return maxVelocity_;
	}

	public void setMaxVelocity(int maxVelocity) {
		maxVelocity_ = maxVelocity;
	}

	public int getTotalParticles() {
		return totalParticles_;
	}

	public void setTotalParticles(int totalParticles) {
		totalParticles_ = totalParticles;
	}

	public int getSearchIterations() {
		return searchIterations_;
	}

	public void setSearchIterations(int searchIterations) {
		searchIterations_ = searchIterations;
	}

	public boolean isUseNetworkGravity() {
		return useNetworkGravity_;
	}

	public void setUseNetworkGravity(boolean useNetworkGravity) {
		useNetworkGravity_ = useNetworkGravity;
	}

}
