 /**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
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
package org.ascent.deployment.test;


import org.ascent.deployment.BandwidthMinimizingPSODeploymentPlanner;
import org.ascent.deployment.Component;
import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.NetworkBandwidthMinimizingPlanner;
import org.ascent.deployment.Node;
import org.ascent.deployment.benchmarks.BenchmarkData;
import org.ascent.deployment.benchmarks.DeploymentBenchmark;
import org.ascent.deployment.RandDeploymentConfigGen;

import junit.framework.TestCase;

public class DeploymentBenchmarkTest extends TestCase {
	
	
	/**
	 * This method will run a benchmark on a given deployment.
	 * 
	 * In order to run the deployment benchmark, you must first
	 * supply it with a deployment configuration.  You then
	 * call the "test" method and supply the planner.
	 */
	public void testDeployment(){

		BandwidthMinimizingPSODeploymentPlanner planner = new BandwidthMinimizingPSODeploymentPlanner();
		DeploymentConfig conf = new DeploymentConfig();
		
		Node n1 = conf.addNode("n1", new int[0]);
		Node n2 = conf.addNode("n2", new int[0]);
		Node n3 = conf.addNode("n3", new int[0]);
		Component c1 = conf.addComponent("c1", new int[0]);
		Component c2 = conf.addComponent("c2", new int[0]);
		Component c3 = conf.addComponent("c3", new int[0]);
		conf.addInteraction("c1-->c2", new int[]{20}, 1, new Component[]{c1,c2});
		conf.addInteraction("c1-->c3", new int[]{30}, 1, new Component[]{c1,c3});
		conf.addInteraction("c1-->c1", new int[]{20}, 1, new Component[]{c1,c1});
		conf.addNetwork("n1", new Node[] {n1, n2, n3}, new int[]{Integer.MAX_VALUE});

		
		DeploymentBenchmark dbm = new DeploymentBenchmark(conf);
		System.out.println(dbm.test(planner).toString());
		
		
		NetworkBandwidthMinimizingPlanner pl2 = new NetworkBandwidthMinimizingPlanner();
		
		
	}
	
	public void testDeploymentBenchmarks(){
		//We'll see if this works
		
		
		
		//Let's run 10 of these
		BenchmarkData bd[] = new BenchmarkData[10];
		BandwidthMinimizingPSODeploymentPlanner planner = new BandwidthMinimizingPSODeploymentPlanner();
		
		for (int i = 0; i < 10; ++i){
			RandDeploymentConfigGen rdcg = new RandDeploymentConfigGen(10,5,0,0,3,4,2,3,5,20,14,30,10,40,3,10,4,14,5,12,1,4,2,8);
			DeploymentBenchmark db = new DeploymentBenchmark(rdcg.makeDeploymentConfig());
			bd[i] = db.test(planner);
			
		}
		
		//Write the results
		DeploymentBenchmark.writeToFile(bd);
		
	}

}
