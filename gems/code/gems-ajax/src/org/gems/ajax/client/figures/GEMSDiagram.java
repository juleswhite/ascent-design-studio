package org.gems.ajax.client.figures;

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
import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.connection.ConnectionLayer;
import org.gems.ajax.client.connection.RectilinearRouter;
import org.gems.ajax.client.event.UIEventConnector;
import org.gems.ajax.client.geometry.Point;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class GEMSDiagram extends AbsolutePanel implements GraphicsConstants,
		DiagramElement {

//	private AbstractDiagramElement zoomViewPanel_;
//	private Widget zoomViewParent_;
//	private int zoomViewOZ_ = 0;

	private List<DiagramPanel> children_ = new ArrayList<DiagramPanel>();
	private FocusPanel eventTrap_;
	private ConnectionLayer connectionLayer_;
	private List<DiagramElementListener> panelListeners_ = new ArrayList<DiagramElementListener>();

	public GEMSDiagram() {
		super();
		ensureDebugId("gems-diagram");
		setStyleName(DIAGRAM_STYLE_NAME);
		eventTrap_ = new FocusPanel();
		add(eventTrap_);
		setWidgetPosition(eventTrap_, 0, 0);
		new UIEventConnector(eventTrap_, this);
		connectionLayer_ = new ConnectionLayer(this, new RectilinearRouter());// new
		// ManhattanShortestPathRouter(this));
	}

	protected void onAttach() {
		super.onAttach();
		DeferredCommand.addCommand(new Command() {
		
			public void execute() {
				setSize("2001px", "2001px");
			}
		
		});
		
	}

	public ConnectionLayer getConnectionLayer() {
		return connectionLayer_;
	}

	public void setConnectionLayer(ConnectionLayer connectionLayer) {
		connectionLayer_ = connectionLayer;
	}

	public void setHeight(String height) {
		super.setHeight(height);
		eventTrap_.setHeight(height);
	}

	public void setSize(String width, String height) {
		super.setSize(width, height);
		eventTrap_.setSize(width, height);
	}

	public void setWidth(String width) {
		super.setWidth(width);
		eventTrap_.setWidth(width);
	}

	public void growBottom(int amount) {
		setHeight((getOffsetHeight() + amount) + "px");
		setWidth((getOffsetWidth() + amount) + "px");
	}

	public void growRight(int amount) {
		setHeight((getOffsetHeight() + amount) + "px");
		setWidth((getOffsetWidth() + amount) + "px");
	}

	public void scrollViewportRight(int amount) {
		Util.scrollElementRight(getParent().getElement(), amount);
	}

	public void scrollViewportLeft(int amount) {
		Util.scrollElementLeft(getParent().getElement(), amount);
	}

	public void scrollViewportUp(int amount) {
		Util.scrollElementUp(getParent().getElement(), amount);
	}

	public void scrollViewportDown(int amount) {
		Util.scrollElementDown(getParent().getElement(), amount);
	}

	public Point getScrollLocation() {
		return new Point(Util.getScrollLeft(getParent().getElement()), Util
				.getScrollTop(getParent().getElement()));
	}

	public void reveal(Point p) {
		Point sl = getScrollLocation();
		if (sl.x + getViewport().getOffsetWidth() < p.x) {
			scrollViewportRight(p.x - (sl.x + getViewport().getOffsetWidth()));
		}
		if (sl.y + getViewport().getOffsetHeight() < p.y) {
			scrollViewportDown(p.y - (sl.y + getViewport().getOffsetHeight()));
		}
	}

	public void add(DiagramPanel p) {
		children_.add(p);
		super.add(p);

		for (DiagramElementListener l : panelListeners_)
			l.onChildAdded(p);
	}

	public void add(DiagramPanel p, int x, int y) {
		children_.add(p);
		super.add(p, x, y);

		for (DiagramElementListener l : panelListeners_)
			l.onChildAdded(p);
	}

	public void remove(AbstractDiagramElement p) {
		children_.remove(p);
		super.remove(p);

		for (DiagramElementListener l : panelListeners_)
			l.onChildRemoved(p);
	}

	public List<DiagramPanel> getFigureChildren() {
		return children_;
	}

	public Widget getViewport() {
		return getParent();
	}

	public boolean addPanelListener(DiagramElementListener arg0) {
		return panelListeners_.add(arg0);
	}

	public boolean removePanelListener(DiagramElementListener arg0) {
		return panelListeners_.remove(arg0);
	}

	public void showFullDiagramView(DiagramElement p) {
		// if (p == zoomViewPanel_) {
		// p.popState();
		// Util.setZIndex(p, zoomViewOZ_);
		// zoomViewPanel_ = null;
		// zoomViewParent_ = null;
		// } else {
		// zoomViewPanel_ = p;
		// zoomViewParent_ = p.getParent();
		//
		// int voff = 0;
		//
		// p.pushState();
		// setWidgetPosition(p, 0, 0);
		// p.setSize(getOffsetWidth() + "px", getOffsetHeight() + "px");
		// p.getBody().setSize(getOffsetWidth(), getOffsetHeight(), true);
		// p.getHeader().setWidth(getOffsetWidth(), true);
		// zoomViewOZ_ = Util.bringToFront(p);
		// }
	}

	public GEMSDiagram getDiagram() {
		return this;
	}

	public void focus() {
		eventTrap_.setFocus(true);
	}

	public void dispose() {
	}

	public void onDeSelect() {
	}

	public void onSelect() {
	}

	public void update() {
	}

	public Widget getDiagramWidget() {
		return this;
	}

	public void addDiagramElementListener(DiagramElementListener l) {
	}

	public void removeDiagramElementListener(DiagramElementListener l) {
	}

	public void onMove() {
	}

	public String getId() {
		return getElement().getId();
	}
}
