package org.ascent.configurator.conf;

import org.ascent.expr.Expression;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class GoalHandler extends AbstractExpressionCreatorHandler {

	public class InvalidGoalDeclarationException extends RuntimeException {
		public InvalidGoalDeclarationException(String msg){
			super(msg);
		}
	}
	
	public static final String MAXIMIZE_PREFIX = "maximize";
	public static final String MINIMIZE_PREFIX = "minimize";

	/* (non-Javadoc)
	 * @see org.refresh.core.conf.AbstractExpressionCreatorHandler#handle(org.refresh.core.conf.RefreshProblem, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void handle(RefreshProblem problem, String context, String directive, String argstr) {
		boolean max = true;
		argstr = argstr.trim();
		if(argstr.startsWith(MAXIMIZE_PREFIX))
			argstr = argstr.substring(MAXIMIZE_PREFIX.length());
		else if(argstr.startsWith(MINIMIZE_PREFIX)){
			argstr = argstr.substring(MINIMIZE_PREFIX.length());
			max = false;
		}
		super.handle(problem, context, directive, argstr);
		problem.setMaximizeGoal(max);
	}



	/* (non-Javadoc)
	 * @see org.refresh.core.conf.AbstractExpressionCreatorHandler#handle(org.refresh.core.conf.RefreshProblem, java.lang.String, java.lang.String, java.lang.String, org.refresh.core.expr.Expression)
	 */
	@Override
	public void handle(RefreshProblem problem, String context, String directive, String argstr, Expression expr) {
		problem.setGoalFunction(expr);
	}
	
	

}
