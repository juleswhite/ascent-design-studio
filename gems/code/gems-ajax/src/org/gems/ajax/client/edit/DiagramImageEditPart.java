package org.gems.ajax.client.edit;

import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.figures.DiagramImagePanel;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.model.ModelHelper;

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

public class DiagramImageEditPart extends DiagramElementEditPart {

	public DiagramImageEditPart(ModelHelper modelHelper, EditPartFactory fact,
			Object model) {
		super(modelHelper, fact, model);
	}

	public Widget getContainer(Object child) {
		return null;
	}

	public Element getContainerElement(Object child) {
		return null;
	}

	public ConnectableDiagramElement createFigure(GEMSDiagram d) {
		DiagramImagePanel dp = new DiagramImagePanel(getDiagram());
		dp.addGlobalStyleDependentName("ip");
		return dp;
	}

	
}
