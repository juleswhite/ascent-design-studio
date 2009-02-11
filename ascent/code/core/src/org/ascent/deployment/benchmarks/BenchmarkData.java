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

public class BenchmarkData {

	private int numNodes_ = 0;
	private int bandwidthUsed_ = 0;
	private long time_ = 0;
	private String alg_ = "";
	private DeploymentConfig config_;
	
	
	public BenchmarkData(DeploymentConfig config){
		config_ = config;
	}
	
	public int getNumNodes() {
		return numNodes_;
	}



	public void setNumNodes(int numNodes) {
		numNodes_ = numNodes;
	}
	
	
	public int getBandwidthUsed() {
		return bandwidthUsed_;
	}

	public void setBandwidthUsed(int bandwidthUsed) {
		bandwidthUsed_ = bandwidthUsed;
	}

	public String getAlg() {
		return alg_;
	}

	public void setAlg(String alg) {
		alg_ = alg;
	}
	
	public int getComponents(){
		return config_.getComponents().length;
	}
	
	public int getInteractions(){
		return config_.getInteractions().length;
	}
	


	
	/**
	 * Chris, make sure and provide
	 * a nice implementation of this
	 * method.
	 */
	public String toString(){
		
		String output = "Benchmark Data for " + alg_ + "\n";
		output += "----------------------------------\n\n";
		output += "Bandwidth Used: " +  bandwidthUsed_ + "\n";
		output += "Nodes: " + numNodes_ + "\n";
		
		return output;
	}
}