package org.ascent.binpacking.test;

import java.util.List;
import java.util.Map;

import org.ascent.binpacking.Bin;
import org.ascent.binpacking.BinPackingProblem;
import org.ascent.binpacking.FFDBinPacker;
import org.ascent.binpacking.Item;
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
public class WikiExampleADwMRTSC extends TestCase {

	public void testExample() {
		// First, we initialize a new BinPackingProblem
		BinPackingProblem bp = new BinPackingProblem();

		// Next, we add a series of hardware resources that
		// software components (items) can be placed on.
		// The int array that is passed into the constructor
		// defines the consumable resources that are available
		// on the hardware resource. In this case, the first
		// int array position represents the amount of RAM
		// that is available and the second position indicates
		// the max available CPU utilization. Later, we specify
		// that the CPU utilization resources is a rate monotonic
		// scheduling constraint.
		bp.getBins().add(new Bin("node1", new int[] { 1024, 100 }));
		bp.getBins().add(new Bin("node2", new int[] { 1024, 100 }));
		bp.getBins().add(new Bin("node3", new int[] { 1024, 100 }));
		bp.getBins().add(new Bin("node4", new int[] { 1024, 100 }));
		bp.getBins().add(new Bin("node5", new int[] { 1024, 100 }));

		// Create two software components (items)
		Item c1 = new Item("C1", new int[] { 200, 20 });
		Item c2 = new Item("C2", new int[] { 200, 20 });

		// Make sure that C1 and C2 end up being
		// placed on the same hardware resource
		c1.getDependencies().add(c2);

		// Add the software components to the
		bp.getItems().add(c1);
		bp.getItems().add(c2);

		// We add some more software components that
		// need to be deployed to the hardware nodes
		bp.getItems().add(new Item("C3", new int[] { 200, 20 }));
		bp.getItems().add(new Item("C4", new int[] { 200, 20 }));
		bp.getItems().add(new Item("C5", new int[] { 200, 20 }));
		bp.getItems().add(new Item("C6", new int[] { 200, 20 }));
		bp.getItems().add(new Item("C7", new int[] { 200, 20 }));

		// Now, we create a special component that needs to
		// have a replica of itself placed on another node.
		// We will use an exclusion consraint to make sure
		// this component and its replica don't end up on
		// the same hardware node.
		Item c8 = new Item("C8", new int[] { 200, 20 });
		bp.getItems().add(c8);

		// Create the replica
		Item c8replica = new Item("C8 replica", new int[] { 200, 20 });
		// Add an exclusion constraint to make sure the
		// replica isn't on the same node as the main
		// component
		c8replica.getExclusions().add(c8);
		bp.getItems().add(c8replica);

		// Now, we specify that the resource at position 1 in the
		// resource arrays of both the hardware and software
		// components represents a rate-monotonic scheduling
		// resource that should be controlled by the Liu & Layland
		// bound. That is, a group of software components can only
		// be mapped to a node if they are guaranteed via the
		// Liu and Layland bound to be schedulable on that hardware
		// node
		bp.getResourcePolicies().put(1, new RateMonotonicResource());

		// Create the solver
		FFDBinPacker packer = new FFDBinPacker(bp);

		// Ask the solver for a solution
		Map<Object, List> dep = packer.nextMapping();

		for (Object o : dep.keySet()) {
			// The component
			Item softwarecomp = (Item) o;

			// The list of hardware nodes that the
			// software component should be mapped to
			List deploymentlocations = dep.get(o);

			for (Object n : deploymentlocations) {
				Bin node = (Bin) n;
				System.out.println("Deploy " + softwarecomp.getName() + " to "
						+ node.getName());
			}
		}
	}
}
