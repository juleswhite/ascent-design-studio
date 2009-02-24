package org.gems.ajax.client.figures;

import com.google.gwt.dom.client.Element;
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
public interface DiagramElement {
	public Widget getDiagramWidget();
	public Element getElement();
	public GEMSDiagram getDiagram();
	public void onSelect();
	public void onDeSelect();
	public void onMove();
	public void update();
	public void dispose();
	public void addDiagramElementListener(DiagramElementListener l);
	public void removeDiagramElementListener(DiagramElementListener l);
	public String getId();
}
