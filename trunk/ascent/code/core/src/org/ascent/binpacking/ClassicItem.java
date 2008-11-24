/**************************************************************************
 * Copyright 2008 Brian Dougherty                                          *
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
package org.ascent.binpacking;

public class ClassicItem extends Element {
    double [] consumedResources_ ;
    
	public ClassicItem(int id, String name) {
		super(id, name);
	}
	
	public ClassicItem(int id, String name, double [] cr) {
		super(id, name);
		consumedResources_ = cr;
	}
	public double [] getConsumedResources_(){
		return consumedResources_;
	}

}
