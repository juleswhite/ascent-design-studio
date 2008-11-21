package org.ascent.binpacking;

import java.util.List;
import java.util.Map;

import org.ascent.configurator.RefreshCore;

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
public interface SearchAlgorithm {

	public void prepareIteration(RefreshCore core);
	public void iterate(Map<Object,List> solution);
	public void finishIteration(RefreshCore core);
}
