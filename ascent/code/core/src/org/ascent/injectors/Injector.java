package org.ascent.injectors;

import java.util.List;
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
public interface Injector {

	public String inject(Map<String,List> features,
			Map<String, Map> values, String conf);

	/**
	 * @param enableTracing
	 *            the enableTracing to set
	 */
	public void setEnableTracing(boolean enableTracing);

}