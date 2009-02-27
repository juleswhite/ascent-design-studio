package org.gems.ajax.client.figures;

import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.connection.Connection;
import org.gems.ajax.client.edit.AnchorManager;
import org.gems.ajax.client.event.UIEventConnector;
import org.gems.ajax.client.geometry.Point;
import org.gems.ajax.client.geometry.Rectangle;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;
import org.gems.ajax.client.util.dojo.MoveListener;

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

public abstract class AbstractDiagramElement extends EventTrapPanel implements GraphicsConstants, ConnectableDiagramElement, MoveListener {

	private GEMSDiagram diagram_;
	protected AnchorManager anchorManager_;
	private List<Connection> connections_ = new ArrayList<Connection>();
	protected List<DiagramElementListener> listeners_ = new ArrayList<DiagramElementListener>(
				3);

	public AbstractDiagramElement(GEMSDiagram diagram) {
		diagram_ = diagram;
		anchorManager_ = new AnchorManager(this);
		new UIEventConnector(this, this);
	}

	public void dispose() {
		if (getParent() != null)
			removeFromParent();
	}

	public void update() {
		updateConnections();
	}

	public void onMoveStart() {
	}

	public void onMoveStop() {
	}

	public void onMove() {
		for (DiagramElementListener p : listeners_) {
			p.onMove(this);
		}
	
		// updateConnections();
	}

	public void updateConnections() {
		for (int i = 0; i < connections_.size(); i++) {
			Connection c = (Connection) connections_.get(i);
			c.update();
		}
	}

	public List<Connection> getConnections() {
		return connections_;
	}

	public void setConnections(List<Connection> connections) {
		connections_ = connections;
	}

	public void attachOutput(Connection c) {
		connections_.add(c);
	}

	public void attachInput(Connection c) {
		connections_.add(c);
	}

	public void detachOutput(Connection c) {
		connections_.remove(c);
	}

	public void detachInput(Connection c) {
		connections_.remove(c);
	}

	public AnchorManager getAnchorManager() {
		return anchorManager_;
	}

	public void setAnchorManager(AnchorManager anchorManager) {
		anchorManager_ = anchorManager;
	}
	
	public GEMSDiagram getDiagram() {
		return diagram_;
	}

	public void setDiagram(GEMSDiagram diagram) {
		diagram_ = diagram;
	}

	public Widget getDiagramWidget() {
		return this;
	}
	
	public void addDiagramElementListener(DiagramElementListener e) {
		listeners_.add(e);
	}

	public void removeDiagramElementListener(DiagramElementListener o) {
		listeners_.remove(o);
	}

	public String getId() {
		return getElement().getId();
	}	
	
	public Rectangle getBounds(){
		Widget w = getDiagramWidget();
		Point p = Util.getDiagramLocation(w);
		int width = w.getOffsetWidth();
		int height = w.getOffsetHeight();
		return new Rectangle(p.x,p.y,width,height);
	}
	
	public Resizer getResizer(){
		return null;
	}
	
	public Widget getBodyPanel(){
		return null;
	}
}