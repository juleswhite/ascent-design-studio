package org.ascent.binpacking;

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
public class Requires extends
		Dependency {

	public Requires(StateProvider sp, Object source,
			List depends) {
		super(sp, source, depends);
	}

	public Requires(StateProvider sp, Object source,
			Object dependency) {
		super(sp, source, dependency);
	}

	@Override
	public String getType() {
		return "Requires";
	}

	@Override
	public void propogate(Object depend, Object sourcetarget) {
		ItemState state = getStateProvider().getSourceState(
				depend);
		state.requireTarget(sourcetarget);
	}

	@Override
	public void apply(RefreshCore core) {
		for(Object st : getDependencies()){
			core.addRequiresMappingConstraint(getSource(), st);
		}
	}
	
	

}
