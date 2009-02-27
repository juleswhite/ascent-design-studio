package org.gems.ajax.client.figures.properties;

import com.google.gwt.user.client.ui.SourcesClickEvents;
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

public interface PropertyViewer {
	public Object getValue();
	public void setValue(Object v);
	public Widget getDisplayWidget();
	public SourcesClickEvents getEditTrigger();
}
