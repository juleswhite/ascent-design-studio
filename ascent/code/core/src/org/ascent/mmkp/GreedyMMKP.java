/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.mmkp;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.ascent.binpacking.ValueFunction;

public class GreedyMMKP extends MMKP {
	int index_ = 0;

	public GreedyMMKP(MMKPProblem problem) {
		super(problem);

	}

	public void init(ValueFunction<Collection> goal) {
		super.init(goal);
		for (MMKPSet s : getSets())
			Collections.sort(s.getItems(), NETVALUE_COMPARATOR);
		Collections.sort(getSets(), Collections.reverseOrder(SETS_BY_NETVALUE_COMPARATOR));
	}

	public Item choosePermuationItem() {
		int start = index_;
		Item pitem = null;
		for (int i = start; i < getSets().size(); i++) {
			log("i:" + i);
			index_ = i + 1;
			MMKPSet set = getSets().get(i);
			for (int j = set.getItems().size() - 1; j > -1; j--) {
				Item item = set.getItems().get(j);

				if (item.getSet().getItems().size() == 1
						|| item.getSet().getCurrentItem() != item) {
					log(" --seeing if " + item + " can be added");
					boolean valid = true;
					// for(Object res : resources.keySet()){
					for (int k = 0; k < getResources().length; k++) {
						valid = ((getCurrentResources()[k]) >= (getConsumedResources()[item
								.getIndex()][k]));
						if (!valid) {
							break;
						}
					}
					if (valid) {
						pitem = item;
						break;
					}
				}
			}
			if (pitem != null)
				break;
		}
		return pitem;
	}

	public boolean done() {
		return false;
	}

	public static void main(String[] args) {
//		for(int i = 10; i <= 100; i += 10){
		int i = 10;
		MMKPProblem p = MMKPProblem.genWithOpt(i, 20, 60, 2, 50, 250, 5, 50,
				5, 50);
//		System.out.println(p);
		GreedyMMKP solver = new GreedyMMKP(p);
		List<Item> solution = solver.solve(DEFAULT_GOAL);

		
		System.out.println("Sets:"+i+" Opt:" + ((solver.getCurrentValue()/(i * 50))*100)+"%");
//		}
	}
}