package org.gems.ajax.client.edit.exdata;

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

public class PointData implements DataType<Point> {

	public static final PointData INSTANCE = new PointData();
	
	private PointData(){}
	
	public Point fromString(String data) {
		if(data == null)
			return new Point(0,0);
		
		int split = data.indexOf(",");
		if(split < 0)
			return new Point(0,0);
		
		int x = Integer.parseInt(data.substring(0,split).trim());
		int y = Integer.parseInt(data.substring(split+1).trim());
		return new Point(x,y);
	}

	public String toString(Point data) {
		return data.x+","+data.y;
	}

}
