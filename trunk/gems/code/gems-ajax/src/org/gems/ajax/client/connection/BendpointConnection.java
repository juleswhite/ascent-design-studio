package org.gems.ajax.client.connection;

import org.gems.ajax.client.geometry.Point;

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
public abstract class BendpointConnection extends Connection {

	private Point[] bendPoints_;

	private ConnectionRouter router_;

	public BendpointConnection(ConnectionAnchor source,
			ConnectionAnchor target, ConnectionRouter router) {
		super(source, target);
		router_ = router;
	}

	public void update() {
		router_.route(this);
		repaint();
		updateDecorations();
		super.update();
	}

	public Point[] getBendPoints() {
		return bendPoints_;
	}

	public void setBendPoints(Point[] bendPoints) {
		bendPoints_ = bendPoints;
	}

	public ConnectionRouter getRouter() {
		return router_;
	}

	public void setRouter(ConnectionRouter router) {
		router_ = router;
		router_.invalidate(this);
	}

	public abstract void repaint();

}
