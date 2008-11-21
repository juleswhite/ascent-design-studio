package org.ascent.configurator.conf;

import org.ascent.configurator.RefreshEngine;

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
public interface ConfigurationAction {

	public void setRefreshEngine(RefreshEngine engine);
	
	public void run();
}
