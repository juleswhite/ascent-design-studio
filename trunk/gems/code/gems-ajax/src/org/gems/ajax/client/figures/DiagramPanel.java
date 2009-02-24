package org.gems.ajax.client.figures;


import org.gems.ajax.client.connection.ChopBoxAnchor;
import org.gems.ajax.client.connection.ConnectionAnchor;
import org.gems.ajax.client.connection.RectilinearConnection;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class DiagramPanel extends GPanel implements DiagramElement {

	public DiagramPanel(GEMSDiagram diagram, boolean withheader) {
		this(diagram, withheader, true, true, false);
	}

	public DiagramPanel(GEMSDiagram diagram) {
		this(diagram, true);
	}

	public DiagramPanel(GEMSDiagram diagram, boolean withheader,
			boolean moveable, boolean resizeable, boolean collapsible) {
		super(diagram, withheader, moveable, resizeable, collapsible);
		
	}

	public DiagramPanel(GEMSDiagram diagram, boolean withheader,
			boolean moveable, boolean resizeable, boolean collapsible,
			boolean manualinit) {
		super(diagram, withheader, moveable, resizeable, collapsible, manualinit);
	}

	public void setHeight(String height) {
		super.setHeight(height);
		updateConnections();
		for (DiagramElementListener p : listeners_) {
			p.onResize(this, getOffsetWidth()+"px", height);
		}
	}

	public void setWidth(String width) {
		super.setWidth(width);
		updateConnections();
		for (DiagramElementListener p : listeners_) {
			p.onResize(this, width, getOffsetHeight()+"px");
		}
	}

	public void collapse() {
		super.collapse();
		for (DiagramElementListener p : listeners_) {
			p.onResize(this, getOffsetWidth()+"px", getOffsetHeight()+"px");
		}
		updateConnections();
	}

	public void expand() {
		super.expand();
		for (DiagramElementListener p : listeners_) {
			p.onResize(this, getOffsetWidth()+"px", getOffsetHeight()+"px");
		}
		updateConnections();
	}

	public void connectTo(DiagramPanel target) {
		ConnectionAnchor ca = new ChopBoxAnchor(this);
		ConnectionAnchor cb = new ChopBoxAnchor(target);
		RectilinearConnection con = new RectilinearConnection(getDiagram(), ca,
				cb);
		con.setConnectionLayer(getDiagram().getConnectionLayer());

		attachOutput(con);
		target.attachInput(con);

		con.update();
	}



	public Widget getDiagramWidget(){
		return this;
	}
}
