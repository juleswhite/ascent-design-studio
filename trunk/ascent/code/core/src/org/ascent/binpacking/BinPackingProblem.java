 /**************************************************************************
 * Copyright 2008 Jules White                                              *
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ascent.ResourceConsumptionPolicy;

public class BinPackingProblem {

	private List<Bin> bins_ = new ArrayList<Bin>();
	private List<Item> items_ = new ArrayList<Item>();
	
	private Map<Object,ResourceConsumptionPolicy> resourcePolicies_ = new HashMap<Object,ResourceConsumptionPolicy>();

	public List<Bin> getBins() {
		return bins_;
	}

	public void setBins(List<Bin> bins) {
		bins_ = bins;
	}

	public List<Item> getItems() {
		return items_;
	}

	public void setItems(List<Item> items) {
		items_ = items;
	}

	public Map<Object, ResourceConsumptionPolicy> getResourcePolicies() {
		return resourcePolicies_;
	}

	public void setResourcePolicies(
			Map<Object, ResourceConsumptionPolicy> resourcePolicies) {
		resourcePolicies_ = resourcePolicies;
	}
	
}
