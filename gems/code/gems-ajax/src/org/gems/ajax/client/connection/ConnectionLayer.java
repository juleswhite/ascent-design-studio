package org.gems.ajax.client.connection;

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
import com.google.gwt.user.client.ui.AbsolutePanel;

public class ConnectionLayer {

	private ConnectionRouter connectionRouter_;
	private AbsolutePanel connectionPanel_;

	public ConnectionLayer(AbsolutePanel connectionPanel, ConnectionRouter connectionRouter) {
		super();
		connectionRouter_ = connectionRouter;
		connectionPanel_ = connectionPanel;
	}

	public ConnectionRouter getConnectionRouter() {
		return connectionRouter_;
	}

	public void setConnectionRouter(ConnectionRouter connectionRouter) {
		connectionRouter_ = connectionRouter;
	}

	public AbsolutePanel getConnectionPanel() {
		return connectionPanel_;
	}

	public void setConnectionPanel(AbsolutePanel connectionPanel) {
		connectionPanel_ = connectionPanel;
	}

}
