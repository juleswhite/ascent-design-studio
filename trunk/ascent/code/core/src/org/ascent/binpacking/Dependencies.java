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
import java.util.Iterator;
import java.util.List;

public class Dependencies implements Iterable<Dependency> {
	private List<Dependency> dependencies_ = new ArrayList<Dependency>();

	public void update(Object target) {
		for (Dependency d : dependencies_) {
			d.update(target);
		}
	}

	public boolean add(Dependency arg0) {
		return dependencies_.add(arg0);
	}

	public boolean remove(Object arg0) {
		return dependencies_.remove(arg0);
	}

	public Iterator<Dependency> iterator() {
		return dependencies_.iterator();
	}

	public int size() {
		return dependencies_.size();
	}

}

