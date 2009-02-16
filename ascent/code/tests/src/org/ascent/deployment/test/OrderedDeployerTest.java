package org.ascent.deployment.test;

import java.util.List;
import java.util.Map;

import org.ascent.VectorSolution;
import org.ascent.binpacking.Bin;
import org.ascent.binpacking.BinPackingProblem;
import org.ascent.binpacking.Item;
import org.ascent.binpacking.LeastBoundMinExcPacker;
import org.ascent.binpacking.LeastBoundPacker;
import org.ascent.binpacking.MultiStrategyBinPacker;
import org.ascent.deployment.Component;
import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.DeploymentPlan;
import org.ascent.deployment.NetworkLink;
import org.ascent.deployment.Node;
import org.ascent.deployment.OrderedDeployer;
import org.ascent.deployment.RateMonotonicResource;

import junit.framework.TestCase;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class OrderedDeployerTest extends TestCase {

	public void test() {
		DeploymentConfig problem = new DeploymentConfig();
		Node p1 = problem.addNode("P1", new int[] { 100 });
		Node p2 = problem.addNode("P2", new int[] { 100 });
		Node p21 = problem.addNode("P21", new int[] { 100 });
		Node p22 = problem.addNode("P22", new int[] { 100 });
		Node p23 = problem.addNode("P23", new int[] { 100 });
		Node p24 = problem.addNode("P24", new int[] { 100 });
		Node p25 = problem.addNode("P25", new int[] { 100 });
		Node p26 = problem.addNode("P26", new int[] { 100 });
		Node p27 = problem.addNode("P27", new int[] { 100 });
		Node p28 = problem.addNode("P28", new int[] { 100 });
		Node p29 = problem.addNode("P29", new int[] { 100 });
		Node p30 = problem.addNode("P30", new int[] { 100 });
		Node p31 = problem.addNode("P31", new int[] { 100 });
		Node p32 = problem.addNode("P32", new int[] { 100 });

		NetworkLink nl1 = problem.addNetwork("LocalArea1", new Node[] { p1, p2,
				p21, p22, p23, p24, p25, p26, p27, p28, p29, p30, p31, p32 },
				new int[] { Integer.MAX_VALUE });

		Component a1 = problem.addComponent("App1", new int[] { 31 });//0
		Component a2 = problem.addComponent("App2", new int[] { 31 });//1
		Component a3 = problem.addComponent("App3", new int[] { 94 });//2
		Component a4 = problem.addComponent("App4", new int[] { 35 });//3
		Component a5 = problem.addComponent("App5", new int[] { 44 });//4
		Component a6 = problem.addComponent("App6", new int[] { 43 });//5
		Component a7 = problem.addComponent("App7", new int[] { 30 });//6
		Component a8 = problem.addComponent("App8", new int[] { 58 });//7
		Component a9 = problem.addComponent("App9", new int[] { 98 });//8
		Component a10 = problem.addComponent("App10", new int[] { 27 });//9
		Component a11 = problem.addComponent("App11", new int[] { 54 });//10
		Component a12 = problem.addComponent("App12", new int[] { 96 });//11
		Component a13 = problem.addComponent("App13", new int[] { 41 });//12
		Component a14 = problem.addComponent("App14", new int[] { 37 });//13

		problem.requireNotColocated(a14, a4);
		problem.requireNotColocated(a13, a4);
		problem.requireColocated(a2, a4);
		problem.init();
		
		OrderedDeployer deployer = new OrderedDeployer(problem);
		
		VectorSolution sol = new VectorSolution(new int[]{8,11,13,12,11,10,9,7,6,5,4,3,2,1,0});
		DeploymentPlan plan = deployer.deploy(sol);
		
	}
	
	public void testDups() {
		DeploymentConfig problem = new DeploymentConfig();
		Node p1 = problem.addNode("P1", new int[] { 100 });
		Node p2 = problem.addNode("P2", new int[] { 100 });
		Node p21 = problem.addNode("P21", new int[] { 100 });
		Node p22 = problem.addNode("P22", new int[] { 100 });
		Node p23 = problem.addNode("P23", new int[] { 100 });
		Node p24 = problem.addNode("P24", new int[] { 100 });
		Node p25 = problem.addNode("P25", new int[] { 100 });
		Node p26 = problem.addNode("P26", new int[] { 100 });
		Node p27 = problem.addNode("P27", new int[] { 100 });
		Node p28 = problem.addNode("P28", new int[] { 100 });
		Node p29 = problem.addNode("P29", new int[] { 100 });
		Node p30 = problem.addNode("P30", new int[] { 100 });
		Node p31 = problem.addNode("P31", new int[] { 100 });
		Node p32 = problem.addNode("P32", new int[] { 100 });

		NetworkLink nl1 = problem.addNetwork("LocalArea1", new Node[] { p1, p2,
				p21, p22, p23, p24, p25, p26, p27, p28, p29, p30, p31, p32 },
				new int[] { Integer.MAX_VALUE });

		Component a1 = problem.addComponent("App1", new int[] { 31 });//0
		Component a2 = problem.addComponent("App2", new int[] { 31 });//1
		Component a3 = problem.addComponent("App3", new int[] { 94 });//2
		Component a4 = problem.addComponent("App4", new int[] { 35 });//3
		Component a5 = problem.addComponent("App5", new int[] { 44 });//4
		Component a6 = problem.addComponent("App6", new int[] { 43 });//5
		Component a7 = problem.addComponent("App7", new int[] { 30 });//6
		Component a8 = problem.addComponent("App8", new int[] { 58 });//7
		Component a9 = problem.addComponent("App9", new int[] { 98 });//8
		Component a10 = problem.addComponent("App10", new int[] { 27 });//9
		Component a11 = problem.addComponent("App11", new int[] { 54 });//10
		Component a12 = problem.addComponent("App12", new int[] { 96 });//11
		Component a13 = problem.addComponent("App13", new int[] { 41 });//12
		Component a14 = problem.addComponent("App14", new int[] { 37 });//13

		problem.requireNotColocated(a14, a4);
		problem.requireNotColocated(a13, a4);
		problem.requireColocated(a2, a4);
		problem.init();
		
		OrderedDeployer deployer = new OrderedDeployer(problem);
		
		VectorSolution sol = new VectorSolution(new int[]{8,8,13,12,11,10,9,7,6,5,4,3,2,0,0});
		DeploymentPlan plan = deployer.deploy(sol);
		
	}
}
