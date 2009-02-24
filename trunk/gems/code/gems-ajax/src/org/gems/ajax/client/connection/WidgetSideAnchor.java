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
import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

public class WidgetSideAnchor extends ConnectionAnchor implements
		GraphicsConstants {

	public WidgetSideAnchor(ConnectableDiagramElement owner, int side) {
		super(owner);
		setDirection(side);
	}

	public void update() {
		int side = getDirection();

		switch (side) {
		case LEFT:
			location_.x = Util.getDiagramX(getOwner().getDiagramWidget());
			location_.y = midY();
			break;
		case RIGHT:
			location_.x = Util.getDiagramX(getOwner().getDiagramWidget())
					+ getOwner().getDiagramWidget().getOffsetWidth();
			location_.y = midY();
			break;
		case BOTTOM:
			location_.x = midX();
			location_.y = Util.getDiagramY(getOwner().getDiagramWidget())
					+ getOwner().getDiagramWidget().getOffsetHeight();
			break;
		case TOP:
			location_.x = midX();
			location_.y = Util.getDiagramY(getOwner().getDiagramWidget());
			break;
		}

		super.update();
	}

	public int midY() {
		return Util.getDiagramY(getOwner().getDiagramWidget())
				+ Util.half(getOwner().getDiagramWidget().getOffsetHeight());
	}

	public int midX() {
		return Util.getDiagramX(getOwner().getDiagramWidget())
				+ Util.half(getOwner().getDiagramWidget().getOffsetWidth());
	}

}
