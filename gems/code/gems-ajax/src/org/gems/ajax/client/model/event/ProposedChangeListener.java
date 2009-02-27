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

	public void aboutToAddChild(ContainmentEvent evt);
	public void aboutToRemoveChild(ContainmentEvent evt);
	public void aboutToAddConnection(ConnectionEvent evt);
	public void aboutToRemoveConnection(ConnectionEvent evt);
	public void aboutToChangeProperty(PropertyEvent evt);
}
