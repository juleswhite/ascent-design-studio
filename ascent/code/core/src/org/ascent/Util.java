/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Util {

	public static List<List> permute(List s1, List s2){
		List result = new ArrayList<List>(s1.size() * s2.size());
		for(int i = 0; i < s1.size(); i++){
			Object o1 = s1.get(i);
			
			for(int j = 0; j < s2.size(); j++){
				Object o2 = s2.get(j);
				ArrayList item = new ArrayList();
				
				if(o1 instanceof Collection)
					item.addAll((Collection)o1);
				else
					item.add(o1);
			}
		}
		return result;
	}
	
	public static int random(int min, int max) {
		double rand = Math.random();
		double range = max - min;
		double delta = range * rand;
		return (int) Math.rint((min + delta));
	}
	
	public static double random(double min, double max) {
		double rand = Math.random();
		double range = max - min;
		double delta = range * rand;
		return min + delta;
	}
}
