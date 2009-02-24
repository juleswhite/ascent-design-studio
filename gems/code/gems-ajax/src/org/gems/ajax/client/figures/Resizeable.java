package org.gems.ajax.client.figures;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.MouseListener;
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

public interface Resizeable {
	public AbsolutePanel getTargetParent();
	public void addMouseListener(MouseListener l);
	public void removeMouseListener(MouseListener l);
	public void setSize(String w, String h);
	public Widget getTargetWidget();
	public boolean inDragHandle(int x, int y, int draghandlewidth, int draghandleheight);
}
