/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.mmkp;

import java.util.List;
import java.util.Map;

import org.ascent.binpacking.OptimizationFunction;
import org.ascent.binpacking.ValueFunction;

public class SummationOptimization implements ValueFunction,
		OptimizationFunction {

	public double getValue(Object src) {
		return ((BasicItem)src).getValue();
	}

	public double getValue(Map<Object, List> src) {
		// TODO Auto-generated method stub
		return 0;
	}

}
