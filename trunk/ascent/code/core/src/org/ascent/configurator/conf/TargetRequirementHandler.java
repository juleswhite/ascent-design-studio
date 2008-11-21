package org.ascent.configurator.conf;

import java.util.ArrayList;
import java.util.List;

import org.ascent.configurator.Constraint;
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
public class TargetRequirementHandler extends AbstractExpressionCreatorHandler {

	public class MalformedConstraintException extends RuntimeException {
		public MalformedConstraintException(String msg) {
			super(msg);
		}
	}

	
	public void handle(RefreshProblem problem, String context,
			String directive, String argstr, Expression curr) {
		

		if (!(curr instanceof BinaryExpression)) {
			throw new MalformedConstraintException(
					"A constraint must specify at least one variable, one comparison, and one comparison value. The constraint \""
							+ argstr
							+ "\" does not specify all of these elements.");
		}

		List<Constraint> cons = problem.getSourceFeasibilityConstraintsMap()
				.get(context);
		if (cons == null) {
			cons = new ArrayList<Constraint>();
		}
		cons.add(new Constraint((BinaryExpression) curr));
		problem.getSourceFeasibilityConstraintsMap().put(context, cons);
	}

}
