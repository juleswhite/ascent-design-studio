package org.gems.ajax.client.edit;

import java.util.Map;

import org.gems.ajax.client.dnd.DnDManager;
import org.gems.ajax.client.dnd.DropTarget;
import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.figures.DiagramPanel;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.model.ModelHelper;

import com.google.gwt.user.client.DeferredCommand;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class RootEditPart extends AbstractModelEditPart {

	private GEMSDiagram diagram_;
	
	public RootEditPart(ModelHelper modelHelper, EditPartFactory fact, GEMSDiagram diagram,
			Object model) {
		super(modelHelper,fact,model);
		diagram_ = diagram;
		
		DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {		
			public void execute() {
				DnDManager.getInstance().addDropTarget(new DropTarget(RootEditPart.this, diagram_,CONTAINMENT_TARGET));
			}		
		});
	}

	public Command getCommand(Request r) {
		if(DELETE_REQUEST.equals(r))
			return null;
		
		return super.getCommand(r);
	}

	public DiagramElement getFigure() {
		return diagram_;
	}

	public void addChild(EditPart child) {
		super.addChild(child);
		diagram_.add(child.getFigure().getDiagramWidget(),0,0);
	}

	public void connectTo(ModelEditPart target, Map<String, Object> coninfo) {
	}

	public ConnectableDiagramElement createFigure(GEMSDiagram d) {
		return null;//diagram_;
	}

	public DiagramPanel getModelFigure() {
		return null;
	}

	public void removeChild(EditPart child) {
		super.removeChild(child);
		diagram_.remove(child.getFigure().getDiagramWidget());
	}

	public void focus() {
		diagram_.focus();
	}

	public void onDeSelect() {
		diagram_.onDeSelect();
	}

	public void onSelect() {
		diagram_.onSelect();
	}

	public void dispose() {
	}

}
