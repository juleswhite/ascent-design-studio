package org.ascent.configurator.conf;

import java.util.HashMap;
import java.util.Map;

import org.ascent.expr.BinaryExpression;
import org.ascent.expr.Expression;
import org.ascent.expr.VariableValueExpression;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class DefineVarHandler implements ConfigDirectiveHandler {

	public class InvalidVariableDeclarationException extends RuntimeException {
		public InvalidVariableDeclarationException(String msg) {
			super(msg);
		}
	}

	public void handle(RefreshProblem problem, String context,
			String directive, String argstr) {
		
		Expression expr = ExpressionParser.getExpression(argstr);
		
		if (expr == null || !(expr instanceof BinaryExpression) || !((BinaryExpression)expr).getOperation().equals("="))
			throw new InvalidVariableDeclarationException(
					"A variable declaration must use the form \"VAR = VALUE\" and the declaration \""
							+ argstr
							+ "\" in context \""
							+ context
							+ "\" does not.");
		
		BinaryExpression bexp = (BinaryExpression)expr;
		Expression valexpr = bexp.getRightHandSide();
		String var = context.trim()+"."+((VariableValueExpression)bexp.getLeftHandSide()).getVariable();

		problem.getContextVariableDefinitions(context).put(var, valexpr);
		
	}

}
