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

public class BenchmarkData {

	private int numNodes_;
	private int bandwidthUsed_;
	private long time_;
	private String alg_;
	
	
	public BenchmarkData(){
		
	}
	
	public int getNumNodes() {
		return numNodes_;
	}



	public void setNumNodes(int numNodes) {
		this.numNodes_ = numNodes;
	}
	
	
	public int getBandwidthUsed() {
		return bandwidthUsed_;
	}

	public void setBandwidthUsed(int bandwidthUsed) {
		this.bandwidthUsed_ = bandwidthUsed;
	}

	public String getAlg() {
		return alg_;
	}

	public void setAlg(String alg) {
		this.alg_ = alg;
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
