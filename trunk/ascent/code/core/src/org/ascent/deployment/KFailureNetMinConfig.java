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

package org.ascent.deployment;

import java.util.ArrayList;

import org.ascent.VectorSolution;

public class KFailureNetMinConfig extends NetMinConfig {
	
	private int failures_;

	public KFailureNetMinConfig(Node[] nodes, NetworkLink[] networks,
			Component[] components, Interaction[] interactions, int failures) {
		super(nodes, networks, components, interactions);
		
		failures_ = failures;

		// Make the components array big enough
		if (failures != 0) {

			ArrayList<Component> temp = new ArrayList<Component>();
			// Iterate through the components and make duplicates
			for (Component c : components) {
				temp.add(c);
				ArrayList<Component> curComps = new ArrayList<Component>();

				for (int i = 0; i < failures; ++i) {
					Component newComponent = new Component(c);
					
					super.getConstraints().add(
							new NotColocated(c, newComponent));
					temp.add(newComponent);
					curComps.add(newComponent);
				}

				for (Component c2 : curComps) {
					for (int i = 0; i < curComps.size(); ++i) {
						if (c2 != curComps.get(i)) {
							super.getConstraints().add(
									new NotColocated(c2, curComps.get(i)));
						}
					}
				}
			}

			Component[] newArray = new Component[temp.size()];

			for (int i = 0; i < temp.size(); ++i) {
				newArray[i] = temp.get(i);
			}
			
			for (int i = 0; i < newArray.length; ++i){
				newArray[i].setId(i);
			}
			
			init();
			super.setComponents(newArray);
			init();

		}

	}
	
	public int[][] getPositionBoundaries(){
		int[][] bounds = super.getPositionBoundaries();
		int[][] b2 = new int[bounds.length+1][2];
		for(int i = 0; i < bounds.length; i++){
		       b2[i+1] = bounds[i];
		}
	
		//The new addition to the bounds array is the
		//min and max for the number of nodes.
		
		b2[0] = new int[] {failures_+1, (failures_+1)*super.getComponents().length};
		
		return b2;
	}
	
	
	@Override
	public VectorSolution[] createInitialSolutions(int count){
		super.createInitialSolutions(count);
		return null;
	}
	
	public DeploymentPlan getDeploymentPlan(VectorSolution vs){
		
		
		return null;
		
	}
	
}
