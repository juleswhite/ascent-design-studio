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
public class BasicConfigurationBinder implements ConfigurationBinder {
	private Injector injector_;
	private Map<String, List> features_;
	private Map<String, Map> values_;
	
	public BasicConfigurationBinder(Injector injector, Map<String, List> features, Map<String, Map> values) {
		super();
		injector_ = injector;
		features_ = features;
		values_ = values;
	}
	
	public String bind(String conf){
		return injector_.inject(features_, values_, conf);
	}
}
