# Introduction #

The ASCENT deployment solver can use Excel to capture deployment requirements. To get started, download the latest copy of [deployment\_problem\_template.xls](http://ascent-design-studio.googlecode.com/svn/trunk/ascent/code/excel/templates/deployment_problem_template.xls) workbook.

# Nodes #
The "Nodes" worksheet contains a list of the hardware nodes that the software can be deployed to. The first column specifies the available CPU utilization that can be consumed on the node. Currently, only 100% utilization is supported (this will change in future releases). All remaining columns specify consumable resources on each processor. For example, the second column specifies a memory resource on each processor that cannot be exceeded by the components deployed to it.

# Networks #
The "Networks" worksheet specifies that networks that connect the nodes. The first column specifies the available bandwidth on the network. Each remaining column with an "x" represents a node that the network connects. The example contains a single network connected to all nodes. Further networks can be added as rows below. The deployment solver will search for solutions that ensure components with interactions between them are either co-located or on nodes connected through a network (gateways between networks cannot be specified...yet).

# Real-time Scheduling #
The "Component Scheduling" worksheet specifies real-time tasks that are associated with each component. Deploying a component on a node requires that all of its constituent tasks be schedulable on the node. ASCENT assumes that a component's tasks can all be scheduled on a node with 100% free utilization. ASCENT uses an adaptive bin-packing algorithm to guarantee that a deployment plan it produces honors these scheduling constraints. The first column specifies the ID of a component. Subsequent columns specify task periods and the utilization consumed by a component's task for that period. Cells should be left BLANK (not set to 0%) if a component does not have a task with a given period. The final column specifies aggregate CPU utilization.

# Resources Consumed by Components #
The "Component Resources" worksheet specifies the resources, such as Memory (not CPU utilization), consumed by a component. Currently, the columns specifying the resource types must be ordered exactly the same as they are in the node specifications from the "Nodes" worksheet. The nth column resource specification from the "Nodes" worksheet should be the n-1th column in the "Component Resources" worksheet. Each column specifies a resource type and the cell at i,j represents the amount of that resource consumed by the ith component.

# Component Co-location #
The "Component Co-location" worksheet specifies constraints on what components can and cannot be placed together. An "x" in the cell i,j means that the components represented by row i and column j cannot be deployed together. An "r" means that the components must be deployed together.

# Component Interactions #
The "Component Interactions" worksheet specifies messaging or interactions between components. If components are co-located, the interaction takes place locally. If the components are not placed on the same host, they must have a connecting network with sufficient bandwidth to support the interaction. The signal column specifies an ID for the interaction. The transmit rate is the frequency of the interaction. The length column specifies the size of each message sent. The Sender column specifies the ID of the sending component. The Receiver column specifies the receiver. The remaining fields are currently for documentation only (this will eventually change).

## Running the Solver on the Excel Problem ##

The following is a code example to load and solve the problem specified in Excel.

```
		NetworkBandwidthMinimizingPlanner problem = new NetworkBandwidthMinimizingPlanner();
		ExcelDeploymentConfig config = new ExcelDeploymentConfig();
		try {
			config.load(new File("your_excel_File.xls"), problem);
		} catch (Exception e) {
			e.printStackTrace();
		}

		problem.init();

		double grate = 2;// the global learning rate
		double lrate = 0.5;// the local learning rate
		double intertia = 1;// the particle intertia impact
		int maxv = 4;// the max particle velocity
		int particles = 200;// the total number of particles
		int iterations = 20;// the total number of iterations per solver
							// invocation

		Pso pso = new Pso(problem);
		pso.setTotalParticles(particles);
		pso.setVelocityMax(maxv);
		pso.setLocalLearningRate(lrate);
		pso.setGlobalLearningRate(grate);
		pso.setIterations(20);

		Comparator<VectorSolution> comp = new VectorSolutionComparator(problem
				.getFitnessFunction());
		VectorSolution sol = pso.solve(problem.getFitnessFunction());
		DeploymentPlan plan = new DeploymentPlan(problem, sol);

		if (plan.isValid()) {
			for (Component c : problem.getComponents()) {
				System.out.println("Deploy " + c.getLabel() + " to "
						+ plan.getHost(c).getLabel());
			}
		} else {
			System.out.println("No solution found.");
		}
```