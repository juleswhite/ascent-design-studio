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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeastBoundMinExcPacker extends LeastBoundPacker {

	private class BinSorter implements Comparator {
		private Map<Object, Double> vals_;

		public BinSorter(Map<Object, Double> vals) {
			super();
			vals_ = vals;
		}

		public int compare(Object arg0, Object arg1) {
			return (int) Math.rint((100 * (vals_.get(arg1) - vals_.get(arg0))));
		}

	}

	public LeastBoundMinExcPacker() {
		super();
	}

	public LeastBoundMinExcPacker(BinPackingProblem p) {
		super(p);
	}

	public Object selectTarget(ItemState ss, List valid) {

		List exc = getExcluded(ss.getItem());
		if (exc.size() == 0) {

			for (Object t : valid) {
				BinState ts = getTargetState(t);
				if (willFit(ss, ts)) {
					return t;
				}
			}
		} else {
			List potentials = new ArrayList();
			for (Object t : valid) {
				BinState ts = getTargetState(t);
				if (willFit(ss, ts)) {
					potentials.add(t);
				}
			}
			if (potentials.size() > 0) {
				Map<Object, Double> vals = new HashMap<Object, Double>();
				for (Object t : potentials) {
					double v = exc.size();
					for (Object e : exc) {
						ItemState est = getSourceState(e);
						if(est.getValid() == null){
							v -= (1.0 / getTargets().size());
						}
						else if (est.getValid().contains(t)) {
							v -= (1.0 / est.getValid().size());
						}
						vals.put(t, v);
					}
				}
				Collections.sort(potentials, new BinSorter(vals));
				return potentials.get(0);
			}
		}
		return null;
	}

}
