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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.ascent.deployment.BandwidthMinimizingPSODeploymentPlanner;
import org.ascent.deployment.Component;
import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.DeploymentPlan;
import org.ascent.deployment.DeploymentPlanner;
import org.ascent.deployment.Interaction;
import org.ascent.deployment.LocalHostLink;
import org.ascent.deployment.Node;
import org.ascent.deployment.ResourceResidual;

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
		
		//Jakarta Monitoring library profiling
		
		BenchmarkData data = new BenchmarkData();
		data.setAlg(planner.getClass().getName());
		
		int score = 0;
		
		//There is a bug in the following loop for the rate calculation
        for (Interaction i : plan.getDeploymentConfiguration().getInteractions()) {
                if (plan.getChannel(i) instanceof LocalHostLink) {
                        score += (i.getResources()[0] * i.getRate());
                }
        }
        
        data.setBandwidthUsed(score);
        int nodesFree = 0;
        ResourceResidual resid = new ResourceResidual(config_);
        
        resid.deploy(plan);
        for (Node n : plan.getDeploymentConfiguration().getNodes()) {
			if (resid.getHostedCount()[n.getId()] == 0) {
				++nodesFree;
			}
        }
        
        data.setNumNodes(plan.getDeploymentConfiguration().getNodes().length - nodesFree);
		
		
		return data;
	}
	
	public DeploymentBenchmark (DeploymentConfig conf){
		config_ = conf;
	}
	
	public static void writeToFile(BenchmarkData[] data){
		File file = new File("data.csv");
		
		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("Algorithm, Nodes, Bandwidth");
			bw.newLine();
			
			for (int i = 0; i < data.length; ++i){
				bw.write(data[i].getAlg() + ", " + data[i].getNumNodes() + ", " + data[i].getBandwidthUsed());
				bw.newLine();
			}
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
