package org.gems.ajax.client.edit;

import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.figures.ResizableDiagramElement;

import com.google.gwt.user.client.ui.AbsolutePanel;

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
public class VerticalLayout implements LayoutManager {

	private ResizableDiagramElement element_;
	private AbsolutePanel container_;
	private ModelEditPart parent_;
	
	private int yPad_ = 10;
	
	public VerticalLayout(AbsolutePanel container, ResizableDiagramElement el, ModelEditPart parent) {
		super();
		element_ = el;
		container_ = container;
		parent_ = parent;
	}

	public void update() {
		int x = 100;
		int y = 100;
		
		for(EditPart p : parent_.getChildren()){
			DiagramElement el = p.getFigure();
			if(el.getDiagramWidget().getOffsetWidth() > x)
				x = el.getDiagramWidget().getOffsetWidth();
			
			container_.setWidgetPosition(el.getDiagramWidget(), x, y);
			y += el.getDiagramWidget().getOffsetHeight() + yPad_;
		}
	
//		System.out.println(""+x+","+y);
//		element_.setSize(x+"px", y+"px");
	}

	public ResizableDiagramElement getElement() {
		return element_;
	}

	public void setElement(ResizableDiagramElement element) {
		element_ = element;
	}

}
