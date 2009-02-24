package org.gems.ajax.client.connection;

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

public interface ConnectionDecoration {
	
	/**
	 * This method should return true if the
	 * widget will handle its own events.
	 * Otherwise, listeners will be added
	 * to the widget to delegate its events
	 * to the connection and its EditPart.
	 * @return
	 */
	public boolean delegatesEvents();
	
	/**
	 * This method should return the location
	 * on the connection that the decoration
	 * should appear. 
	 * @return
	 */
	public ConnectionLocation getLocation();
	
	/**
	 * This method should return the widget
	 * which will render the decoration.
	 * @return
	 */
	public Widget getWidget();
	
	/**
	 * This method should rotate the
	 * widget (if needed) to point in
	 * the direction specified. 
	 * 
	 * Currently, valid directions are
	 * UP,DOWN,LEFT,RIGHT.
	 * 
	 * If this method creates a new
	 * widget to handle the change
	 * in direction, the decoration
	 * must remove the old widget
	 * and dispose of it properly.
	 * 
	 * @param direction
	 */
	public void setDirection(int direction);
	
	/**
	 * This method should dispose of the
	 * widget that renders this decoration
	 * and release any resources that are held by
	 * the decoration.
	 */
	public void dispose();
	
	/**
	 * This method is called by the connection
	 * after the decoration has been positioned
	 * on the connection. This method should do
	 * any updating that is needed to the decoration.
	 */
	public void update();
}
