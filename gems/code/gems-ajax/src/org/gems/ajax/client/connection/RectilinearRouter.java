package org.gems.ajax.client.connection;

import org.gems.ajax.client.geometry.Point;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

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
public class RectilinearRouter implements ConnectionRouter, GraphicsConstants {

	private int leadOffset_ = 25;
	
	public void route(BendpointConnection conn) {
		ConnectionAnchor src = conn.getSource();
		ConnectionAnchor trg = conn.getTarget();
		
		src.update();
		trg.update();
		Point s = src.getLocation(Util.getDiagramLocation(trg.getOwner().getDiagramWidget()));
		Point t = trg.getLocation(Util.getDiagramLocation(src.getOwner().getDiagramWidget()));
		Point s1 = getStartPoint(src,s);
		Point t1 = getStartPoint(trg, t);

		int dx = t1.x - s1.x;
		int dy = t1.y - s1.y;
		int adx = Math.abs(dx);
		int ady = Math.abs(dy);
		
		if(adx > ady){
			conn.setBendPoints(new Point[]{
					new Point(s.x,s.y),
					s1,
					new Point(s1.x, Util.half(dy) + s1.y),
					new Point(t1.x, Util.half(dy) + s1.y),
					t1,
					new Point(t.x,t.y)
			});
		}
		else{
			conn.setBendPoints(new Point[]{
					new Point(s.x,s.y),
					s1,
					new Point(Util.half(dx) + s1.x,s1.y),
					new Point(Util.half(dx) + s1.x,t1.y),
					t1,
					new Point(t.x,t.y)
			});
		}
	}
	
	public Point getStartPoint(ConnectionAnchor anchor, Point s){
		int direction = anchor.getDirection();
		Point start = s.getCopy();
		
		switch (direction) {
		case LEFT:
			start.translate(-1 * leadOffset_,0);
			break;
		case RIGHT:
			start.translate(leadOffset_,0);
			break;
		case TOP:
			start.translate(0, -1 * leadOffset_);
			break;
		case BOTTOM:
			start.translate(0, leadOffset_);
			break;
		}
		
		return start;
	}

	public void invalidate(BendpointConnection conn) {
	}

	public void remove(BendpointConnection conn) {
	}

}
