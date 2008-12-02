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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.ascent.ReverseComparator;
import org.ascent.configurator.ProblemBuilderCore;
import org.ascent.configurator.RefreshCore;

public class MultiStrategyBinPacker {
	
	private FFDCore solver_;
	
	
	public MultiStrategyBinPacker() {
	}

	public Map<Object,List> pack(BinPackingProblem p){
		List<Map<Object,List>> sols = new ArrayList<Map<Object,List>>();
		FFDCore solver = new FFDCore(p);
		addSolution(sols, solver.nextMapping());
		
		solver = new FFDCore(p);
		solver.setItemSortingStrategy(new ReverseComparator(solver.getItemSortingStrategy()));
		addSolution(sols, solver.nextMapping());
		
		solver = new FFDCore(p);
		solver.setWeightingStrategy(new WeightUpdateStrategy() {
		
			public double getWeight(ItemState st) {
				double w = 0;
				for (int i = 0; i < st.getSize().length; i++) {
					w += st.getSize()[i] * st.getSize()[i];
				}
				return Math.sqrt(w);
			}
		});
		addSolution(sols, solver.nextMapping());
		
		solver = new FFDCore(p);
		solver_ = solver;
		solver.setWeightingStrategy(new WeightUpdateStrategy() {
		
			public double getWeight(ItemState st) {
				double w = 0;
				for (int i = 0; i < st.getSize().length; i++) {
					w += st.getSize()[i] * st.getSize()[i];
				}
				return Math.sqrt(w) + (1000 * solver_.getExcluded(Arrays.asList(new Object[]{st.getItem()})).size());
			}
		});
		
		addSolution(sols, solver.nextMapping());
		
		solver = new FFDCore(p);
		solver_ = solver;
		solver.setBinSortingStrategy(new ReverseComparator(solver.getBinSortingStrategy()));
		solver.setWeightingStrategy(new WeightUpdateStrategy() {
		
			public double getWeight(ItemState st) {
				double w = 0;
				for (int i = 0; i < st.getSize().length; i++) {
					w += st.getSize()[i] * st.getSize()[i];
				}
				return Math.sqrt(w) + (1000 * solver_.getExcluded(Arrays.asList(new Object[]{st.getItem()})).size());
			}
		});
		addSolution(sols, solver.nextMapping());
		
		solver = new LeastBoundPacker(p);
		addSolution(sols, solver.nextMapping());
		
		solver = new FFDCore(p);
		solver.setBinSortingStrategy(new ReverseComparator(solver.getBinSortingStrategy()));
		addSolution(sols, solver.nextMapping());
		
		return sols.get(0);
	}
	
	protected void addSolution(List<Map<Object,List>> sols, Map<Object,List> s){
		if(s != null){
			sols.add(s);
		}
	}
}
