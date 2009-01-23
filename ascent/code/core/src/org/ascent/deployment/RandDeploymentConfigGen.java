 /**************************************************************************
 * Copyright 2009 Brian Dougherty                                             *
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
import java.util.Random;
public class RandDeploymentConfigGen{
	private int numComponents_;//check
	private int numNodes_;//check
	private int numResources_ ;//check
	private int numExConstraints_; //check
	private int numReqConstraints_;//check
	private int numTasksPerComponent_;//seeMakeComponents
	private int numNetworks_;
	private int numComponentInteractions_;
	private double minResConsumption_;//check
	private double maxResConsumption_; //checkmaxvalue not inclusive
	private double minCompUtilization_; //see makeComponents;
	private double maxCompUtilization_;
	private double minAvailResValue_;//check
	private double maxAvailResValue_;//check
	private double minBwidthPerInteraction_;
	private double maxBwidthPerInteraction_;
	private double minInteractionPeriod_;// see makeComponents
	private double maxInteractionPeriod_;
	private double minNetworkBandwidth_;
	private double maxNetworkBandwidth_;
	private NetworkLink[] networks_;
	private Component[] components_;
	private Node[] nodes_;
	private Interaction[] interactions_;
	private List<DeploymentConstraint> constraints_ = new ArrayList<DeploymentConstraint>();
	public RandDeploymentConfigGen(){}
	
	public RandDeploymentConfigGen(int numComponents, int numNodes, int numResources, int numExConstraints, int numReqConstraints,
			int numTasksPerComponent, int numNetworks, int numComponentInteractions,double minResConsumption,
			double maxResConsumption, double minCompUtilization, double maxCompUtilization,
			double minAvailResValue, double maxAvailResValue, double minBwidthPerInteraction, 
			double maxBwidthPerInteraction, double minInteractionPeriod, double maxInteractionPeriod,
			double minNetworkBandwidth, double maxNetworkBandwidth){
		
		numComponents_=numComponents;
		numNodes_=numNodes;
		numResources_=numResources;
		numExConstraints_=numExConstraints;
		numReqConstraints_=numReqConstraints;
		numTasksPerComponent_=numTasksPerComponent;
		numNetworks_=numNetworks;
		numComponentInteractions_=numComponentInteractions;
		minResConsumption_=minResConsumption;
		maxResConsumption_=maxResConsumption;
		minCompUtilization_=minCompUtilization;
		maxCompUtilization_=maxCompUtilization;
		minAvailResValue_=minAvailResValue;
		maxAvailResValue_=maxAvailResValue;
		minBwidthPerInteraction_=minBwidthPerInteraction;
		maxBwidthPerInteraction_=maxBwidthPerInteraction;
		minInteractionPeriod_=minInteractionPeriod;
		maxInteractionPeriod_=maxInteractionPeriod;
		minNetworkBandwidth_=minNetworkBandwidth;
		maxNetworkBandwidth_=maxNetworkBandwidth;
		
		
	}
	
	public void makeComponents(){
		Random r = new Random();
		double availFraction = 1 - (minResConsumption_/maxResConsumption_);
		double availDifference = availFraction * maxResConsumption_;
		int intDifference = (int)Math.round(availDifference);
		components_ = new Component [numComponents_];
		

		for(int i =0; i < numComponents_; i++){
			int [] resources = new int[numResources_];
			for(int j = 0; j <numResources_; j++){
				
				 int intRandomRes =  r.nextInt(intDifference) + (int)Math.round(minResConsumption_);//() *
				
				 if(intRandomRes >maxResConsumption_ ){
					 intRandomRes = (int) Math.round(maxResConsumption_);
					 if(intRandomRes >maxResConsumption_){
						 intRandomRes--;
					 }
				 }
				resources[j] = intRandomRes;
				
			}
			String componentId = "Component " + i;
			Component newComponent = new Component(i, componentId, resources);
			double availCU= 1 - (minCompUtilization_/maxCompUtilization_);
			double avD = availCU * maxCompUtilization_;
			int intavD = (int) Math.round(avD); // available utilization ;
			int RandomRes = r.nextInt(intavD) +(int) Math.round(minCompUtilization_);
			double availPer;
			double avDPer;
			int intavDPer;
			double RandomPer;
			for(int j = 0; j < numTasksPerComponent_-1; j++){
				
				double util = Math.random();
				
				if(util > .01){
					util = util - .01;
				}
				else{
					util = util + .01;
				}
				util = util * RandomRes;
				
				availPer= 1 - (minInteractionPeriod_/maxInteractionPeriod_);
				avDPer = availPer * maxInteractionPeriod_;
				intavDPer = (int) Math.round(avDPer); // available utilization ;
				RandomPer = r.nextInt(intavD) +(int) Math.round(maxInteractionPeriod_);
				newComponent.addTask(RandomPer, RandomRes);
				RandomRes -= util;
			}
			
			availPer= 1 - (minInteractionPeriod_/maxInteractionPeriod_);
			avDPer = availPer * maxInteractionPeriod_;
			intavDPer = (int) Math.round(avDPer); // available utilization ;
			RandomPer = r.nextInt(intavD) +(int) Math.round(maxInteractionPeriod_);
			newComponent.addTask(RandomPer, RandomRes);

			System.out.println("Component is " + newComponent);
			components_[i] = newComponent;
		}
	}
	
	public void makeNodes(){
		Random r = new Random();
		double availFraction = 1 - (minAvailResValue_/maxAvailResValue_);
		double availDifference = availFraction * maxAvailResValue_;
		int intDifference = (int)Math.round(availDifference);
		nodes_ = new Node [numNodes_];

		for(int i =0; i < numNodes_; i++){
			int [] resources = new int[numResources_];
			for(int j = 0; j <numResources_; j++){
				
				 int intRandomRes =  r.nextInt(intDifference) + (int)Math.round(minResConsumption_);;//() *
				 
				 if(intRandomRes >maxAvailResValue_ ){
					 intRandomRes = (int) Math.round(maxAvailResValue_);
					 if(intRandomRes >maxAvailResValue_ ){
						 intRandomRes--;
					 }
				 }
				resources[j] = intRandomRes;
				
			}
			String nodeId = "Node " + i;
			Node newNode = new Node(i, nodeId, resources);
			System.out.println("Node is " + newNode);
			nodes_[i] = newNode;
		}
		
	}
	
	public void addExConstraints(){
		int constraintsAdded = 0;
		int safetyCounter = 0;
		Random r = new Random();
		while(constraintsAdded < numExConstraints_){
			double chance = Math.random();
			for(Component c : components_){
				if( chance <=.2){ // we want to spread the constraints randomly
					int compInd = r.nextInt(numComponents_); //randomly pick the component to add.
					
					if(compInd != c.getId()){
						Colocated checkCo = new Colocated(c,components_[compInd]);
						if(!(constraints_.contains(checkCo))){
							requireNotColocated(c,components_[compInd]);
							constraintsAdded++; // num Colocated constraints added
						}
					}
					
					
				}
				if(constraintsAdded == numExConstraints_){
					break;
				}
			}
			safetyCounter++;
			if(safetyCounter == Integer.MAX_VALUE){
				System.out.println("Could not add all Constraints. See addExConstraints method.");
				return;
			}
		}
	}
	
	public void addReqConstraints(){
		int constraintsAdded = 0;
		int safetyCounter = 0;
		Random r = new Random();
		while(constraintsAdded < numReqConstraints_){
			double chance = Math.random();
			for(Component c : components_){
				if( chance <=.2){ // we want to spread the constraints randomly
					int compInd = r.nextInt(numComponents_); //randomly pick the component to add.
					
					if(compInd != c.getId()){
						NotColocated checkNotCo = new NotColocated(c,components_[compInd]);
						if(!(constraints_.contains(checkNotCo))){
							requireColocated(c,components_[compInd]);
							constraintsAdded++; // num Colocated constraints added
						}
					}
					
					
				}
				if(constraintsAdded == numExConstraints_){
					break;
				}
			}
			safetyCounter++;
			if(safetyCounter == Integer.MAX_VALUE){
				System.out.println("Could not add all Constraints. See addColoConstraints method.");
				return;
			}
		}
	}
	
	
	public void makeInteractions(){
	
	}

	public void makeNetworks(){
		
	}
	
	public static void main(String args[]){
		RandDeploymentConfigGen rdcg = new RandDeploymentConfigGen();
		rdcg.makeComponents();
		
	}
	public void requireNotColocated(Component a, Component b) {
		getConstraints().add(new NotColocated(a, b));
	}

	public void requireColocated(Component a, Component b) {
		getConstraints().add(new Colocated(a, b));
	}
	public List<DeploymentConstraint> getConstraints() {
		return constraints_;
	}
}

