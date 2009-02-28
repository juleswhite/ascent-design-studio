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
public interface ProposedChangeListener extends ModelListener {

	/**
	 * Called before a parent has a new child added.
	 * @param evt
	 */
	public void aboutToAddChild(ContainmentEvent evt);
	
	/**
	 * Called before a parent has a child removed.
	 * @param evt
	 */
	public void aboutToRemoveChild(ContainmentEvent evt);
	
	/**
	 * Called before a connection is added to an element.
	 * This method is dispatched once for both the source and target
	 * of the association.
	 * @param evt
	 */
	public void aboutToAddConnection(ConnectionEvent evt);
	
	/**
	 * Called before a connection is removed from an element.
	 * This method is dispatched once for both the source and target
	 * of the association.
	 * @param evt
	 */
	public void aboutToRemoveConnection(ConnectionEvent evt);
	
	/**
	 * Called before a property on an element is changed.
	 * @param evt
	 */
	public void aboutToChangeProperty(PropertyEvent evt);
	
}
