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


package org.ascent.deployment;

import java.util.ArrayList;
import java.util.List;

import org.ascent.binpacking.BinSelector;
import org.ascent.binpacking.BinState;
import org.ascent.binpacking.FFDBinPacker;
import org.ascent.binpacking.ItemState;

public abstract class DeploymentStrategy implements BinSelector {

	public Object selectBin(FFDBinPacker core, Object item, List validbins,
			ItemState itemstate, List<BinState> binstates) {
		List<HardwareNode> nodes = new ArrayList<HardwareNode>();
		for(Object n : validbins)
			nodes.add((HardwareNode)n);
		
		return selectNode(core,(SoftwareComponent)item,nodes,itemstate,binstates);
	}

	public abstract HardwareNode selectNode(FFDBinPacker core, SoftwareComponent item, List<HardwareNode> validbins,
			ItemState itemstate, List<BinState> binstates);
}