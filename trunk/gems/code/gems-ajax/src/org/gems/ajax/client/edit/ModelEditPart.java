package org.gems.ajax.client.edit;

import java.util.List;

import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.figures.GEMSDiagram;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public interface ModelEditPart extends EditPart {

	public ConnectableDiagramElement getModelFigure();

	public void addChild(EditPart child);

	public void removeChild(EditPart child);
	
	public List<EditPart> getChildren();
	
	public void connectTo(ModelEditPart target, String assoctype);
}
