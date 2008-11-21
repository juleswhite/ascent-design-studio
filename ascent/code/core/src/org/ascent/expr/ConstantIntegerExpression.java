package org.ascent.expr;
/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class ConstantIntegerExpression extends UnaryExpression {
	private int value_;

	public ConstantIntegerExpression(int value) {
		super();
		value_ = value;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value_;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		value_ = value;
	}
	
	
}
