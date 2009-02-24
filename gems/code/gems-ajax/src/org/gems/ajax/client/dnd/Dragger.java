package org.gems.ajax.client.dnd;

import org.gems.ajax.client.edit.EditConstants;
import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SimplePanel;
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

public class Dragger extends DragSource implements EditConstants {

	private DiagramElement diagramElement_;
	
	public Dragger(DiagramElement diagramel, FocusPanel widget, Widget avatar) {
		super(widget, avatar, CONTAINMENT_TARGET);
		diagramElement_ = diagramel;
	}
	
	public Dragger(FocusPanel widget, DiagramElement de) {
		super(widget, new SimplePanel(), CONTAINMENT_TARGET);
		getAvatar().setStylePrimaryName(DRAGGER_AVATAR_STYLE);
		diagramElement_ = de;
	}
	
	public Dragger(FocusPanel widget) {
		super(widget, new SimplePanel(), CONTAINMENT_TARGET);
		getAvatar().setStylePrimaryName(DRAGGER_AVATAR_STYLE);
	}

	public void startDrag(int x, int y) {
		int w = getWidget().getOffsetWidth();
		int h = getWidget().getOffsetHeight();
		getAvatar().setSize(w+"px", h+"px");
		super.startDrag(x, y);
	}

	public void onDrop(DropTarget dt, int x, int y) {
		AbsolutePanel p = (getWidget().getParent() instanceof AbsolutePanel) ? (AbsolutePanel)getWidget().getParent() : null;
		if(p != null){
			x = Util.toDiagramX(getWidget(), x);
			y = Util.toDiagramY(getWidget(), y);
			
			p.setWidgetPosition(getWidget(), x - getXOff(), y - getYOff());
			if(diagramElement_ != null){
				diagramElement_.onMove();
				diagramElement_.onMove();
			}
		}
	}

	
}
