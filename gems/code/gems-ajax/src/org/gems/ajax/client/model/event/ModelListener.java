package org.gems.ajax.client.model.event;

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

	/**
	 * Called after a parent has a new child added.
	 * @param evt
	 */
	public void childAdded(ContainmentEvent evt);
	
	/**
	 * Called after a parent has a child removed.
	 * @param evt
	 */
	public void childRemoved(ContainmentEvent evt);
	
	/**
	 * Called after a connection is added to an element.
	 * This method is dispatched once for both the source and target
	 * of the association.
	 * @param evt
	 */
	public void connectionAdded(ConnectionEvent evt);
	
	/**
	 * Called after a connection is removed from an element.
	 * This method is dispatched once for both the source and target
	 * of the association.
	 * @param evt
	 */
	public void connectionRemoved(ConnectionEvent evt);
	
	/**
	 * Called after a property has changed on an element.
	 * @param evt
	 */
	public void propertyChanged(PropertyEvent evt);
	
	
	
}
