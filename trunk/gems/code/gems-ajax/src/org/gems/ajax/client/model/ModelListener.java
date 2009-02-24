package org.gems.ajax.client.model;

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

public interface ModelListener {

	public void childAdded(ContainmentEvent evt);
	public void childRemoved(ContainmentEvent evt);
	public void connectionAdded(ConnectionEvent evt);
	public void connectionRemoved(ConnectionEvent evt);
	public void propertyChanged(PropertyEvent evt);
}
