package org.gems.ajax.client.connection;

import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.geometry.Point;
import org.gems.ajax.client.util.Util;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class ChopBoxAnchor extends WidgetSideAnchor {

	public ChopBoxAnchor(ConnectableDiagramElement owner) {
		super(owner, LEFT);
	}

	public Point getLocation(Point o) {
		Point p = Util.getDiagramLocation(getOwner().getDiagramWidget());
		
		int dx = Math.abs(o.x - p.x);
		int dy = Math.abs(o.y - p.y);
		
		if(dx > dy){
				if(p.x > o.x)
					setDirection(LEFT);
				else
					setDirection(RIGHT);
		} else {
			if(p.y > o.y)
				setDirection(TOP);
			else
				setDirection(BOTTOM);
		}
		
		return super.getLocation(o);
	}

}
