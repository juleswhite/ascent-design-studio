/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.probe;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TargetFileProbe implements TargetFinder {

	private Target node_;

	public TargetFileProbe(String nodeprops) {
		node_ = Target.loadFrom(nodeprops);
	}

	public TargetFileProbe(Class c, String id) {
		try {
			node_ = Target.loadFrom(c, id);
		} catch (Exception e) {

		}
	}

	public List<Target> getNodes() {
		LinkedList<Target> lc = new LinkedList<Target>();
		lc.add(node_);
		return lc;
	}

	public Map getTargetProperties(Object target) {
		Target n = (Target) target;
		return n.getResources();
	}

	public List getTargets() {
		return getNodes();
	}

	public void run() {
	}

}
