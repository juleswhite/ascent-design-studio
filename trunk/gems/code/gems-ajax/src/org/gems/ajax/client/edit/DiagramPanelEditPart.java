package org.gems.ajax.client.edit;

import org.gems.ajax.client.figures.AbstractDiagramElement;
import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.figures.DiagramElementListener;
import org.gems.ajax.client.figures.DiagramPanel;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.event.ModelListener;
import org.gems.ajax.client.util.GraphicsConstants;

import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class DiagramPanelEditPart extends DiagramElementEditPart implements
		ModelListener, ModelEditPart, EditConstants, DiagramElementListener,
		MovementAware, GraphicsConstants {

	private LayoutManager layoutManager_;
	
	public DiagramPanelEditPart(ModelHelper modelHelper, EditPartFactory fact,
			Object model) {
		super(modelHelper, fact, model);

	}

	public ConnectableDiagramElement createFigure(GEMSDiagram d) {
		DiagramPanel panel = new DiagramPanel(d);
		panel.setTitle(getModelHelper().getLabel(getModel()));

		EditPartManager.mapPartToElement(panel.getContentPanel().getElement(),
				this);
		EditPartManager.mapPartToElement(panel.getBodyPanel().getElement(),
				this);
		EditPartManager.mapPartToElement(panel.getHeaderPanel().getElement(),
				this);
		EditPartManager.mapPartToElement(panel.getToolBar().getElement(), this);

		return panel;
	}

	public AbstractDiagramElement getModelFigure() {
		return (AbstractDiagramElement) getFigure();
	}

	public void onDeSelect() {
		if (getModelFigure().getResizer() != null)
			getModelFigure().getResizer().hideDragHandle();

		super.onDeSelect();
	}

	public void onSelect() {

		if (getModelFigure().getResizer() != null)
			getModelFigure().getResizer().showDragHandle();

		super.onSelect();
	}

	public void onMove(DiagramElement p) {
		super.onMove(p);

		if (getModelFigure().getResizer() != null)
			if (getModelFigure().getResizer().handleShowing())
				getModelFigure().getResizer().updateDragHandle();
	}

	public void onAncestorMoved(DiagramElement p) {
		getModelFigure().onMove();
	}

	public void onResize(AbstractDiagramElement p, String w, String h) {
		super.onResize(p,w,h);

		if (getModelFigure().getResizer() != null)
			if (getModelFigure().getResizer().handleShowing())
				getModelFigure().getResizer().updateDragHandle();
		
//		updateLayout();
	}
	
	public void updateLayout(){
		if(layoutManager_ != null){
			layoutManager_.update();
		}
	}

	public Widget getContainer(Object child) {
		return getModelFigure().getBodyPanel();
	}

	public void addChild(EditPart child) {
		super.addChild(child);
		updateLayout();
	}

//	@Override
//	public ConnectableDiagramElement getFigure() {
//		DiagramPanel fig = (DiagramPanel)super.getFigure();
//		layoutManager_ = new VerticalLayout((AbsolutePanel)fig.getBodyPanel(),fig,this);
//		return fig;
//	}

	
}
