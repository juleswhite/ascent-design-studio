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

public interface UIKeyListener {
	
	public void onKeyUp(DiagramElement sender, char keyCode, int modifiers);

	public void onKeyPress(DiagramElement sender, char keyCode, int modifiers);

	public void onKeyDown(DiagramElement sender, char keyCode, int modifiers);
}
