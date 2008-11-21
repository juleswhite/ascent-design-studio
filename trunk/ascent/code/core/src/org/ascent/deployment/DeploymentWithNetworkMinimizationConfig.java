package org.ascent.deployment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ascent.ProblemConfigImpl;
import org.ascent.ResourceConsumptionPolicy;
import org.ascent.VectorSolution;
import org.ascent.binpacking.ValueFunction;
import org.ascent.pso.Pso;

public class DeploymentWithNetworkMinimizationConfig extends ProblemConfigImpl {

	public class ResourceResidual {
		protected List<Interaction> disconnections_ = new ArrayList<Interaction>();
		protected List<NetworkLink> linkExhaustions_ = new ArrayList<NetworkLink>();
		protected List<Node> hostExhaustions_ = new ArrayList<Node>();

		private int[][] nodeResiduals_;
		private int[][] networkResiduals_;
		private int[] hostedCount_;

		public ResourceResidual() {
			nodeResiduals_ = new int[nodes_.length][nodes_[0].resources_.length];
			for (Node n : nodes_) {
				int[] resid = new int[n.resources_.length];
				System.arraycopy(n.resources_, 0, resid, 0, resid.length);
				// nodeResiduals_.put(n, resid);
				nodeResiduals_[n.id_] = resid;
			}

			networkResiduals_ = new int[networks_.length][networks_[0].resources_.length];
			for (NetworkLink n : networks_) {
				int[] resid = new int[n.resources_.length];
				System.arraycopy(n.resources_, 0, resid, 0, resid.length);
				networkResiduals_[n.id_] = resid;
			}
		}

		public int[] getHostedCount() {
			return hostedCount_;
		}

		public void setHostedCount(int[] hostedCount) {
			hostedCount_ = hostedCount;
		}

		public void deploy(Interaction inter, NetworkLink nl) {
			int[] resid = residuals(getResourceResiduals(nl), inter.resources_);
			// networkResiduals_.put(nl, resid);
			networkResiduals_[nl.id_] = resid;
		}

		public void deploy(Component c, Node n) {
			int[] resid = residuals(getResourceResiduals(n), c.resources_);

			// Since the resources are initd to 69 rather than
			// 100 we have to deal with the case of a single
			// app that consumes more than 69 CPU but is
			// still schedulable
			if (hostedCount_[n.id_] == 1) {
				for (int i = 0; i < resid.length; i++) {
					if (rateMonotonicResourceMap_ != null
							&& rateMonotonicResourceMap_[i] == 1
							&& resid[i] < 0)
						resid[i] = 0;
				}
			}
//			if(resourcePolicies_ != null){
//				for(int i = 0; i < resid.length; i++){
//					if(resourcePolicies_[i] != null){
//						Node[]
//						resid[i] = resourcePolicies_[i].getResourceResidual(consumers, producers, avail, consumed)
//					}
//				}
//			}
			// nodeResiduals_.put(n, resid);
			nodeResiduals_[n.id_] = resid;
		}

		public int[] getResourceResiduals(Node n) {
			// return nodeResiduals_.get(n);
			return nodeResiduals_[n.id_];
		}

		public int[] getResourceResiduals(NetworkLink nl) {
			// return networkResiduals_.get(nl);
			return networkResiduals_[nl.id_];
		}

		public void deploy(DeploymentPlan plan) {
			hostedCount_ = new int[nodes_.length];

			for (Component c : components_) {
				Node n = plan.getHost(c);
				hostedCount_[n.id_]++;
				deploy(c, n);
			}
			for (Interaction inter : interactions_) {
				NetworkLink l = plan.getChannel(inter);
				if (l == null) {
					disconnections_.add(inter);
				} else {
					deploy(inter, l);
				}
			}
			for (Node n : nodes_) {
				if (exhausted(getResourceResiduals(n))) {
					hostExhaustions_.add(n);
				}
			}
			for (NetworkLink link : networks_) {
				if (exhausted(getResourceResiduals(link))) {
					linkExhaustions_.add(link);
				}
			}
		}

		public boolean exhausted(int[] residual) {
			for (int i = 0; i < residual.length; i++) {
				if (residual[i] < 0)
					return true;
			}
			return false;
		}

		public boolean valid() {
			return linkExhaustions_.size() == 0 && disconnections_.size() == 0
					&& hostExhaustions_.size() == 0;
		}

		public String toString() {
			String str = "Residual Resources {\n";

			for (Node n : nodes_) {
				int[] resid = getResourceResiduals(n);
				str += "\t\t"
						+ n.label_
						+ "("
						+ n.id_
						+ ")"
						+ DeploymentWithNetworkMinimizationConfig.this
								.toString(resid) + "\n";
			}
			for (NetworkLink n : networks_) {
				int[] resid = getResourceResiduals(n);
				str += "\t\t-->"
						+ n.label_
						+ "("
						+ n.id_
						+ ")"
						+ DeploymentWithNetworkMinimizationConfig.this
								.toString(resid) + "\n";
			}
			str += "}\n";
			return str;
		}
	}

	public class DeploymentPlan {
		private VectorSolution solution_;

		public DeploymentPlan(VectorSolution solution) {
			super();
			solution_ = solution;
		}

		public Node getHost(Component c) {
			int hostid = solution_.getPosition()[c.id_];
			return nodes_[hostid];
		}

		public NetworkLink getChannel(Interaction i) {
			Node[] hosts = getHosts(i.participants_);
			NetworkLink[] links = getLinks(hosts);
			if (links.length > 0)
				return links[0];
			else
				return null;
		}

		public Node[] getHosts(Component[] comps) {
			List<Node> nodes = new ArrayList<Node>();
			for (Component c : comps) {
				Node n = getHost(c);
				if (!nodes.contains(n)) {
					nodes.add(n);
				}
			}
			return nodes.toArray(new Node[0]);
		}

		public Component[] getHostedComponents(Node n) {
			List<Component> hosted = new ArrayList<Component>();
			for (int i = 0; i < solution_.getPosition().length; i++) {
				int host = solution_.getPosition()[i];
				if (host == n.id_) {
					hosted.add(components_[i]);
				}
			}
			return hosted.toArray(new Component[0]);
		}

		public String toString() {
			String str = "Deployment Plan {\n";
			for (Component c : components_) {
				str += "\t\t" + c.label_ + "-->" + getHost(c).label_ + "\n";
			}
			for (Interaction inter : interactions_) {
				NetworkLink l = getChannel(inter);
				if (l != null) {
					str += "\t\t" + inter.label_ + "-->" + l.label_ + "\n";
				} else {
					str += "\t\t" + inter.label_ + "--> Disconnected\n";
				}
			}
			str += "}\n";
			return str;
		}
	}

	public class ModelElement implements Comparable<ModelElement> {
		protected int id_;
		protected String label_;
		protected int[] resources_;

		public ModelElement(int id, String label, int[] resources) {
			super();
			id_ = id;
			label_ = label;
			resources_ = resources;
		}

		public int compareTo(ModelElement o) {
			return id_ - o.id_;
		}

		public String toString() {
			return label_
					+ " ("
					+ id_
					+ ") "
					+ DeploymentWithNetworkMinimizationConfig.this
							.toString(resources_);
		}
	}

	public class NetworkLink extends ModelElement {
		private Node[] nodes_;

		public NetworkLink(int id, String label, Node[] nodes, int[] resources) {
			super(id, label, resources);
			nodes_ = nodes;
			Arrays.sort(nodes_);
		}

		public boolean connectsTo(Node n) {
			return Arrays.binarySearch(nodes_, n) > -1;
		}
	}

	public class LocalHostLink extends NetworkLink {
		public LocalHostLink(int id, String label, Node node, int rtotal) {
			super(id, label, new Node[] { node }, new int[rtotal]);
			for (int i = 0; i < rtotal; i++) {
				resources_[i] = Integer.MAX_VALUE;
			}
		}
	}

	public class Node extends ModelElement {
		private NetworkLink[] networkLinks_;

		public Node(int id, String label, int[] resources) {
			super(id, label, resources);
		}

	}

	public class Component extends ModelElement {
		private Interaction[] interactions_;

		public Component(int id, String label, int[] resources) {
			super(id, label, resources);
		}
	}

	public class Interaction extends ModelElement {
		private Component[] participants_;
		protected double rate_;

		public Interaction(int id, String label, int[] resources, double rate) {
			super(id, label, resources);
			rate_ = rate;
		}
	}

	private ValueFunction<VectorSolution> fitnessFunction_ = new ValueFunction<VectorSolution>() {

		public double getValue(VectorSolution src) {
			if (src.getArtifact() == null) {
				DeploymentPlan plan = new DeploymentPlan(src);
				int score = scoreDeployment(plan);
				src.setArtifact(score);
			}
			return (Integer) src.getArtifact();
		}
	};

	protected NetworkLink[] networks_;
	protected Component[] components_;
	protected Node[] nodes_;
	protected Interaction[] interactions_;
	protected int[] networkResourceCoeffs_;
	protected ResourceConsumptionPolicy[] resourcePolicies_;
	protected int[] rateMonotonicResourceMap_;
	private boolean acceptInfeasibleSolutions_ = true;

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

	public NetworkLink[] getLinks(Node[] nodes) {
		if (nodes.length > 1) {
			ArrayList<NetworkLink> links = new ArrayList<NetworkLink>();
			NetworkLink[] possible = getLinks(nodes[0], nodes[1]);
			for (NetworkLink l : possible) {
				boolean valid = true;
				for (int i = 2; i < nodes.length; i++) {
					if (!l.connectsTo(nodes[i])) {
						valid = false;
						break;
					}
				}
				if (valid) {
					links.add(l);
				}
			}
			return links.toArray(new NetworkLink[0]);
		} else {
			return new NetworkLink[] { new LocalHostLink(0, "LocalHostLink",
					nodes[0], interactions_[0].resources_.length) };
		}
	}

	public NetworkLink[] getLinks(Node a, Node b) {
		List<NetworkLink> links = new ArrayList<NetworkLink>();
		for (NetworkLink l : a.networkLinks_) {
			if (l.connectsTo(b)) {
				links.add(l);
			}
		}
		return links.toArray(new NetworkLink[0]);
	}

	public boolean fits(ModelElement[] hosted, ModelElement host) {
		int[] resids = residuals(hosted, host);

		for (int r : resids) {
			if (r < 0) {
				return false;
			}
		}

		return true;
	}

	public int scoreDeployment(DeploymentPlan plan) {
		boolean valid = true;

		ResourceResidual residual = new ResourceResidual();
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

	public int[] residuals(ModelElement[] hosted, ModelElement host) {
		int[] sums = sumResources(hosted);
		int[] avail = host.resources_;
		int[] resid = residuals(avail, sums);
		return resid;
	}

	public int[] residuals(int[] available, int[] consumed) {
		if (available.length != consumed.length)
			throw new DeploymentProblemSpecException(
					"The network links and interactions must specify an identical number of resources (provided/consumed)!");

		int[] resid = new int[available.length];
		for (int i = 0; i < available.length; i++) {
			resid[i] = available[i] - consumed[i];
		}
		return resid;
	}

	public int[] sumResources(ModelElement[] els) {
		int total = els[0].resources_.length;
		int[] res = new int[total];
		for (int i = 0; i < total; i++)
			for (int j = 0; j < els.length; j++)
				res[i] += els[j].resources_[i];

		return res;
	}

	public int[] sum(int[] a, int[] b) {
		int total = a.length;
		int[] res = new int[total];
		for (int i = 0; i < total; i++)
			res[i] += (a[i] + b[i]);

		return res;
	}

	public String toString() {
		String str = "Deployment Problem {\n";
		for (Node n : nodes_) {
			str += "\t" + n + "\n";
		}
		for (NetworkLink n : networks_) {
			str += "\t-->" + n + "\n";
			str += "\t\t[\n";
			for (Node ep : n.nodes_) {
				str += "\t\t--" + ep.label_ + "(" + ep.id_ + ")\n";
			}
			str += "\t\t]\n";
		}
		for (Component c : components_) {
			str += "\t" + c + "\n";
			if (c.interactions_.length > 0) {
				str += "\t\tinteractions-->[\n";
				for (Interaction inter : c.interactions_) {
					str += "\t\t" + inter + "\n";
				}
				str += "\t\t]\n";
			}
		}

		str += "}";
		return str;
	}

	public Node addNode(String id, int[] res) {
		Node n = new Node(nStart_.size(), id, res);
		n.networkLinks_ = new NetworkLink[0];
		nStart_.add(n);
		return n;
	}

	public Component addComponent(String id, int[] res) {
		Component c = new Component(cStart_.size(), id, res);
		c.interactions_ = new Interaction[0];
		cStart_.add(c);
		return c;
	}

	public NetworkLink addNetwork(String id, Node[] nodes, int[] res) {
		NetworkLink nl = new NetworkLink(nlStart_.size(), id, nodes, res);
		for (Node n : nodes) {
			NetworkLink[] nets = n.networkLinks_;
			if (nets == null) {
				nets = new NetworkLink[1];
				n.networkLinks_ = nets;
			} else {
				NetworkLink[] nints = new NetworkLink[nets.length + 1];
				System.arraycopy(nets, 0, nints, 0, nets.length);
				n.networkLinks_ = nints;
			}
			n.networkLinks_[n.networkLinks_.length - 1] = nl;
		}
		nlStart_.add(nl);
		return nl;
	}

	public Interaction addInteraction(String id, int[] res, double rate,
			Component[] comps) {
		Interaction i = new Interaction(iStart_.size(), id, res, rate);
		i.participants_ = comps;

		iStart_.add(i);
		for (Component c : comps) {
			Interaction[] ints = c.interactions_;
			if (ints == null) {
				ints = new Interaction[1];
				c.interactions_ = ints;
			} else {
				Interaction[] nints = new Interaction[ints.length + 1];
				System.arraycopy(ints, 0, nints, 0, ints.length);
				c.interactions_ = nints;
			}
			c.interactions_[c.interactions_.length - 1] = i;
		}
		return i;
	}

	public boolean isFeasible(VectorSolution vs) {
		if (!acceptInfeasibleSolutions_) {
			DeploymentPlan plan = new DeploymentPlan(vs);
			ResourceResidual resid = new ResourceResidual();
			resid.deploy(plan);
			return resid.valid();
		} else {
			return true;
		}
	}

	public String toString(int[] res) {
		String str = "[";
		for (int i = 0; i < res.length; i++) {
			if (i != 0) {
				str += ",";
			}
			str += res[i];
		}
		str += "]";
		return str;
	}

	public void printSolutionStats(VectorSolution vs) {
		DeploymentPlan plan = new DeploymentPlan(vs);
		ResourceResidual resid = new ResourceResidual();
		resid.deploy(plan);

		System.out.println("Valid:" + resid.valid());
		System.out.println("Score:" + scoreDeployment(plan));
		System.out.println(plan);
		System.out.println(resid);
		System.out.println("----------------------------");
		System.out.println("Valid:" + resid.valid());
		System.out.println("Score:" + scoreDeployment(plan));
		for(Node n : nodes_){
			if(resid.getHostedCount()[n.id_] == 0){
				System.out.println("\t"+n.label_+" is free");
			}
		}
	}

	public int[] getRateMonotonicResourceMap() {
		return rateMonotonicResourceMap_;
	}

	public void setRateMonotonicResourceMap(int[] rateMonotonicResourceMap) {
		rateMonotonicResourceMap_ = rateMonotonicResourceMap;
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
