package org.ascent.deployment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ascent.VectorSolution;
import org.ascent.binpacking.BinPackingProblem;
import org.ascent.binpacking.FFDBinPacker;
import org.ascent.binpacking.RandomItemPacker;

public class NetworkBandwidthMinimizingPlanner extends
		DeploymentWithNetworkMinimizationConfig {

	public VectorSolution[] createInitialSolutions(int count) {

		Map mapping = new HashMap();
		BinPackingProblem bp = new BinPackingProblem();
		bp.getResourcePolicies().putAll(getResourceConsumptionPolicies());
		for (Node n : getNodes()) {
			HardwareNode hn = new HardwareNode(n.getLabel(), n.getResources());
			bp.getBins().add(hn);
			mapping.put(hn, n);
		}
		for (Component c : getComponents()) {
			SoftwareComponent cn = new SoftwareComponent(c.getLabel(), c
					.getResources());
			bp.getItems().add(cn);
			mapping.put(c, cn);
		}

		List<Map<Object, List>> binsols = new ArrayList<Map<Object, List>>();
		for (int i = 0; i < count - 2; i++) {
			Map<Object, List> sol = (new RandomItemPacker(bp)).nextMapping();
			binsols.add(sol);
		}
		binsols.add((new FFDBinPacker(bp)).nextMapping());

		int stotal = Math.min(binsols.size(), count);
		VectorSolution[] sols = new VectorSolution[stotal];

		for (int i = 0; i < stotal; i++) {
			int[] pos = new int[getComponents().length];
			Map<Object, List> sol = binsols.get(i);

			for (int j = 0; j < getComponents().length; j++) {
				SoftwareComponent c = (SoftwareComponent) mapping
						.get(getComponents()[j]);
				HardwareNode host = (HardwareNode) sol.get(c).get(0);
				Node node = (Node) mapping.get(host);
				for (int k = 0; k < getNodes().length; k++) {
					if (getNodes()[k] == node) {
						pos[j] = k;
						break;
					}
				}
			}
			sols[i] = new VectorSolution(pos);
		}
		
		return sols;
	}

	
	public int scoreDeployment(DeploymentPlan plan) {
		boolean valid = true;

		ResourceResidual residual = new ResourceResidual(this);
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
							+ residual.getHostExhaustions().size() + residual
							.getDisconnections().size());
		}
	}

}
