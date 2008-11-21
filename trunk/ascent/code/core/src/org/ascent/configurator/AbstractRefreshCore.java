package org.ascent.configurator;

import java.util.HashMap;
import java.util.Map;

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
public abstract class AbstractRefreshCore implements RefreshCore {
	
	public class UndefinedAttributeException extends RuntimeException {

		public UndefinedAttributeException(String arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

	}

	public class UnsupportedOperationException extends RuntimeException {

		public UnsupportedOperationException(String arg0) {
			super(arg0);
		}

	}

	public class UnsupportedExpressionType extends RuntimeException {

		public UnsupportedExpressionType(String arg0) {
			super(arg0);
		}

	}
	
	private boolean zeroUndefinedVariables_ = true;

	protected Map<Object, Map<Object, Object>> sourceVariableValuesTable_ = new HashMap<Object, Map<Object, Object>>();

	protected Map<Object, Map<Object, Object>> targetVariableValuesTable_ = new HashMap<Object, Map<Object, Object>>();

	public void setTargetVariableValues(Object target, Map values) {
		targetVariableValuesTable_.put(target, values);
	}

	public void setTargetVariableValues(Object target, Object[] values) {
		Map valuemap = new HashMap();
		for (int i = 0; i < values.length; i += 2) {
			valuemap.put(values[i], values[i + 1]);
		}
		targetVariableValuesTable_.put(target, valuemap);
	}

	public void setSourceVariableValues(Object source, Map values) {
		sourceVariableValuesTable_.put(source, values);
	}

	public void setSourceVariableValues(Object source, Object[] values) {
		Map valuemap = new HashMap();
		for (int i = 0; i < values.length; i += 2) {
			valuemap.put(values[i], values[i + 1]);
		}
		sourceVariableValuesTable_.put(source, valuemap);
	}
	
	public Map getTargetVariableValues(Object target) {
		Map vals = targetVariableValuesTable_.get(target);
		if (vals == null) {
			vals = new HashMap();
			targetVariableValuesTable_.put(target, vals);
		}
		return vals;
	}

	public Map getSourceVariableValues(Object source) {
		Map vals = sourceVariableValuesTable_.get(source);
		if (vals == null) {
			vals = new HashMap();
			sourceVariableValuesTable_.put(source, vals);
		}
		return vals;
	}
	
	/**
	 * @turn the zeroUndefinedVariables
	 */
	public boolean getZeroUndefinedVariables() {
		return zeroUndefinedVariables_;
	}

	/**
	 * @param zeroUndefinedVariables
	 *            the zeroUndefinedVariables to set
	 */
	public void setZeroUndefinedVariables(boolean zeroUndefinedVariables) {
		zeroUndefinedVariables_ = zeroUndefinedVariables;
	}
	
	public void clearVariableTables(){
		sourceVariableValuesTable_.clear();
		targetVariableValuesTable_.clear();
		sourceVariableValuesTable_ = new HashMap<Object, Map<Object,Object>>();
		targetVariableValuesTable_ = new HashMap<Object, Map<Object,Object>>();
	}
}
