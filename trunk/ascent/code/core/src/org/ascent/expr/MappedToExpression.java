package org.ascent.expr;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class MappedToExpression extends UnaryExpression {

	private Object source_;
	private Object target_;

	public MappedToExpression(Object source, Object target) {
		super();
		source_ = source;
		target_ = target;
	}

	public Object getSource() {
		return source_;
	}

	public void setSource(Object source) {
		source_ = source;
	}

	public Object getTarget() {
		return target_;
	}

	public void setTarget(Object target) {
		target_ = target;
	}

}
