package org.ascent.pso.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ascent.Util;
import org.ascent.VectorSolution;
import org.ascent.deployment.DeploymentWithNetworkMinimizationConfig;

public class LargeProblemNetworkMinConfig extends
		DeploymentWithNetworkMinimizationConfig {

	
	public VectorSolution[] createInitialSolutions(int count) {
		VectorSolution[] sols = new VectorSolution[count];
		for (int i = 0; i < count; i++) {
			ResourceResidual resid = new ResourceResidual();
			resid.setHostedCount(new int[components_.length]);
			List<Component> comps = new ArrayList<Component>(count);
			List<Component> a = Arrays.asList(components_);
			comps.addAll(a);

			List<Node> n = Arrays.asList(nodes_);
			List<Node> nodes = new ArrayList<Node>(nodes_.length);
			nodes.addAll(n);

			int[] pos = new int[components_.length];

			int[] hosted = new int[components_.length];
			
			while (comps.size() > 0) {
				int index = Util.random(0, comps.size() - 1);
				Component c = comps.remove(index);
				boolean deployed = false;
				while (!deployed) {

					for (int j = 0; j < nodes.size(); j++) {
						Node node = nodes.get(j);
						int nin = node.getId();

						int[] avail = resid.getResourceResiduals(nodes_[nin]);
						int[] after = residuals(avail, c.getResources());
						
//						for (int k = 0; k < avail.length; k++) {
						int k = 0;
							if (rateMonotonicResourceMap_[k] == 1
									&& after[k] < 0
									&& hosted[nin] == 0) {
								resid.deploy(c, node);
								hosted[nin]++;
								nodes.remove(node);
								pos[c.getId()] = nin;
								deployed = true;
								break;
							} else if (after[k] > 0) {
								hosted[nin]++;
								resid.deploy(c, node);
								pos[c.getId()] = nin;
								deployed = true;
								break;
							}
							
//						}
					}
				}
			}
			sols[i] = new VectorSolution(pos);
		}
		return sols;
	}

	public int scoreDeployment(DeploymentPlan plan) {
		boolean valid = true;

		ResourceResidual residual = new ResourceResidual();
		residual.deploy(plan);

		if (residual.valid()) {
			int score = 0;
			for (Interaction i : interactions_) {
				if (plan.getChannel(i) instanceof LocalHostLink) {
					score += (i.getResources()[0] * i.getRate());
				}
			}
			return score;
		} else {
			return -1
					* (residual.getLinkExhaustions().size()
							+ residual.getHostExhaustions().size() + residual.getDisconnections()
							.size());
		}
	}

}
