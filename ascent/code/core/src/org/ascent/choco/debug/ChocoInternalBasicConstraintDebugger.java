package org.ascent.choco.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ascent.configurator.AbstractRefreshChocoCore;

import choco.AbstractProblem;
import choco.Constraint;
import choco.integer.IntDomainVar;
import choco.integer.IntVar;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class ChocoInternalBasicConstraintDebugger implements ChocoCoreInternalConstraintDebugger {

	private AbstractProblem problem_;

	private String description_;

	private String name_;

	private Constraint constraint_;

	private IntDomainVar errorBit_;

	private Object source_;
	
	private AbstractRefreshChocoCore core_;

	public ChocoInternalBasicConstraintDebugger(AbstractRefreshChocoCore core, AbstractProblem problem, Object source,
			String name, String description, Constraint constraint) {
		super();
		problem_ = problem;
		description_ = description;
		constraint_ = constraint;
		name_ = name;
		source_ = source;
		core_ = core;
	}

	public Object getSource() {
		return source_;
	}

	public void setSource(Object source) {
		source_ = source;
	}

	public Constraint getConstraint() {
		return constraint_;
	}

	public void setConstraint(Constraint constraint) {
		constraint_ = constraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.refresh.core.choco.debug.ConstraintDebugger#getErrorBit()
	 */
	public IntDomainVar getErrorBit() {
		errorBit_ = problem_.makeBoundIntVar(name_, 0, 1);
		return errorBit_;
	}

	public String getError() {
		return description_;
	}

	public String toString() {
		String vmsg = (errorBit_.getVal() == 1) ? " is violoated"
				: " is not violated";
		return description_ + vmsg;
	}

	public AbstractRefreshChocoCore getCore() {
		return core_;
	}

	public void setCore(AbstractRefreshChocoCore core) {
		core_ = core;
	}
	
	public List getSourceTargets(Object src){
		ArrayList result = new ArrayList();
		Map<Object,IntVar> ttable = core_.getTargetTable(src);
		for(Object o : ttable.keySet()){
			if(((IntDomainVar)ttable.get(o)).getVal() == 1){
				result.add(o);
			}
		}
		return result;
	}
}
