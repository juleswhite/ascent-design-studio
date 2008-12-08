package org.ascent.deployment;


public class NetworkBandwidthMinimizingPlanner extends
		DeploymentWithNetworkMinimizationConfig {

	
	public int scoreDeployment(DeploymentPlan plan) {
		if (plan.isValid()) {
			int score = 0;
			for (Interaction i : interactions_) {
				if (plan.getChannel(i) instanceof LocalHostLink) {
					score += (i.getResources()[0] * i.getRate());
				}
			}
			return score;
		} else {
			ResourceResidual residual = new ResourceResidual(this);
			residual.deploy(plan);
			return -1
					* (residual.getLinkExhaustions().size()
							+ residual.getHostExhaustions().size() + residual
							.getDisconnections().size());
		}
	}

}
