/**************************************************************************
 * Copyright 2008 Jules White                                              *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/

package org.ascent.deployment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ascent.VectorSolution;
import org.ascent.binpacking.ValueFunction;
import org.ascent.pso.Pso;

public class DeploymentWithNetworkMinimizationConfig extends DeploymentConfig {

	
	private ValueFunction<VectorSolution> fitnessFunction_ = new ValueFunction<VectorSolution>() {

		public double getValue(VectorSolution src) {
			if (src.getArtifact() == null) {
				DeploymentPlan plan = new DeploymentPlan(DeploymentWithNetworkMinimizationConfig.this,src);
				int score = scoreDeployment(plan);
				src.setArtifact(score);
			}
			return (Integer) src.getArtifact();
		}
	};

	private List<Node> nStart_ = new ArrayList<Node>();
	private List<Component> cStart_ = new ArrayList<Component>();
	private List<NetworkLink> nlStart_ = new ArrayList<NetworkLink>();
	private List<Interaction> iStart_ = new ArrayList<Interaction>();

	public DeploymentWithNetworkMinimizationConfig(Node[] nodes,
			NetworkLink[] networks, Component[] components,
			Interaction[] interactions) {
		super(components.length, 0, nodes.length - 1);
		nodes_ = nodes;
		networks_ = networks;
		components_ = components;
		interactions_ = interactions;

		orderElements();
	}

	public DeploymentWithNetworkMinimizationConfig() {
		super(0, 0, 0);
	}

	public void init() {
		nodes_ = nStart_.toArray(new Node[0]);
		components_ = cStart_.toArray(new Component[0]);
		networks_ = nlStart_.toArray(new NetworkLink[0]);
		interactions_ = iStart_.toArray(new Interaction[0]);

		if (boundaries_.length != components_.length) {
			boundaries_ = new int[components_.length][2];
			for (int i = 0; i < boundaries_.length; i++) {
				boundaries_[i] = new int[] { 0, nodes_.length - 1 };
			}
		}
	}

	protected void orderElements() {
		Arrays.sort(networks_);
		Arrays.sort(nodes_);
		Arrays.sort(components_);
		Arrays.sort(interactions_);
	}

	public int scoreDeployment(DeploymentPlan plan) {
		boolean valid = true;

		ResourceResidual residual = new ResourceResidual(this);
		residual.deploy(plan);

		if (residual.valid()) {
			int[] capacity = new int[networks_[0].resources_.length];
			for (NetworkLink nl : networks_) {
				capacity = sum(capacity, residual.getResourceResiduals(nl));
			}
			int score = 0;
			for (int i = 0; i < capacity.length; i++) {
				int coeff = (networkResourceCoeffs_ == null) ? 1
						: networkResourceCoeffs_[i];
				score += (capacity[i] * coeff);
			}

			return score;
		} else {
			return -1
					* (residual.linkExhaustions_.size()
							+ residual.hostExhaustions_.size() + residual.disconnections_
							.size());
		}
	}

	

	public Node addNode(String id, int[] res) {
		Node n = new Node(nStart_.size(), id, res);
		n.setNetworkLinks(new NetworkLink[0]);
		nStart_.add(n);
		return n;
	}

	public Component addComponent(String id, int[] res) {
		Component c = new Component(cStart_.size(), id, res);
		c.setInteractions(new Interaction[0]);
		cStart_.add(c);
		return c;
	}

	public NetworkLink addNetwork(String id, Node[] nodes, int[] res) {
		NetworkLink nl = new NetworkLink(nlStart_.size(), id, nodes, res);
		for (Node n : nodes) {
			NetworkLink[] nets = n.getNetworkLinks();
			if (nets == null) {
				nets = new NetworkLink[1];
				n.setNetworkLinks(nets);
			} else {
				NetworkLink[] nints = new NetworkLink[nets.length + 1];
				System.arraycopy(nets, 0, nints, 0, nets.length);
				n.setNetworkLinks(nints);
			}
			n.getNetworkLinks()[n.getNetworkLinks().length - 1] = nl;
		}
		nlStart_.add(nl);
		return nl;
	}

	public Interaction addInteraction(String id, int[] res, double rate,
			Component[] comps) {
		Interaction i = new Interaction(iStart_.size(), id, res, rate);
		i.setParticipants(comps);

		iStart_.add(i);
		for (Component c : comps) {
			Interaction[] ints = c.getInteractions();
			if (ints == null) {
				ints = new Interaction[1];
				c.setInteractions(ints);
			} else {
				Interaction[] nints = new Interaction[ints.length + 1];
				System.arraycopy(ints, 0, nints, 0, ints.length);
				c.setInteractions(nints);
			}
			c.getInteractions()[c.getInteractions().length - 1] = i;
		}
		return i;
	}

	public void printSolutionStats(VectorSolution vs) {
		DeploymentPlan plan = new DeploymentPlan(this,vs);
		ResourceResidual resid = new ResourceResidual(this);
		resid.deploy(plan);

		System.out.println("Valid:" + resid.valid());
		System.out.println("Score:" + scoreDeployment(plan));
		System.out.println(plan);
		System.out.println(resid);
		System.out.println("----------------------------");
		System.out.println("Valid:" + resid.valid());
		System.out.println("Score:" + scoreDeployment(plan));
		for (Node n : nodes_) {
			if (resid.getHostedCount()[n.id_] == 0) {
				System.out.println("\t" + n.label_ + " is free");
			}
		}
	}

	public ValueFunction<VectorSolution> getFitnessFunction() {
		return fitnessFunction_;
	}

	public void setFitnessFunction(ValueFunction<VectorSolution> fitnessFunction) {
		fitnessFunction_ = fitnessFunction;
	}

	public static void main(String[] args) {
		DeploymentWithNetworkMinimizationConfig problem = new DeploymentWithNetworkMinimizationConfig();

		Node p1 = problem.addNode("P1", new int[] { 69 });
		Node p2 = problem.addNode("P2", new int[] { 69 });
		Node p21 = problem.addNode("P21", new int[] { 69 });
		Node p22 = problem.addNode("P22", new int[] { 69 });
		Node p23 = problem.addNode("P23", new int[] { 69 });
		Node p24 = problem.addNode("P24", new int[] { 69 });
		Node p25 = problem.addNode("P25", new int[] { 69 });
		Node p26 = problem.addNode("P26", new int[] { 69 });
		Node p27 = problem.addNode("P27", new int[] { 69 });
		Node p28 = problem.addNode("P28", new int[] { 69 });
		Node p29 = problem.addNode("P29", new int[] { 69 });
		Node p30 = problem.addNode("P30", new int[] { 69 });
		Node p31 = problem.addNode("P31", new int[] { 69 });
		Node p32 = problem.addNode("P32", new int[] { 69 });

		NetworkLink nl1 = problem.addNetwork("LocalArea1", new Node[] { p1, p2,
				p21, p22, p23, p24, p25, p26, p27, p28, p29, p30, p31, p32 },
				new int[] { Integer.MAX_VALUE });

		Component a1 = problem.addComponent("App1", new int[] { 31 });
		Component a2 = problem.addComponent("App2", new int[] { 31 });
		Component a3 = problem.addComponent("App3", new int[] { 94 });
		Component a4 = problem.addComponent("App4", new int[] { 35 });
		Component a5 = problem.addComponent("App5", new int[] { 44 });
		Component a6 = problem.addComponent("App6", new int[] { 43 });
		Component a7 = problem.addComponent("App7", new int[] { 30 });
		Component a8 = problem.addComponent("App8", new int[] { 58 });
		Component a9 = problem.addComponent("App9", new int[] { 98 });
		Component a10 = problem.addComponent("App10", new int[] { 27 });
		Component a11 = problem.addComponent("App11", new int[] { 54 });
		Component a12 = problem.addComponent("App12", new int[] { 96 });
		Component a13 = problem.addComponent("App13", new int[] { 41 });
		Component a14 = problem.addComponent("App14", new int[] { 37 });

		// Interaction i1 = problem.addInteraction("Sensor1--Sensor5",
		// new int[] { 234 }, new Component[] { c1, c5 });
		// Interaction i2 = problem.addInteraction("Sensor3--Sensor5",
		// new int[] { 804 }, new Component[] { c3, c5 });
		// Interaction i3 = problem.addInteraction("Sensor2--Sensor4",
		// new int[] { 234 }, new Component[] { c2, c4 });

		problem.init();

		System.out.println(problem);

		Pso pso = new Pso(problem);
		VectorSolution sol = pso.solve(problem.fitnessFunction_);
		if (sol != null)
			sol = pso.solve(problem.fitnessFunction_);

		problem.printSolutionStats(sol);
	}
}
