package org.ascent.configurator.conf.debug;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class Conflict {
	private Object source_;
	private ConstraintReference constraint_;
	private String explanation_;

	public Conflict(Object source, String explanation) {
		super();
		source_ = source;
		explanation_ = explanation;
	}

	public Conflict(Object source, ConstraintReference constraint,
			String explanation) {
		super();
		source_ = source;
		constraint_ = constraint;
		explanation_ = explanation;
	}

	public Object getSource() {
		return source_;
	}

	public void setSource(Object source) {
		source_ = source;
	}

	public String getExplanation() {
		return explanation_;
	}

	public void setExplanation(String explanation) {
		explanation_ = explanation;
	}

	public ConstraintReference getConstraint() {
		return constraint_;
	}

	public void setConstraint(ConstraintReference constraint) {
		constraint_ = constraint;
	}

	public String toString(){
		return "Unable to map:"+getSource()+" reason:[\n  "+getExplanation().trim()+"\n]";
	}
}
