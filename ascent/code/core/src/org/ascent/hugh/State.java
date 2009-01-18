/**************************************************************************
 * Copyright 2009 Brian Dougherty                                          *
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
package org.ascent.hugh;

import java.util.ArrayList;

public class State {
	
	private ArrayList<Integer> stateArray_;//Ordered List of chosen Components
	
	
	public State(){
		
	}
	
	public State(int numComps){
		initializeArray(numComps);
	}
	
	public State(ArrayList<Integer> stateArray){
		stateArray_ = stateArray;
	}
	
	public String toString(){
		String sa = "State is  " + stateArray_;
		return sa;
	}
	
	private void initializeArray(int size){
		stateArray_= new ArrayList(size);
		for(int i =0; i < size; i++){
			stateArray_.set(i, new Integer(-1));
		}
	}
	
}
