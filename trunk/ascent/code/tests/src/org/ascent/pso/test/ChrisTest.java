package org.ascent.pso.test;

import org.ascent.VectorSolution;
import org.ascent.deployment.Component;
import org.ascent.deployment.NetworkBandwidthMinimizingPlanner;
import org.ascent.deployment.NetworkLink;
import org.ascent.deployment.Node;
import org.ascent.pso.Pso;

import com.sun.xml.internal.ws.addressing.ProblemAction;

import junit.framework.TestCase;

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
	
}
