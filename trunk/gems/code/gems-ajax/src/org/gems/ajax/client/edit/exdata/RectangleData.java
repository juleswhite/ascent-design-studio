package org.gems.ajax.client.edit.exdata;

import org.gems.ajax.client.geometry.Rectangle;

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

public class RectangleData implements DataType<Rectangle>{
	
	public static final RectangleData INSTANCE = new RectangleData();
	
	private RectangleData(){}

	public Rectangle fromString(String data) {
		if(data == null)
			return new Rectangle(0,0,0,0);
		
		int split = data.indexOf(",");
		int split2 = data.indexOf(",",split+1);
		int split3 = data.indexOf(",",split+1);
		if(split < 0 || split2 < 0 || split3 < 0)
			return new Rectangle(0,0,0,0);
		
		int x = Integer.parseInt(data.substring(0,split).trim());
		int y = Integer.parseInt(data.substring(split+1,split2).trim());
		int w = Integer.parseInt(data.substring(split2+1,split3).trim());
		int h = Integer.parseInt(data.substring(split3+1).trim());
		return new Rectangle(x,y,w,h);
	}

	public String toString(Rectangle data) {
		return data.x+","+data.y+","+data.width+","+data.height;
	}

}
