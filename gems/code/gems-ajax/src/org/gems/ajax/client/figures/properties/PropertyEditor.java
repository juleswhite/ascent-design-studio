package org.gems.ajax.client.figures.properties;

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

public interface PropertyEditor {
	public Object getValue();
	public void edit(Object v);
	public Widget getWidget();
	public void addListener(PropertyEditorListener l);
	public void removeListener(PropertyEditorListener l);
}
