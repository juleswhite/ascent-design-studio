package org.ascent.configurator.conf.debug;

import java.util.ArrayList;
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
public class RefreshDebugger {

	private RefreshCore core_;
	private RefreshProblem problem_;
	private DebuggerFactory debuggerFactory_;
	
	public RefreshDebugger(RefreshCore core, RefreshProblem problem) {
		super();
		core_ = core;
		problem_ = problem;
		debuggerFactory_ = problem;
	}

	public RefreshCore getCore() {
		return core_;
	}

	public void setCore(RefreshCore core) {
		core_ = core;
	}

	public RefreshProblem getProblem() {
		return problem_;
	}

	public void setProblem(RefreshProblem problem) {
		problem_ = problem;
	}
	
	public List<Conflict> debug(List<ConstraintReference> refs){
		ArrayList<Conflict> conflicts = new ArrayList<Conflict>(refs.size());
		for(ConstraintReference ref : refs){
			ProblemDebugger d = debuggerFactory_.createDebugger(ref);
			if(d instanceof SourceConstraintDebugger){
				SourceConstraintDebugger sd = (SourceConstraintDebugger)d;
				conflicts.add(new Conflict(sd.getSource(),ref,sd.getError()));
			}
		}
		return conflicts;
	}
	
}
