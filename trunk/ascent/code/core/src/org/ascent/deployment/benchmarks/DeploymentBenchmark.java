 /**************************************************************************
 * Copyright 2009 Jules White                                              *
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


package org.ascent.deployment.benchmarks;

import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.DeploymentPlan;
import org.ascent.deployment.DeploymentPlanner;

public class DeploymentBenchmark {

	protected DeploymentConfig config_;
	/**
	 * This method takes a DeploymentPlanner as input and
	 * runs a suite of deployment benchmarks on it. The
	 * results are returned in a BenchmarkData object. 
	 * 
	 * @param planner
	 * @return
	 */
	public BenchmarkData test(DeploymentPlanner planner){
		long start = System.currentTimeMillis();
		DeploymentPlan plan = planner.deploy(config_);
		long finish = System.currentTimeMillis();
		
		BenchmarkData data = new BenchmarkData();
		data.setAlg(planner.getClass().getName());
		//data.setBandwidthUsed(plan.getSolution());
		
		
		return null;
	}
	
	public DeploymentBenchmark (DeploymentConfig conf){
		config_ = conf;
	}
}
