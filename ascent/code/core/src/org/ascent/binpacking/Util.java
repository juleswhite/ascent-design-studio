/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.binpacking;

import java.util.ArrayList;
import java.util.List;

public class Util {

	public static void collectRequired(Dependencies deps, List all,
			DependencyManager m) {
		for (Dependency d : deps) {
			if (d instanceof Requires) {
				List refs = d.getDependencies();
				for (Object o : refs) {
					if (!all.contains(o)) {
						Dependencies odeps = m.getDependencies(o);
						collectRequired(odeps, all, m);
						all.add(o);
					}
				}
			}
		}
	}

	public static void collectExcluded(Object src, List excluded,
			DependencyManager m) {
		List required = new ArrayList();
		required.add(src);
		collectRequired(m.getDependencies(src), required, m);
		collectExcluded(required, excluded, m);
	}

	public static void collectExcluded(List srcs, List excluded,
			DependencyManager m) {
		for (Object src : srcs) {

			Dependencies deps = m.getDependencies(src);
			for (Dependency d : deps) {
				if (d instanceof Excludes) {
					List refs = d.getDependencies();
					for (Object o : refs) {
						if (!excluded.contains(o)) {
							excluded.add(o);
						}
					}
				}
			}
		}
	}
	
	public static boolean willFit(ItemState ss, BinState ts) {
		for (int i = 0; i < ss.getSizeWithDependencies().length; i++)
			if (ss.getSizeWithDependencies()[i] > ts.getSize()[i])
				return false;

		return true;
	}

	public static void insert(ItemState ss, BinState ts) {
		for (int i = 0; i < ss.getSize().length; i++)
			ts.getSize()[i] -= ss.getSize()[i];
	}

	public static void remove(ItemState ss, BinState ts) {
		for (int i = 0; i < ss.getSize().length; i++)
			ts.getSize()[i] += ss.getSize()[i];
	}
}
