package org.gems.ajax.server.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ServerUtils {
	
	public static final String APPLICATION_CONFIG = "gems-server.xml";

	private static ApplicationContext serverContext_;

	private static final ServerUtils instance_ = new ServerUtils();

	private ServerUtils() {
	}

	public static ServerUtils getInstance() {
		return instance_;
	}

	public synchronized ApplicationContext getServerApplicationContext() {
		if (serverContext_ == null) {
			serverContext_ = new ClassPathXmlApplicationContext(
					APPLICATION_CONFIG);
		}
		return serverContext_;
	}
}
