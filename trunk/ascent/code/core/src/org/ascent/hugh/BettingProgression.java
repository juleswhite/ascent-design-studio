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

import com.sun.source.tree.BinaryTree;

public class BettingProgression {
	String name_;
	ArrayList<Double> multipliers_;
	//BinaryTree multiTree_ = new BinaryTree();
	int currentIndex_;
	public BettingProgression(){
		
	}
	
	public BettingProgression(String name, ArrayList<Double> multipliers ){
		name_ = name;
		multipliers_ = multipliers;
	}
	
	public double getWin(){
		if (currentIndex_ %2 == 0 || currentIndex_ ==1){ // First step in progression, or we've been winning
			if( ((currentIndex_*2) + 1) <= multipliers_.size()){
				Double wm=  multipliers_.get(((currentIndex_*2) + 1));
				currentIndex_ = (currentIndex_*2) + 1;
				return wm.doubleValue();
			}
			else{
				Double wm=  multipliers_.get(currentIndex_);
				return wm.doubleValue();
			}
		}
		else{ // been losing
			currentIndex_ = 1;
			return multipliers_.get(currentIndex_).doubleValue();
		}
	}
	
	public double getLoss(){
		if (currentIndex_ %2 == 1 || currentIndex_ ==1){ // First step in progression, or we've been winning
			if( ((currentIndex_*2)) <= multipliers_.size()){
				Double wm=  multipliers_.get(((currentIndex_*2)));
				currentIndex_ = (currentIndex_*2);
				return wm.doubleValue();
			}
			else{
				Double wm=  multipliers_.get(currentIndex_);
				return wm.doubleValue();
			}
		}
		else{ // been winning
			currentIndex_ = 1;
			return multipliers_.get(currentIndex_).doubleValue();
		}
	}
}
