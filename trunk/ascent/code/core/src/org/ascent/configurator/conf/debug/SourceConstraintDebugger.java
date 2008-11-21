package org.ascent.configurator.conf.debug;

import java.util.List;

import org.ascent.configurator.RefreshCore;
import org.ascent.configurator.conf.RefreshProblem;

/******************************************************************************
 * Copyright (c) 2007 Jules White.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jules White - initial API and implementation 
 ****************************************************************************/
public abstract class SourceConstraintDebugger extends ProblemDebugger {
	
	private Object source_;

	public SourceConstraintDebugger(RefreshCore core, RefreshProblem problem, Object src) {
		super(core, problem);
		source_ = src;
	}

	public Object getSource() {
		return source_;
	}

	public void setSource(Object source) {
		source_ = source;
	}
	
	public List getSourceTargets(){
		return getSourceTargets(source_);
	}
	
	public List getSourceTargets(Object src){
		return getCore().getSourceTargets(src);
	}
}
