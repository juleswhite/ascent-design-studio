package org.gems.ajax.client.edit;

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

public interface MovementAware {
	
	/**
	 * This method is called when the
	 * figure that is directly being
	 * listened to is moved.
	 * @param p
	 */
	public void onMove(DiagramElement p);
	
	/**
	 * This method is called when an
	 * ancesctor of the figure being
	 * listened to is moved.
	 * @param p
	 */
	public void onAncestorMoved(DiagramElement p);
}
