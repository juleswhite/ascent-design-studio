/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.probe;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BasicPlatformProbe implements PropertyProbe {
	public static final String OS_NAME = "os.name";

	public static final String OS_ARCH = "os.arch";

	public static final String OS_VERSION = "os.versions";

	public static final String PLATFORM_MEMORY = "memory";

	public static final String PLATFORM_FREE_MEMORY = "free-memory";

	private Map properties_;

	public void run() {
		properties_ = new HashMap(19);

		Properties p = System.getProperties();
		Enumeration keys = p.propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = p.getProperty(key);
			properties_.put(key, value);
		}

		long memmax = Runtime.getRuntime().maxMemory();
		properties_.put(PLATFORM_MEMORY, memmax);

		long fremem = Runtime.getRuntime().freeMemory();
		properties_.put(PLATFORM_FREE_MEMORY, fremem);
	}

	public Map getProperties() {
		return properties_;
	}

	public void setProperties(Map properties) {
		this.properties_ = properties;
	}

}
