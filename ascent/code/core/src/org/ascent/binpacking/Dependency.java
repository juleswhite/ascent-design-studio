package org.ascent.binpacking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ascent.configurator.RefreshCore;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public abstract class Dependency {
	private StateProvider stateProvider_;
	private Object source_;
	private List dependencies_ = new ArrayList();

	public Dependency(StateProvider sp) {
		stateProvider_ = sp;
	}

	public Dependency(StateProvider sp, Object source) {
		stateProvider_ = sp;
		source_ = source;
	}

	public Dependency(StateProvider sp, Object source,
			List dependencies) {
		stateProvider_ = sp;
		source_ = source;
		dependencies_ = dependencies;
	}
	
	public Dependency(StateProvider sp, Object source,
			Object dependency) {
		stateProvider_ = sp;
		source_ = source;
		dependencies_ = Arrays.asList(dependency);
	}

	public void update(Object target) {
		for (Object item : dependencies_) {
			propogate(item, target);
		}
	}

	public Object getSource() {
		return source_;
	}

	public void setSource(Object source) {
		source_ = source;
	}

	public List getDependencies() {
		return dependencies_;
	}

	public void setDependencies(List dependencies) {
		dependencies_ = dependencies;
	}

	public String toString() {
		String val = source_ + " " + getType() + " [";
		for (Object item : dependencies_) {
			val += item + ",";
		}
		if (val.endsWith(","))
			val = val.substring(0, val.length() - 1);
		val += "]";
		return val;
	}

	public StateProvider getStateProvider() {
		return stateProvider_;
	}

	public void setStateProvider(StateProvider stateProvider) {
		stateProvider_ = stateProvider;
	}

	public abstract String getType();

	public abstract void propogate(Object depend, Object sourcetarget);

	public abstract void apply(RefreshCore core);
}
