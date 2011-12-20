package org.ascent.schedule.optimizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ascent.ProblemConfigImpl;
import org.ascent.VectorSolution;
import org.ascent.binpacking.Bin;
import org.ascent.binpacking.BinPackingProblem;
import org.ascent.binpacking.FFDBinPacker;
import org.ascent.binpacking.RandomItemPacker;
import org.ascent.binpacking.ValueFunction;
import org.ascent.deployment.Colocated;
import org.ascent.deployment.Component;
import org.ascent.deployment.DeploymentConstraint;
import org.ascent.deployment.HardwareNode;
import org.ascent.deployment.Interaction;
import org.ascent.deployment.NetworkLink;
import org.ascent.deployment.Node;
import org.ascent.deployment.NotColocated;
import org.ascent.deployment.PlacementConstraint;
import org.ascent.deployment.SoftwareComponent;

public class ScheduleConfig extends ProblemConfigImpl {
	
	private ValueFunction<VectorSolution> scoringFunction_ = new ValueFunction<VectorSolution>() {

		public double getValue(VectorSolution src) {
			System.out.println(" In get value of ScheduleConfig");
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
		super(tasks.length, 0, tasks.length-1);
		tasks_ = tasks;
		applications_ = applications;
		addTasks(tasks);
		addApplications(applications);
		orderElements();
		System.out.println(" Applist size = " + appList_.size());
		System.out.println(" Tasklistsize  = " + taskList_.size() );
	}
	
	public List<SchedulableTask> addTasks (SchedulableTask[] tasks){
		for(int i= 0; i < tasks.length; i++){
			taskList_.add(tasks[i]);
		}
		return taskList_;
	}
	
	public List<Application> addApplications (Application[] apps){
		for(int i= 0; i < apps.length; i++){
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
	
	public void init() {
		

		if (boundaries_.length != tasks_.length) {
			boundaries_ = new int[tasks_.length][1];
			for (int i = 0; i < boundaries_.length; i++) {
				boundaries_[i] = new int[] {0};
			}
		}
	}

	public VectorSolution[] createInitialSolutions(int count) {
		/*if (seedWithBinPacking_) {
			Map mapping = new HashMap();
			BinPackingProblem bp = new BinPackingProblem();
			bp.getResourcePolicies().putAll(getResourceConsumptionPolicies());
			for (Node n : getNodes()) {
				HardwareNode hn = new HardwareNode(n.getLabel(), n
						.getResources());
				bp.getBins().add(hn);
				mapping.put(hn, n);
				mapping.put(n, hn);
			}
			for (Component c : getComponents()) {
				SoftwareComponent cn = new SoftwareComponent(c.getLabel(), c
						.getResources());
				cn.setRealTimeTasks(c.getRealTimeTasks());
				bp.getItems().add(cn);
				mapping.put(c, cn);
			}

			for (DeploymentConstraint con : constraints_) {
				if (con instanceof NotColocated) {
					NotColocated ncon = (NotColocated) con;
					SoftwareComponent sc = (SoftwareComponent) mapping.get(ncon
							.getSource());

					for (Component c : ncon.getTargets()) {
						SoftwareComponent tc = (SoftwareComponent) mapping
								.get(c);
						sc.getExclusions().add(tc);
					}
				}
				if (con instanceof Colocated) {
					Colocated ncon = (Colocated) con;
					SoftwareComponent sc = (SoftwareComponent) mapping.get(ncon
							.getSource());

					for (Component c : ncon.getTargets()) {
						SoftwareComponent tc = (SoftwareComponent) mapping
								.get(c);
						sc.getDependencies().add(tc);
					}
				}

				if (con instanceof PlacementConstraint) {
					PlacementConstraint pcon = (PlacementConstraint) con;
					SoftwareComponent sc = (SoftwareComponent) mapping.get(pcon
							.getSource());
					sc.setValidBins(new ArrayList<Bin>());
					for (Node n : pcon.getValidHosts()) {
						HardwareNode hn = (HardwareNode) mapping.get(n);
						sc.getValidBins().add(hn);
					}
				}
			}

			List<Map<Object, List>> binsols = new ArrayList<Map<Object, List>>();
			for (int i = 0; i < count - 2; i++) {
				RandomItemPacker packer = new RandomItemPacker(bp);
				Map<Object, List> sol = packer.nextMapping();
				binsols.add(sol);
			}
			binsols.add((new FFDBinPacker(bp)).nextMapping());

			int stotal = Math.min(binsols.size(), count);
			VectorSolution[] sols = new VectorSolution[stotal];

			for (int i = 0; i < stotal; i++) {
				int[] pos = new int[getComponents().length];
				Map<Object, List> sol = binsols.get(i);

				for (int j = 0; j < getComponents().length; j++) {
					SoftwareComponent c = (SoftwareComponent) mapping
							.get(getComponents()[j]);
					if (c != null && sol != null && sol.get(c) != null) {

						HardwareNode host = (HardwareNode) sol.get(c).get(0);
						Node node = (Node) mapping.get(host);
						for (int k = 0; k < getNodes().length; k++) {
							if (getNodes()[k] == node) {
								pos[j] = k;
								break;
							}
						}
					}
				}
				sols[i] = new VectorSolution(pos);
			}

			return sols;
		} else {*/
			return super.createInitialSolutions(count);
		//}
	}
	
	
	protected void orderElements() {
		//Arrays.sort(tasks_);
		//Arrays.sort(applications_);
		
	}
	public int scoreSchedule(Schedule plan) {
		return 0;
	}
	
	public Schedule getSchedule(VectorSolution sol) {
		System.out.println(" VS = "+ sol);
		return new Schedule(this, sol);
	}
	
	public ScheduleConfig getSchedule(){
		return this;
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
	public ValueFunction<VectorSolution> getFitnessFunction() {
		return scoringFunction_;
	}
	public SchedulableTask[] getTasks_() {
		return tasks_;
	}
	public void setTasks_(SchedulableTask[] tasks_) {
		this.tasks_ = tasks_;
	}
	public Application[] getApplications_() {
		return applications_;
	}
	public void setApplications_(Application[] applications_) {
		this.applications_ = applications_;
	}
	
	public boolean isFeasible(VectorSolution sol) {
		boolean feasible = true;
		ArrayList found = new ArrayList();
		String reportString = "";
		//System.out.println("VS in isFeasible = " + sol);
		//System.out.println(" sol.length = " + sol.getPosition().length);
		for( int i =0; i < sol.getPosition().length; i++){
			
			int foundInt = sol.getPosition()[i]; 
			reportString = reportString + " Checking position "+ i+ " to see if " + foundInt +" is in "+ found+"with Result "+found.contains(foundInt) +"\n";
			if(!found.contains(foundInt)){
				found.add(foundInt);
			}
			else{
				//System.out.println(reportString);
				return false;
			}
		}
		//System.out.println(reportString);
		return feasible;
	}
	

}
