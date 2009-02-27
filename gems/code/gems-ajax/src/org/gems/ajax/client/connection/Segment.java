package org.gems.ajax.client.connection;

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
import org.gems.ajax.client.event.UIEventConnector;
import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.figures.DiagramElementListener;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.geometry.Point;
import org.gems.ajax.client.util.GraphicsConstants;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class Segment extends FocusPanel implements GraphicsConstants,
		DiagramElement {

	private GEMSDiagram diagram_;
	private RectilinearConnection connection_;
	private String styleBaseName_ = "";
	private Point location_;
	private boolean horizontal_ = false;
	private boolean selected_ = false;
	private int length_;

	public Segment(GEMSDiagram diagram, RectilinearConnection p) {
		super();
		diagram_ = diagram;
		connection_ = p;
		new UIEventConnector(this, p);
	}
	
	protected Segment(){}

	public void update() {
		updateStyle();
	}

	public void dispose() {
		removeFromParent();
	}

	public void onSelect() {
		selected_ = true;
		updateStyle();
	}

	public void onDeSelect() {
		selected_ = false;
		updateStyle();
	}

	public void updateStyle() {
		if (horizontal_) {
			if (selected_)
				setStylePrimaryName(getStyleBaseName()+SELECTED_HORIZONTAL_LINE_STYLE);
			else
				setStylePrimaryName(getStyleBaseName()+HORIZONTAL_LINE_STYLE);
		} else {
			if (!selected_)
				setStylePrimaryName(getStyleBaseName()+VERTICAL_LINE_STYLE);
			else
				setStylePrimaryName(getStyleBaseName()+SELECTED_VERTICAL_LINE_STYLE);
		}
	}

	public boolean isHorizontal() {
		return horizontal_;
	}

	public void setHorizontal(boolean h) {
		horizontal_ = h;
		updateStyle();
	}

	public int getLength() {
		return length_;
	}

	public void setLength(int length) {
		length_ = length;
		if (isHorizontal())
			setWidth(length_ + "px");
		else
			setHeight(length_ + "px");

	}

	public Point getLocation() {
		return location_;
	}

	public void setLocation(Point location) {
		location_ = location;
		((AbsolutePanel) getParent()).setWidgetPosition(this, location.x,
				location.y);
	}

	public GEMSDiagram getDiagram() {
		return diagram_;
	}

	public void setDiagram(GEMSDiagram diagram) {
		diagram_ = diagram;
	}

	public String getStyleBaseName() {
		return styleBaseName_;
	}

	public void setStyleBaseName(String styleBaseName) {
		styleBaseName_ = styleBaseName;
		updateStyle();
	}

	public Widget getDiagramWidget() {
		return this;
	}
	
	public String getId() {
		return getElement().getId();
	}

	public RectilinearConnection getConnection() {
		return connection_;
	}

	public void setConnection(RectilinearConnection connection) {
		connection_ = connection;
	}

	public void onMove() {
	}

	public void addDiagramElementListener(DiagramElementListener l) {
	}

	public void removeDiagramElementListener(DiagramElementListener l) {
	}

}
