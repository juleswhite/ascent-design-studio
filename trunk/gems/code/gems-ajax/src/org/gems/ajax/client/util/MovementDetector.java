package org.gems.ajax.client.util;

import org.gems.ajax.client.util.dojo.DomChangeListener;

import com.google.gwt.user.client.ui.Widget;

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

public class MovementDetector implements DomChangeListener{
	private int x_;
	private int y_;
	private MovementListener listener_;
	private Widget widget_;
	
	public MovementDetector(Widget w, MovementListener l) {
		super();
		widget_ = w;
		listener_ = l;
		x_ = Util.getDiagramX(w);
		y_ = Util.getDiagramY(w);
	}

	public void onAttributeChanged() {
		int nx = Util.getDiagramX(widget_);
		int ny = Util.getDiagramY(widget_);
		if(nx != x_ || ny != y_){
			listener_.moved(nx - x_, ny - y_);
			x_ = nx;
			y_ = ny;
		}
	}
	

	
}
