package org.ascent.configurator;

import java.util.List;
import java.util.Map;

import org.ascent.expr.BinaryExpression;
import org.ascent.expr.Expression;

import choco.Constraint;
import choco.Solver;
import choco.Var;
import choco.integer.IntExp;
import choco.integer.IntVar;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public interface ChocoRefreshCore extends RefreshCore {

	/**
	 * @return the optimizationFunction
	 */
	public abstract IntExp getOptimizationFunction();

	
	public abstract Map<Object, List> nextMapping(Solver s);

	public abstract Map<Object, IntVar> getTargetTable(Object src);

	public abstract Constraint getSourceMappingConstraint(Object src,
			Object trg, IntVar mapvar, BinaryExpression expr);

	public abstract Constraint getConstraint(Expression lhs, Expression rhs,
			String op);

	public abstract Constraint getConstraint(Object src, Object trg,
			Expression lhs, Expression rhs, String op);

	public abstract IntExp getIntExpr(Object src, Object trg, Expression exp);

	public abstract IntExp getOrCreateSummation(Object key, Object var,
			Map<Object, Map<Object, Var>> avals,
			Map<Object, Map<Object, Object>> vals);

	public abstract int getValue(Object item, Object var,
			Map<Object, Map<Object, Object>> vals);

	public abstract IntVar getOrCreatePresenceVariable(Object item);

	public abstract IntVar getOrCreateIntVariableValue(Object item, Object var,
			Map<Object, Map<Object, Var>> avals,
			Map<Object, Map<Object, Object>> vals);

	public abstract IntExp getIntExpr(Expression exp);

	public abstract IntExp getIntExpr(IntExp lhs, IntExp rhs, String op);

}
