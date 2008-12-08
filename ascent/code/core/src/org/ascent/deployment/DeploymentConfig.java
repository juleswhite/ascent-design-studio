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
import java.util.List;
import java.util.Map;

import org.ascent.ProblemConfigImpl;
import org.ascent.ResourceConsumptionPolicy;
import org.ascent.VectorSolution;
import org.ascent.binpacking.Packer;
import org.ascent.binpacking.ValueFunction;

public class DeploymentConfig extends ProblemConfigImpl{

	protected NetworkLink[] networks_;
	protected Component[] components_;
	protected Node[] nodes_;
	protected Interaction[] interactions_;
	protected int[] networkResourceCoeffs_;
	protected ResourceConsumptionPolicy[] resourcePolicies_;
	protected Packer packer_ = new Packer();
	protected boolean acceptInfeasibleSolutions_ = true;
	
	public DeploymentConfig(int positions, int bmin, int bmax) {
		super(positions, bmin, bmax);
	}

	public Map<Object, ResourceConsumptionPolicy> getResourceConsumptionPolicies() {
		return packer_.getResourceConsumptionPolicies();
	}

	public NetworkLink[] getNetworks() {
		return networks_;
	}

	public void setNetworks(NetworkLink[] networks) {
		networks_ = networks;
	}

	public Component[] getComponents() {
		return components_;
	}

	public void setComponents(Component[] components) {
		components_ = components;
	}

	public Node[] getNodes() {
		return nodes_;
	}

	public void setNodes(Node[] nodes) {
		nodes_ = nodes;
	}

	public Interaction[] getInteractions() {
		return interactions_;
	}

	public void setInteractions(Interaction[] interactions) {
		interactions_ = interactions;
	}

	public ResourceConsumptionPolicy[] getResourcePolicies() {
		return resourcePolicies_;
	}

	public void setResourcePolicies(ResourceConsumptionPolicy[] resourcePolicies) {
		resourcePolicies_ = resourcePolicies;
	}

	public boolean isAcceptInfeasibleSolutions() {
		return acceptInfeasibleSolutions_;
	}

	public void setAcceptInfeasibleSolutions(boolean acceptInfeasibleSolutions) {
		acceptInfeasibleSolutions_ = acceptInfeasibleSolutions;
	}
	
	public Packer getPacker() {
		return packer_;
	}

	public void setPacker(Packer packer) {
		packer_ = packer;
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
	
	public static String toString(int[] res) {
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
	
	public NetworkLink[] getLinks(Node a, Node b) {
		List<NetworkLink> links = new ArrayList<NetworkLink>();
		for (NetworkLink l : a.getNetworkLinks()) {
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
	
	

	public String toString() {
		String str = "Deployment Problem {\n";
		for (Node n : nodes_) {
			str += "\t" + n + "\n";
		}
		for (NetworkLink n : networks_) {
			str += "\t-->" + n + "\n";
			str += "\t\t[\n";
			for (Node ep : n.getNodes()) {
				str += "\t\t--" + ep.label_ + "(" + ep.id_ + ")\n";
			}
			str += "\t\t]\n";
		}
		for (Component c : components_) {
			str += "\t" + c + "\n";
			if (c.getInteractions().length > 0) {
				str += "\t\tinteractions-->[\n";
				for (Interaction inter : c.getInteractions()) {
					str += "\t\t" + inter + "\n";
				}
				str += "\t\t]\n";
			}
		}

		str += "}";
		return str;
	}
}
