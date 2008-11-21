package org.ascent.injectors;
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
public class VariableNotInScopeException extends RuntimeException {

	private String variable_;
	
	public VariableNotInScopeException(String context, String variable) {
		super("The variable \""+variable+"\" is not in scope in the context \""+context+"\"");
		variable_ = variable;
	}

	public String getVariable() {
		return variable_;
	}

	public void setVariable(String variable) {
		variable_ = variable;
	}


}
