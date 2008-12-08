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



public class Component extends ModelElement implements Schedulable{
	private Interaction[] interactions_;
	private List<RealTimeTask> realTimeTasks_ = new ArrayList<RealTimeTask>();
	
	public Component(int id, String label, int[] resources) {
		super(id, label, resources);
	}

	public Interaction[] getInteractions() {
		return interactions_;
	}

	public void setInteractions(Interaction[] interactions) {
		interactions_ = interactions;
	}

	public int getTotalTasks() {
		if(realTimeTasks_.size() > 0)
			return realTimeTasks_.size();
		else
			return 1;
	}
	
	public void addTask(double period, double util){
		realTimeTasks_.add(new RealTimeTask(period,util));
	}
	
}
