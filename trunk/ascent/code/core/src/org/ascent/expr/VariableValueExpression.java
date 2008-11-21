package org.ascent.expr;
/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class VariableValueExpression extends UnaryExpression {
	private Object variable_;

	
	public VariableValueExpression(Object variable) {
		super();
		variable_ = variable;
	}

	/**
	 * @return the variable
	 */
	public Object getVariable() {
		return variable_;
	}

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(Object variable) {
		variable_ = variable;
	}
	
}
