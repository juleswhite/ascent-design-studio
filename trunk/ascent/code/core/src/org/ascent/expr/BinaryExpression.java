package org.ascent.expr;
/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class BinaryExpression extends Expression{
	private Expression rightHandSide_;
	private Expression leftHandSide_;
	private String operation_;
	
	
	public BinaryExpression(Expression lhs, Expression rightHandSide, String operation) {
		super();
		rightHandSide_ = rightHandSide;
		operation_ = operation;
		leftHandSide_ = lhs;
	}
	public Expression getRightHandSide(){
		return rightHandSide_;
	}
	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation_;
	}
	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		operation_ = operation;
	}
	/**
	 * @return the leftHandSide
	 */
	public Expression getLeftHandSide() {
		return leftHandSide_;
	}
	/**
	 * @param leftHandSide the leftHandSide to set
	 */
	public void setLeftHandSide(Expression leftHandSide) {
		leftHandSide_ = leftHandSide;
	}
	/**
	 * @param rightHandSide the rightHandSide to set
	 */
	public void setRightHandSide(BinaryExpression rightHandSide) {
		rightHandSide_ = rightHandSide;
	}
	

	
}
