package org.gems.ajax.client.event;

import org.gems.ajax.client.figures.DiagramElement;

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
public interface UIEventListener {

	public void onMouseEnter(DiagramElement sender);

	public void onMouseLeave(DiagramElement sender);

	public void onMouseMove(DiagramElement sender, int x, int y);

	public void onMouseUp(DiagramElement sender, int x, int y) ;
	
	public void onMouseDown(DiagramElement el, int x, int y);

	public void onFocus(DiagramElement el);

	public void onLostFocus(DiagramElement el);
}
