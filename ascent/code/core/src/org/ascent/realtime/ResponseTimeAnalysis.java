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

package org.ascent.realtime;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ascent.deployment.RealTimeTask;

public class ResponseTimeAnalysis {

	public static final Comparator<RealTimeTask> RTM_PRIORITY_SORTER = new Comparator<RealTimeTask>() {

		public int compare(RealTimeTask o1, RealTimeTask o2) {
			if (o1.getPeriod() < o2.getPeriod())
				return -1;
			else if (o2.getPeriod() < o1.getPeriod())
				return 1;
			else
				return 0;
		}

	};
	
	public boolean schedulable(List<RealTimeTask> tasks){
		Collections.sort(tasks,RTM_PRIORITY_SORTER);
		
		for(int i = 0; i < tasks.size(); i++){
			if(!schedulable(tasks.subList(0, i),tasks.get(i))){
				return false;
			}
		}
		return true;
	}

	public boolean schedulable(List<RealTimeTask> tasks, RealTimeTask ti) {
		return rt(tasks,ti) <= ti.getPeriod();
	}

	public double rt(List<RealTimeTask> tasks, RealTimeTask task) {
		double ci = task.getPeriod() * task.getUtilization();
		double rtprev = ci;
		double rt = -1;
		while(true){
			rt = rt(rtprev,tasks,ci);
			if(rt == rtprev)
				break;
			rtprev = rt;
		}
		return rt;
	}

	public double rt(double rtprev, List<RealTimeTask> tasks, double ci) {
		double rt = ci;

		for (int i = 0; i < tasks.size(); i++) {
			RealTimeTask hp = tasks.get(i);
			rt += Math.ceil(rtprev / hp.getPeriod())
					* (hp.getPeriod() * hp.getUtilization());

		}
		return rt;
	}
}
