package org.ascent.pso.test;

import junit.framework.TestCase;

import org.ascent.VectorSolution;
import org.ascent.deployment.Component;
import org.ascent.deployment.Interaction;
import org.ascent.deployment.KFailureNetMinConfig;
import org.ascent.deployment.NetworkBandwidthMinimizingPlanner;
import org.ascent.deployment.NetworkLink;
import org.ascent.deployment.Node;
import org.ascent.deployment.benchmarks.DeploymentBenchmark;
import org.ascent.pso.Pso;

public class ChrisTest extends TestCase {
	
	public void testOne(){
		

	NetworkBandwidthMinimizingPlanner problem = new NetworkBandwidthMinimizingPlanner();
	
	Node p3 = problem.addNode("P3", new int[] { 300 });
	Node p5 = problem.addNode("P5", new int[] { 400 });
	Node p1 = problem.addNode("P1", new int[] { 100 });
	Node p2 = problem.addNode("P2", new int[] { 100 });

	NetworkLink nl = problem.addNetwork("LAN", new Node[] {p1, p2, p3, p5},
			new int[] {Integer.MAX_VALUE});
	
	Component a1 = problem.addComponent("App1", new int[] { 95 });
	Component a2 = problem.addComponent("App2", new int[] { 120 });
	Component a3 = problem.addComponent("App3", new int[] { 200 });
	Component a4 = problem.addComponent("App4", new int[] { 89 });
	Component a5 = problem.addComponent("App5", new int[] { 26 });
	Component a6 = problem.addComponent("App6", new int[] { 43 });
	Component a7 = problem.addComponent("App7", new int[] { 90 });
	
	problem.init();
	
	
	Pso pso = new Pso(problem);
	VectorSolution sol = pso.solve(problem.getFitnessFunction());
	
	problem.printSolutionStats(sol);
	}
	
	public void testKFailure(){
		Node[] nodes = new Node [4];
		nodes[0] = new Node(0, "P3", new int[] {400});
		nodes[1] = new Node(1, "P5", new int[] {400});
		nodes[2] = new Node(2, "P1", new int[] {400});
		nodes[3] = new Node(3, "P2", new int[] {400});
		
		Component[] components = new Component[7];
		components[0] = new Component(0, "App1", new int[] { 95 });
		components[1] = new Component(1, "App2", new int[] { 120 });
		components[2] = new Component(2, "App3", new int[] { 200 });
		components[3] = new Component(3, "App4", new int[] { 89 });
		components[4] = new Component(4, "App5", new int[] { 26 });
		components[5] = new Component(5, "App6", new int[] { 43 });
		components[6] = new Component(6, "App7", new int[] { 90 });
		
		NetworkLink[] nl = new NetworkLink[1];
		nl[0] = new NetworkLink(0, "LAN", nodes, new int[] {Integer.MAX_VALUE});
		
		KFailureNetMinConfig prob = new KFailureNetMinConfig(nodes, nl, components, new Interaction[] {}, 3);
		prob.init();
		Pso pso = new Pso(prob);
		
		VectorSolution sol = pso.solve(prob.getFitnessFunction());
		
		prob.printSolutionStats(prob.getDeploymentPlan(sol));
		//DeploymentBenchmark db = new DeploymentBenchmark(prob);
		//System.out.println(db.executeTest(prob.getDeploymentPlan(sol)));
		
	}
	
}
