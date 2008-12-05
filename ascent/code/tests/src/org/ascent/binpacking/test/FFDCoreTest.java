package org.ascent.binpacking.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.ascent.binpacking.Bin;
import org.ascent.binpacking.BinPackingProblem;
import org.ascent.binpacking.FFDCore;
import org.ascent.binpacking.Item;
import org.ascent.binpacking.LeastBoundMinExcPacker;
import org.ascent.binpacking.LeastBoundPacker;
import org.ascent.binpacking.MultiStrategyBinPacker;
import org.ascent.configurator.CoreAdapter;
import org.ascent.configurator.ProblemBuilderCore;
import org.ascent.configurator.RefreshCore;
import org.ascent.configurator.conf.RefreshProblem;
import org.ascent.deployment.RateMonotonicPessimisticResource;
import org.ascent.deployment.RateMonotonicResource;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class FFDCoreTest extends TestCase {

	public RefreshProblem createProblem(ProblemConfiguration conf) {
		ProblemBuilderCore pbc = new ProblemBuilderCore(new CoreAdapter());
		initProblem(pbc, conf);
		return pbc.getProblem();
	}

	public void initProblem(RefreshCore core, ProblemConfiguration conf) {

		int[][] sres = createResourceArray(conf.srcs, conf.dims, conf.srmin,
				conf.srmax);
		int[][] tres = createResourceArray(conf.trgs, conf.dims, conf.trmin,
				conf.trmax);

		Object[] sitems = createItemsArray(conf.srcs, "Source");
		Object[] titems = createItemsArray(conf.trgs, "Target");

		core.setSetsToMap(sitems, titems);
		core.setResourceConstraints(sres, tres);

		for (int i = 0; i < conf.srcs; i++) {
			int max = random(conf.reqmin, conf.reqmax);
			for (int j = 0; j < max; j++) {
				int other = random(0, conf.srcs - 1);
				if (other != i) {
					int reqt = random(0, 100);

					if (reqt < conf.reqethreshold) {
						core.addExcludesMappingConstraint(sitems[i],
								sitems[other]);
					} else if (reqt > conf.reqcthreshold) {
						core.addRequiresMappingConstraint(sitems[i],
								sitems[other]);
					}
				}
			}
		}
	}
	
	public void testLeastBoundMinExcPacking() {

		BinPackingProblem bp = new BinPackingProblem();

		bp.getBins().add(new Bin("node1", new int[] { 1024, 100, 80 }));
		bp.getBins().add(new Bin("node2", new int[] { 1024, 100, 10 }));
		bp.getBins().add(new Bin("node3", new int[] { 1024, 100, 80}));
		bp.getBins().add(new Bin("node4", new int[] { 1024, 100, 30 }));
		bp.getBins().add(new Bin("node5", new int[] { 1024, 100, 50 }));

		Item c1 = new Item("C1", new int[] { 200, 20, 5 });
		Item c2 = new Item("C2", new int[] { 200, 20, 0 });

		c1.getDependencies().add(c2);

		bp.getItems().add(c1);
		bp.getItems().add(c2);

		bp.getItems().add(new Item("C3", new int[] { 200, 20,5 }));
		bp.getItems().add(new Item("C4", new int[] { 200, 70,19 }));
		bp.getItems().add(new Item("C5", new int[] { 200, 90,75 }));
		bp.getItems().add(new Item("C6", new int[] { 200, 70,30 }));
		bp.getItems().add(new Item("C7", new int[] { 200, 20,10 }));


		Item c8 = new Item("C8", new int[] { 200, 20, 0 });
		bp.getItems().add(c8);

		Item c8replica = new Item("C8 replica", new int[] { 200, 20, 0 });

		c8replica.getExclusions().add(c8);
		bp.getItems().add(c8replica);
		bp.getResourcePolicies().put(1, new RateMonotonicResource());

		LeastBoundPacker packer = new LeastBoundMinExcPacker(bp);
		

		// Ask the solver for a solution
		Map<Object, List> dep = packer.nextMapping();
		
		assertNotNull(dep);
	}
	
	public void testMultiAlgPacking() {

		BinPackingProblem bp = new BinPackingProblem();

		bp.getBins().add(new Bin("node1", new int[] { 1024, 100, 80 }));
		bp.getBins().add(new Bin("node2", new int[] { 1024, 100, 10 }));
		bp.getBins().add(new Bin("node3", new int[] { 1024, 100, 80}));
		bp.getBins().add(new Bin("node4", new int[] { 1024, 100, 30 }));
		bp.getBins().add(new Bin("node5", new int[] { 1024, 100, 50 }));

		Item c1 = new Item("C1", new int[] { 200, 20, 5 });
		Item c2 = new Item("C2", new int[] { 200, 20, 0 });

		c1.getDependencies().add(c2);

		bp.getItems().add(c1);
		bp.getItems().add(c2);

		bp.getItems().add(new Item("C3", new int[] { 200, 20,5 }));
		bp.getItems().add(new Item("C4", new int[] { 200, 70,19 }));
		bp.getItems().add(new Item("C5", new int[] { 200, 90,75 }));
		bp.getItems().add(new Item("C6", new int[] { 200, 70,30 }));
		bp.getItems().add(new Item("C7", new int[] { 200, 20,10 }));


		Item c8 = new Item("C8", new int[] { 200, 20, 0 });
		bp.getItems().add(c8);

		Item c8replica = new Item("C8 replica", new int[] { 200, 20, 0 });

		c8replica.getExclusions().add(c8);
		bp.getItems().add(c8replica);
		bp.getResourcePolicies().put(1, new RateMonotonicResource());

		MultiStrategyBinPacker packer = new MultiStrategyBinPacker();
		

		// Ask the solver for a solution
		Map<Object, List> dep = packer.pack(bp);
		
		assertNotNull(dep);
	}

	public void testForcedOrderPacking() {

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
		bp.getBins().add(new Bin("node1", new int[] { 1024, 100, 80 }));
		bp.getBins().add(new Bin("node2", new int[] { 1024, 100, 10 }));
		bp.getBins().add(new Bin("node3", new int[] { 1024, 100, 80}));
		bp.getBins().add(new Bin("node4", new int[] { 1024, 100, 30 }));
		bp.getBins().add(new Bin("node5", new int[] { 1024, 100, 50 }));

		// Create two software components (items)
		Item c1 = new Item("C1", new int[] { 200, 20, 5 });
		Item c2 = new Item("C2", new int[] { 200, 20, 0 });

		// Make sure that C1 and C2 end up being
		// placed on the same hardware resource
		c1.getDependencies().add(c2);

		// Add the software components to the
		bp.getItems().add(c1);
		bp.getItems().add(c2);

		// We add some more software components that
		// need to be deployed to the hardware nodes
		bp.getItems().add(new Item("C3", new int[] { 200, 20,5 }));
		bp.getItems().add(new Item("C4", new int[] { 200, 70,19 }));
		bp.getItems().add(new Item("C5", new int[] { 200, 90,7 }));
		bp.getItems().add(new Item("C6", new int[] { 200, 70,0 }));
		bp.getItems().add(new Item("C7", new int[] { 200, 20,10 }));

		// Now, we create a special component that needs to
		// have a replica of itself placed on another node.
		// We will use an exclusion consraint to make sure
		// this component and its replica don't end up on
		// the same hardware node.
		Item c8 = new Item("C8", new int[] { 200, 20, 0 });
		bp.getItems().add(c8);

		// Create the replica
		Item c8replica = new Item("C8 replica", new int[] { 200, 20, 0 });
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
		FFDCore packer = new FFDCore(bp);
		
		packer.getPriorityPackingQueue().add(c8);
		packer.getPriorityPackingQueue().add(c8replica);
		

		// Ask the solver for a solution
		Map<Object, List> dep = packer.nextMapping();
		
		assertNotNull(dep);
	}

	public void testBinPackingProblemConfiguration() {

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
		FFDCore packer = new FFDCore(bp);

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

		assertNotNull(dep);

		bp = new BinPackingProblem();

		bp.getBins().add(new Bin("node1", new int[] { 1024, 100 }));
		bp.getBins().add(new Bin("node2", new int[] { 1024, 100 }));
		bp.getBins().add(new Bin("node3", new int[] { 1024, 100 }));
		bp.getBins().add(new Bin("node4", new int[] { 1024, 100 }));

		bp.getItems().add(new Item("C1", new int[] { 900, 20 }));
		bp.getItems().add(new Item("C2", new int[] { 200, 20 }));
		bp.getItems().add(new Item("C3", new int[] { 300, 20 }));
		bp.getItems().add(new Item("C4", new int[] { 100, 20 }));
		bp.getItems().add(new Item("C5", new int[] { 200, 80 }));
		bp.getItems().add(new Item("C6", new int[] { 200, 70 }));
		bp.getItems().add(new Item("C7", new int[] { 200, 30 }));
		bp.getItems().add(new Item("C8", new int[] { 200, 20 }));
		bp.getResourcePolicies().put(1, new RateMonotonicResource());

		packer = new FFDCore(bp);
		dep = packer.nextMapping();

		assertNull(dep);
	}

	public void testLargeResourceProblemWithCons() {
		ProblemConfiguration p = new ProblemConfiguration();
		p.dims = 3;
		p.srcs = 1000;
		p.trgs = 50;
		p.srmax = 75;
		p.srmin = 1;
		p.trmax = 1000;
		p.trmin = 500;
		p.reqmax = 1;
		p.reqmin = 0;

		FFDCore core = new FFDCore();
		core.requireAllMappedExactlyOnce();
		initProblem(core, p);

		Map<Object, List> sol = core.nextMapping();
		assertNotNull(sol);

		int packed = 0;
		for (Object o : sol.keySet()) {
			if (sol.get(o).size() > 0) {
				packed++;
			}
		}

	}

	public void testLargeResourceProblemWithoutCons() {
		ProblemConfiguration p = new ProblemConfiguration();
		p.dims = 3;
		p.srcs = 1000;
		p.trgs = 50;
		p.srmax = 75;
		p.srmin = 1;
		p.trmax = 1000;
		p.trmin = 500;
		p.reqmax = 0;
		p.reqmin = 0;

		FFDCore core = new FFDCore();
		core.requireAllMappedExactlyOnce();
		initProblem(core, p);

		Map<Object, List> sol = core.nextMapping();
		assertNotNull(sol);

		int packed = 0;
		for (Object o : sol.keySet()) {
			if (sol.get(o).size() > 0) {
				packed++;
			}
		}

	}

	public Object[] createItemsArray(int itemcount, String prefix) {
		String[] items = new String[itemcount];
		for (int i = 0; i < items.length; i++) {
			items[i] = prefix + "_" + i;
		}
		return items;
	}

	public int[][] createResourceArray(int items, int dims, int rmin, int rmax) {
		int[][] res = new int[items][dims];
		for (int i = 0; i < items; i++) {
			for (int j = 0; j < dims; j++) {
				res[i][j] = random(rmin, rmax);
			}
		}
		return res;
	}

	public void testFFDCore() {
		String[] srcs = new String[] { "a", "b", "c" };
		String[] trgs = new String[] { "1", "2", "3" };
		int[][] ssizes = new int[3][3];
		int[][] tsizes = new int[3][3];

		ssizes[0] = new int[] { 5, 4, 5 };
		ssizes[1] = new int[] { 5, 4, 5 };
		ssizes[2] = new int[] { 5, 4, 5 };

		tsizes[0] = new int[] { 5, 4, 5 };
		tsizes[1] = new int[] { 15, 8, 15 };
		tsizes[2] = new int[] { 5, 4, 5 };

		FFDCore core = new FFDCore();
		core.setSetsToMap(srcs, trgs);
		core.requireMapped("a");
		core.requireMapped("c");
		core.addRequiresMappingConstraint("a", "b");
		core.addExcludesMappingConstraint("c", "b");
		core.setResourceConstraints(ssizes, tsizes);

		Map<Object, List> sol = core.nextMapping();
	}

	public void testBareResourceConstraintsAndDerivedVariableOpt() {
		FFDCore core = new FFDCore();
		core.setSetsToMap(new String[] { "a", "b", "c", "d", "e", "f", "g",
				"h", "i", "j", "k", "l" }, new String[] { "1", "2", "3", "4",
				"5", "6", "7", "8", "9", "10" });
		core
				.setSourceVariableValues("a", new Object[] { "RAM", 11, "CPU",
						20 });
		core.setSourceVariableValues("b", new Object[] { "RAM", 9, "CPU", 25 });
		core
				.setSourceVariableValues("c", new Object[] { "RAM", 15, "CPU",
						32 });
		core
				.setSourceVariableValues("d", new Object[] { "RAM", 19, "CPU",
						32 });
		core.setSourceVariableValues("e", new Object[] { "RAM", 4, "CPU", 32 });
		core.setSourceVariableValues("f", new Object[] { "RAM", 1, "CPU", 32 });
		core.setSourceVariableValues("g", new Object[] { "RAM", 4, "CPU", 52 });
		core.setSourceVariableValues("h", new Object[] { "RAM", 2, "CPU", 32 });
		core
				.setSourceVariableValues("i", new Object[] { "RAM", 18, "CPU",
						22 });
		core.setSourceVariableValues("j", new Object[] { "RAM", 7, "CPU", 19 });
		core.setSourceVariableValues("k", new Object[] { "RAM", 3, "CPU", 37 });
		core.setSourceVariableValues("l", new Object[] { "RAM", 5, "CPU", 52 });

		core.addExcludesMappingConstraint("a", "c");
		core.addExcludesMappingConstraint("f", "b");
		core.addExcludesMappingConstraint("e", "c");
		core.addExcludesMappingConstraint("c", "b");
		// core.addRequiresMappingConstraint("a","b");

		core.addTargetIntResourceConstraint("1", "RAM", 10);
		core.addTargetIntResourceConstraint("2", "RAM", 16);
		core.addTargetIntResourceConstraint("3", "RAM", 12);
		core.addTargetIntResourceConstraint("4", "RAM", 220);
		core.addTargetIntResourceConstraint("5", "RAM", 12);
		core.addTargetIntResourceConstraint("6", "RAM", 10);
		core.addTargetIntResourceConstraint("7", "RAM", 160);
		core.addTargetIntResourceConstraint("8", "RAM", 12);
		core.addTargetIntResourceConstraint("9", "RAM", 22);
		core.addTargetIntResourceConstraint("10", "RAM", 12);
		core.addTargetIntResourceConstraint("1", "CPU", 100);
		core.addTargetIntResourceConstraint("2", "CPU", 100);
		core.addTargetIntResourceConstraint("3", "CPU", 100);
		core.addTargetIntResourceConstraint("4", "CPU", 100);
		core.addTargetIntResourceConstraint("5", "CPU", 100);
		core.addTargetIntResourceConstraint("6", "CPU", 100);
		core.addTargetIntResourceConstraint("7", "CPU", 100);
		core.addTargetIntResourceConstraint("8", "CPU", 100);
		core.addTargetIntResourceConstraint("9", "CPU", 100);
		core.addTargetIntResourceConstraint("10", "CPU", 100);
		core.getResourcePolicies().put("CPU", new RateMonotonicResource());
		core.requireAllMappedExactlyOnce();

		long time = System.currentTimeMillis();
		Map<Object, List> sol = core.nextMapping();
		long end = System.currentTimeMillis();
		long total = end - time;
		assertNotNull(sol);

		for (Object o : sol.keySet()) {
			for (Object t : sol.get(o)) {
				System.out.println(o + "->" + t);
			}
		}
	}

	public void testBareResourceConstraintsAndPostSwap() {
		FFDCore core = new FFDCore();
		core.setSetsToMap(new String[] { "a", "b", "c", "d", "e", "f", "g",
				"h", "i", "j", "k", "l" }, new String[] { "1", "2", "3", "4",
				"5", "6", "7", "8", "9", "10" });
		core.setSourceVariableValues("a", new Object[] { "RAM", 11 });
		core.setSourceVariableValues("b", new Object[] { "RAM", 23 });
		core.setSourceVariableValues("c", new Object[] { "RAM", 21 });
		core.setSourceVariableValues("d", new Object[] { "RAM", 19 });
		core.setSourceVariableValues("e", new Object[] { "RAM", 4 });
		core.setSourceVariableValues("f", new Object[] { "RAM", 1 });
		core.setSourceVariableValues("g", new Object[] { "RAM", 4 });
		core.setSourceVariableValues("h", new Object[] { "RAM", 2 });
		core.setSourceVariableValues("i", new Object[] { "RAM", 18 });
		core.setSourceVariableValues("j", new Object[] { "RAM", 7 });
		core.setSourceVariableValues("k", new Object[] { "RAM", 3 });
		core.setSourceVariableValues("l", new Object[] { "RAM", 5 });

		core.addExcludesMappingConstraint("a", "c");
		core.addExcludesMappingConstraint("f", "b");
		core.addExcludesMappingConstraint("e", "c");
		core.addExcludesMappingConstraint("c", "b");
		// core.addRequiresMappingConstraint("a","b");

		core.addTargetIntResourceConstraint("1", "RAM", 10);
		core.addTargetIntResourceConstraint("2", "RAM", 16);
		core.addTargetIntResourceConstraint("3", "RAM", 12);
		core.addTargetIntResourceConstraint("4", "RAM", 44);
		core.addTargetIntResourceConstraint("5", "RAM", 12);
		core.addTargetIntResourceConstraint("6", "RAM", 10);
		core.addTargetIntResourceConstraint("7", "RAM", 16);
		core.addTargetIntResourceConstraint("8", "RAM", 19);
		core.addTargetIntResourceConstraint("9", "RAM", 20);
		core.addTargetIntResourceConstraint("10", "RAM", 12);
		// core.requireAllMappedExactlyOnce();
		core.requireMapped("c");
		core.mapTo("j", "1");

		long time = System.currentTimeMillis();
		Map sol = core.nextMapping();
		long end = System.currentTimeMillis();
		long total = end - time;
		assertNotNull(sol);
	}

	public void testLargeNonResourceProblem() {
		int totalsrcs = 500;
		int totaltrgs = 30;
		int iter = 200;
		double avg = 0;
		int solved = 0;
		for (int j = 0; j < iter; j++) {
			String[] srcs = new String[totalsrcs];
			String[] trgs = new String[totaltrgs];
			int[][] tsizes = new int[totaltrgs][];
			for (int i = 0; i < totalsrcs; i++) {
				srcs[i] = "source_" + i;
			}
			for (int i = 0; i < totaltrgs; i++) {
				trgs[i] = "target_" + i;
				tsizes[i] = new int[] { random(200, 2000), random(200, 2000) };
			}

			FFDCore core = new FFDCore();
			long start = System.currentTimeMillis();
			ArrayList targets = new ArrayList(totaltrgs);
			targets.addAll(Arrays.asList(trgs));
			ArrayList sources = new ArrayList(totalsrcs);
			sources.addAll(Arrays.asList(srcs));
			core.setSetsToMap(sources, targets);

			int[][] ssizes = new int[totalsrcs][];
			for (int i = 0; i < totalsrcs; i++) {
				double rand = Math.random();
				if (rand < 0.1 && i > 0) {
					rand = Math.random();
					int exc = random(0, i - 1);
					if (exc != i) {
						if (rand < .5) {
							// core.addRequiresMappingConstraint(srcs[i],
							// srcs[exc]);
						} else {
							core.addExcludesMappingConstraint(srcs[i],
									srcs[exc]);
						}
					}
				}
				ssizes[i] = new int[] { random(5, 50), random(5, 50) };
			}

			core.setResourceConstraints(ssizes, tsizes);

			// core.requireAllMappedExactlyOnce();
			long setupend = System.currentTimeMillis();
			Map<Object, List> solution = core.nextMapping();

			if (solution != null) {
				solved++;
				avg += core.getOptimalityLowerBound();
			}

			long end = System.currentTimeMillis();
			// System.out.println("Setup time for problem " + totalsrcs + "X"
			// + totaltrgs + " = " + (setupend - start));
			// System.out.println("Total time for problem " + totalsrcs + "X"
			// + totaltrgs + " = " + (end - start));
		}

		System.out.println("Avg Opt LB:" + (avg / ((double) solved)));
		// Map<Object, List> prev = null;
		// for (int i = 0; i < 1000; i++) {
		// start = System.currentTimeMillis();
		// int src = random(0, totalsrcs - 1);
		// int trg = random(0, totalsrcs - 1);
		// core.addExcludesMappingConstraint(srcs[src], srcs[trg]);
		// solution = core.nextMapping();
		// end = System.currentTimeMillis();
		// System.out.println("Total time for problem " + totalsrcs + "X"
		// + totaltrgs + " = " + (end - start));
		//
		// if (prev != null && solution != null)
		// assertTrue(different(solution, prev));
		//
		// prev = solution;
		// if (solution == null)
		// break;
		// }
	}

	public void testLargeNonResourceProblemAdaptive() {
		int totalsrcs = 500;
		int totaltrgs = 30;
		int iter = 200;
		double avg = 0;
		int solved = 0;
		for (int j = 0; j < iter; j++) {
			String[] srcs = new String[totalsrcs];
			String[] trgs = new String[totaltrgs];
			int[][] tsizes = new int[totaltrgs][];
			for (int i = 0; i < totalsrcs; i++) {
				srcs[i] = "source_" + i;
			}
			for (int i = 0; i < totaltrgs; i++) {
				trgs[i] = "target_" + i;
				tsizes[i] = new int[] { random(200, 2000), random(200, 2000) };
			}

			FFDCore core = new FFDCore();
			long start = System.currentTimeMillis();
			ArrayList targets = new ArrayList(totaltrgs);
			targets.addAll(Arrays.asList(trgs));
			ArrayList sources = new ArrayList(totalsrcs);
			sources.addAll(Arrays.asList(srcs));
			core.setSetsToMap(sources, targets);

			int[][] ssizes = new int[totalsrcs][];
			for (int i = 0; i < totalsrcs; i++) {
				double rand = Math.random();
				if (rand < 0.1 && i > 0) {
					rand = Math.random();
					int exc = random(0, i - 1);
					if (exc != i) {
						if (rand < .5) {
							core.addRequiresMappingConstraint(srcs[i],
									srcs[exc]);
						} else {
							core.addExcludesMappingConstraint(srcs[i],
									srcs[exc]);
						}
					}
				}
				ssizes[i] = new int[] { random(5, 50), random(5, 50) };
			}

			core.setResourceConstraints(ssizes, tsizes);

			// core.requireAllMappedExactlyOnce();
			long setupend = System.currentTimeMillis();
			Map<Object, List> solution = core.nextMapping();

			if (solution != null) {
				solved++;
				avg += core.getOptimalityLowerBound();
			}

			long end = System.currentTimeMillis();
			// System.out.println("Setup time for problem " + totalsrcs + "X"
			// + totaltrgs + " = " + (setupend - start));
			// System.out.println("Total time for problem " + totalsrcs + "X"
			// + totaltrgs + " = " + (end - start));
		}

		// System.out.println("Adaptive Avg Opt LB:"+(avg/((double)solved)));
		// Map<Object, List> prev = null;
		// for (int i = 0; i < 1000; i++) {
		// start = System.currentTimeMillis();
		// int src = random(0, totalsrcs - 1);
		// int trg = random(0, totalsrcs - 1);
		// core.addExcludesMappingConstraint(srcs[src], srcs[trg]);
		// solution = core.nextMapping();
		// end = System.currentTimeMillis();
		// System.out.println("Total time for problem " + totalsrcs + "X"
		// + totaltrgs + " = " + (end - start));
		//
		// if (prev != null && solution != null)
		// assertTrue(different(solution, prev));
		//
		// prev = solution;
		// if (solution == null)
		// break;
		// }
	}

	public boolean different(Map<Object, List> sol1, Map<Object, List> sol2) {
		for (Object o : sol1.keySet()) {
			List l1 = sol1.get(o);
			List l2 = sol2.get(o);
			if (l1.size() != l2.size())
				return true;
			else {
				for (Object t : l1) {
					if (!l2.contains(t))
						return true;
				}
			}
		}
		return false;
	}

	public int random(int min, int max) {
		double rand = Math.random();
		double range = max - min;
		double delta = range * rand;
		return (int) Math.rint((min + delta));
	}

	// public void testCoreSolver(){
	// RefreshVectorCore core = new RefreshVectorCore();
	// core.setSetsToMap(new String[]{"a","b","c"}, new String[]{"1","2","3"});
	// core.setUseBranchAndBound(false);
	// //core.requireMapped("a");
	// //core.addExcludesMappingConstraint("a", "c");
	// //core.addRequiresMappingConstraint("a", "b");
	// Map<Object,List> sol = core.nextMapping();
	// while(sol != null){
	// System.out.println("#################");
	// for(Object o : sol.get("a")){
	// System.out.println("a->"+o);
	// }
	// for(Object o : sol.get("b")){
	// System.out.println("b->"+o);
	// }
	// for(Object o : sol.get("c")){
	// System.out.println("c->"+o);
	// }
	// sol = core.nextMapping();
	// }
	// }
}
