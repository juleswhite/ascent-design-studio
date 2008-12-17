package org.ascent.deployment.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.ascent.VectorSolution;
import org.ascent.deployment.Component;
import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.DeploymentPlan;
import org.ascent.deployment.NetworkGravityOptimizer;
import org.ascent.deployment.Node;
import org.ascent.deployment.NetworkGravityOptimizer.InteractionGroup;

/******************************************************************************
 * Copyright (c) 2007 Jules White.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jules White - initial API and implementation 
 ****************************************************************************/
public class NetworkGravityTest extends TestCase{

	
	public void testCalculateNetChangeOfMoveSimple(){
		DeploymentConfig conf = new DeploymentConfig();
		Node n1 = conf.addNode("n1", new int[0]);
		Node n2 = conf.addNode("n2", new int[0]);
		Node n3 = conf.addNode("n3", new int[0]);
		Component c1 = conf.addComponent("c1", new int[0]);
		Component c2 = conf.addComponent("c2", new int[0]);
		Component c3 = conf.addComponent("c3", new int[0]);
		conf.addInteraction("c1-->c2", new int[]{20,30}, 1, new Component[]{c1,c2});
		conf.addInteraction("c1-->c3", new int[]{30,40}, 1, new Component[]{c1,c3});
		conf.addInteraction("c1-->c1", new int[]{20,30}, 1, new Component[]{c1,c1});
		conf.init();
		
		Map<Component, Node> plan = new HashMap<Component, Node>();
		plan.put(c1, n1);
		plan.put(c2, n2);
		plan.put(c3, n3);
		DeploymentPlan dplan = new DeploymentPlan(conf,plan);
		
		NetworkGravityOptimizer gravity = new NetworkGravityOptimizer();
		int[] change = gravity.calculateNetChangeOfMove(dplan, c1, n2);
		assertEquals(change[0], -20);
		assertEquals(change[1], -30);
	}
	
	public void testCalculateNetChangeOfMoveComplex(){
		DeploymentConfig conf = new DeploymentConfig();
		Node n1 = conf.addNode("n1", new int[0]);
		Node n2 = conf.addNode("n2", new int[0]);
		Node n3 = conf.addNode("n3", new int[0]);
		Component c1 = conf.addComponent("c1", new int[0]);
		Component c2 = conf.addComponent("c2", new int[0]);
		Component c3 = conf.addComponent("c3", new int[0]);
		Component c4 = conf.addComponent("c4", new int[0]);
		conf.addInteraction("c1-->c2", new int[]{20,30}, 1, new Component[]{c1,c2});
		conf.addInteraction("c1-->c3", new int[]{30,40}, 1, new Component[]{c1,c3});
		conf.addInteraction("c1-->c1", new int[]{20,30}, 1, new Component[]{c1,c1});
		conf.addInteraction("c1-->c4", new int[]{80,300}, 1, new Component[]{c1,c4});
		conf.init();
		
		Map<Component, Node> plan = new HashMap<Component, Node>();
		plan.put(c1, n1);
		plan.put(c4, n1);
		plan.put(c2, n2);
		plan.put(c3, n3);
		DeploymentPlan dplan = new DeploymentPlan(conf,plan);
		
		NetworkGravityOptimizer gravity = new NetworkGravityOptimizer();
		int[] change = gravity.calculateNetChangeOfMove(dplan, c1, n2);
		assertEquals(change[0], 60);
		assertEquals(change[1], 270);
	}
	
	public void testCalculateNetChangeOfMoveComplexWithSwaps(){
		DeploymentConfig conf = new DeploymentConfig();
		Node n1 = conf.addNode("n1", new int[0]);
		Node n2 = conf.addNode("n2", new int[0]);
		Node n3 = conf.addNode("n3", new int[0]);
		Component c1 = conf.addComponent("c1", new int[0]);
		Component c2 = conf.addComponent("c2", new int[0]);
		Component c3 = conf.addComponent("c3", new int[0]);
		Component c4 = conf.addComponent("c4", new int[0]);
		conf.addInteraction("c1-->c2", new int[]{20,30}, 1, new Component[]{c1,c2});
		conf.addInteraction("c1-->c3", new int[]{30,40}, 1, new Component[]{c1,c3});
		conf.addInteraction("c1-->c1", new int[]{20,30}, 1, new Component[]{c1,c1});
		conf.addInteraction("c1-->c4", new int[]{80,300}, 1, new Component[]{c1,c4});
		conf.init();
		
		Map<Component, Node> plan = new HashMap<Component, Node>();
		plan.put(c1, n1);
		plan.put(c4, n1);
		plan.put(c2, n2);
		plan.put(c3, n3);
		DeploymentPlan dplan = new DeploymentPlan(conf,plan);
		
		Map<Component, Node> swapplan = new HashMap<Component, Node>();
		swapplan.put(c1, n2);
		swapplan.put(c4, n2);
		
		NetworkGravityOptimizer gravity = new NetworkGravityOptimizer();
		int[] change = gravity.calculateNetChangeOfMove(dplan,swapplan, c1, n2);
		assertEquals(change[0], -20);
		assertEquals(change[1], -30);
		
		change = gravity.calculateNetChangeOfMove(dplan, c1, n2);
		assertEquals(change[0], 60);
		assertEquals(change[1], 270);
		
		swapplan.put(c2, n1);
		change = gravity.calculateNetChangeOfMove(dplan,swapplan, c1, n2);
		assertEquals(change[0], 0);
		assertEquals(change[1], 0);
	}
	
	public void testFindInteractionGroups(){
		DeploymentConfig conf = new DeploymentConfig();
		Node n1 = conf.addNode("n1", new int[0]);
		Node n2 = conf.addNode("n2", new int[0]);
		Node n3 = conf.addNode("n3", new int[0]);
		Component c1 = conf.addComponent("c1", new int[0]);
		Component c2 = conf.addComponent("c2", new int[0]);
		Component c3 = conf.addComponent("c3", new int[0]);
		Component c4 = conf.addComponent("c4", new int[0]);
		conf.addInteraction("c1-->c2", new int[]{20,30}, 1, new Component[]{c1,c2});
		conf.addInteraction("c1-->c3", new int[]{30,40}, 1, new Component[]{c1,c3});
		conf.addInteraction("c1-->c1", new int[]{20,30}, 1, new Component[]{c1,c1});
		conf.addInteraction("c1-->c4", new int[]{80,300}, 1, new Component[]{c1,c4});
		conf.init();
		
		Map<Component, Node> plan = new HashMap<Component, Node>();
		plan.put(c1, n1);
		plan.put(c4, n1);
		plan.put(c2, n2);
		plan.put(c3, n3);
		DeploymentPlan dplan = new DeploymentPlan(conf,plan);
		
		NetworkGravityOptimizer gravity = new NetworkGravityOptimizer();
		List<InteractionGroup> groups = gravity.findInteractionGroups(dplan);
		
		assertEquals(groups.size(),4);
		
		for(InteractionGroup g : groups){
			if(g.getSource() == c1 && g.getTarget() == n2){
				assertEquals(g.getSize()[0], -60);
				assertEquals(g.getSize()[1], -270);
			}
			if(g.getSource() == c1 && g.getTarget() == n3){
				assertEquals(g.getSize()[0], -50);
				assertEquals(g.getSize()[1], -260);
			}
			if(g.getSource() == c2 && g.getTarget() == n1){
				assertEquals(g.getSize()[0], 20);
				assertEquals(g.getSize()[1], 30);
			}
			if(g.getSource() == c3 && g.getTarget() == n1){
				assertEquals(g.getSize()[0], 30);
				assertEquals(g.getSize()[1], 40);
			}
		}
	}
	
	public void testSimpleOptimize(){
		DeploymentConfig conf = new DeploymentConfig();
		Node n1 = conf.addNode("n1", new int[0]);
		Node n2 = conf.addNode("n2", new int[0]);
		Node n3 = conf.addNode("n3", new int[0]);
		Component c1 = conf.addComponent("c1", new int[0]);
		Component c2 = conf.addComponent("c2", new int[0]);
		Component c3 = conf.addComponent("c3", new int[0]);
		Component c4 = conf.addComponent("c4", new int[0]);
		conf.addInteraction("c1-->c2", new int[]{20,30}, 1, new Component[]{c1,c2});
		conf.addInteraction("c1-->c3", new int[]{30,40}, 1, new Component[]{c1,c3});
		conf.addInteraction("c1-->c1", new int[]{20,30}, 1, new Component[]{c1,c1});
		conf.addInteraction("c1-->c4", new int[]{80,300}, 1, new Component[]{c1,c4});
		conf.init();
		
		Map<Component, Node> plan = new HashMap<Component, Node>();
		plan.put(c1, n1);
		plan.put(c4, n1);
		plan.put(c2, n2);
		plan.put(c3, n3);
		DeploymentPlan dplan = new DeploymentPlan(conf,plan);
		
		NetworkGravityOptimizer gravity = new NetworkGravityOptimizer();
		gravity.optimize(dplan);
		System.out.println(dplan);
	}
	
	public void testDenseOptimize(){
		DeploymentConfig conf = new DeploymentConfig();
		Node n1 = conf.addNode("n1", new int[]{80});
		Node n2 = conf.addNode("n2", new int[]{40});
		Node n3 = conf.addNode("n3", new int[]{20});
		Component c1 = conf.addComponent("c1", new int[]{20});
		Component c2 = conf.addComponent("c2", new int[]{20});
		Component c2a = conf.addComponent("c2a", new int[]{20});
		Component c3 = conf.addComponent("c3", new int[]{20});
		Component c4 = conf.addComponent("c4", new int[]{20});
		conf.addInteraction("c1-->c4", new int[]{20,30}, 1, new Component[]{c1,c4});
		conf.addInteraction("c1-->c3", new int[]{90,400}, 1, new Component[]{c1,c3});
		conf.addInteraction("c1-->c1", new int[]{20,30}, 1, new Component[]{c1,c1});
		conf.addInteraction("c1-->c2", new int[]{80,300}, 1, new Component[]{c1,c2});
		conf.addInteraction("c1-->c2a", new int[]{80,300}, 1, new Component[]{c1,c2a});
		conf.init();
		
		Map<Component, Node> plan = new HashMap<Component, Node>();
		plan.put(c1, n1);
		plan.put(c4, n1);
		plan.put(c2, n2);
		plan.put(c2a, n2);
		plan.put(c3, n3);
		DeploymentPlan dplan = new DeploymentPlan(conf,plan);
		
		NetworkGravityOptimizer gravity = new NetworkGravityOptimizer();
		gravity.optimize(dplan);
		assertEquals(n1, dplan.getHost(c1));
		assertEquals(n1, dplan.getHost(c2));
		assertEquals(n1, dplan.getHost(c2a));
		assertEquals(n1, dplan.getHost(c4));
		assertEquals(n3, dplan.getHost(c3));
	}
	
	public void testDenseOptimizeWithPush(){
		DeploymentConfig conf = new DeploymentConfig();
		Node n1 = conf.addNode("n1", new int[]{60});
		Node n2 = conf.addNode("n2", new int[]{40});
		Node n3 = conf.addNode("n3", new int[]{20});
		Component c1 = conf.addComponent("c1", new int[]{20});
		Component c2 = conf.addComponent("c2", new int[]{20});
		Component c2a = conf.addComponent("c2a", new int[]{20});
		Component c3 = conf.addComponent("c3", new int[]{20});
		Component c4 = conf.addComponent("c4", new int[]{20});
		conf.addInteraction("c1-->c4", new int[]{20,30}, 1, new Component[]{c1,c4});
		conf.addInteraction("c1-->c3", new int[]{90,400}, 1, new Component[]{c1,c3});
		conf.addInteraction("c1-->c1", new int[]{20,30}, 1, new Component[]{c1,c1});
		conf.addInteraction("c1-->c2", new int[]{80,300}, 1, new Component[]{c1,c2});
		conf.addInteraction("c1-->c2a", new int[]{80,300}, 1, new Component[]{c1,c2a});
		conf.init();
		
		Map<Component, Node> plan = new HashMap<Component, Node>();
		plan.put(c1, n1);
		plan.put(c4, n1);
		plan.put(c2, n2);
		plan.put(c2a, n2);
		plan.put(c3, n3);
		DeploymentPlan dplan = new DeploymentPlan(conf,plan);
		
		NetworkGravityOptimizer gravity = new NetworkGravityOptimizer();
		gravity.optimize(dplan);
		assertEquals(n1, dplan.getHost(c1));
		assertEquals(n1, dplan.getHost(c2));
		assertEquals(n1, dplan.getHost(c2a));
		assertEquals(n2, dplan.getHost(c4));
		assertEquals(n3, dplan.getHost(c3));
	}
}
