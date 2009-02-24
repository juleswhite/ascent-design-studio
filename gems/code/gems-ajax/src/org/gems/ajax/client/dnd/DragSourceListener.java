package org.gems.ajax.client.dnd;

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

public interface DragSourceListener {
	public void onEnterTarget(DragSource ds, DropTarget t);
	public void onExitTarget(DragSource ds, DropTarget t);
	public void onDragStart(DragSource ds);
	public void onDrop(DragSource ds, DropTarget t);
	public void onDragEnd(DragSource ds);
}
