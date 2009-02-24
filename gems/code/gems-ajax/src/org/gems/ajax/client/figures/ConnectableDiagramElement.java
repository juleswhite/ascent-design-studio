package org.gems.ajax.client.figures;

import java.util.List;

import org.gems.ajax.client.connection.Connection;
import org.gems.ajax.client.edit.AnchorManager;

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

public interface ConnectableDiagramElement extends ResizableDiagramElement {

	public void updateConnections();

	public List<Connection> getConnections();

	public void attachOutput(Connection c);

	public void attachInput(Connection c);

	public void detachOutput(Connection c);

	public void detachInput(Connection c);

	public AnchorManager getAnchorManager();

}
