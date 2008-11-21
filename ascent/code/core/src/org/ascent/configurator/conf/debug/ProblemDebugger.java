package org.ascent.configurator.conf.debug;

import org.ascent.configurator.RefreshCore;
import org.ascent.configurator.conf.RefreshProblem;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public abstract class ProblemDebugger {

	private RefreshCore core_;
	private RefreshProblem problem_;

	public ProblemDebugger(RefreshCore core, RefreshProblem problem) {
		super();
		core_ = core;
		problem_ = problem;
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

	public abstract String getError();
}
