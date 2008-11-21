package org.ascent.configurator;

import org.ascent.expr.BinaryExpression;
import org.ascent.expr.Expression;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class Constraint {
	private BinaryExpression expression_;
	
	public Constraint(BinaryExpression expression) {
		super();
		expression_ = expression;
	}

	
	/**
	 * @param expression the expression to set
	 */
	public void setExpression(BinaryExpression expression) {
		expression_ = expression;
	}

	public Expression getExpression(){
		return expression_;
	}
}
