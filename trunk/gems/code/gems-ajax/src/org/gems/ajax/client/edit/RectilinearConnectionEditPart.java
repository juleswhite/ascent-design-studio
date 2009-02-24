package org.gems.ajax.client.edit;

import org.gems.ajax.client.connection.BasicConnectionLocation;
import org.gems.ajax.client.connection.BasicDecoration;
import org.gems.ajax.client.connection.ChopBoxAnchor;
import org.gems.ajax.client.connection.Connection;
import org.gems.ajax.client.connection.ConnectionAnchor;
import org.gems.ajax.client.connection.ConnectionDecoration;
import org.gems.ajax.client.connection.RectilinearConnection;
import org.gems.ajax.client.figures.AbstractDiagramElement;
import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.figures.DetailFigure;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.util.GraphicsConstants;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class RectilinearConnectionEditPart extends AbstractConnectionEditPart {

	private ConnectionDecoration propertiesDecoration_;
	private DetailFigure detailFigure_ = new DetailFigure(new HTML(
			"<b>Properties:</b>"));

	public RectilinearConnectionEditPart(ModelHelper modelHelper,
			EditPartFactory f, Object model) {
		super(modelHelper, f, model);
	}

	public Connection createConnection(GEMSDiagram d,
			ConnectionAnchor srcanchor, ConnectionAnchor trganchor) {
		Connection con = new RectilinearConnection(d, srcanchor, trganchor);
		con.addDecoration(new BasicDecoration("circle",
				new BasicConnectionLocation(GraphicsConstants.START)));
		BasicConnectionLocation bcl = new BasicConnectionLocation(
				GraphicsConstants.END);
		con.addDecoration(new BasicDecoration("arrow", bcl));
		return con;
	}

	public ConnectionAnchor createSourceAnchor(ConnectableDiagramElement src) {
		return new ChopBoxAnchor(src);
	}

	public ConnectionAnchor createTargetAnchor(ConnectableDiagramElement trg) {
		return new ChopBoxAnchor(trg);
	}

	public void focus() {
		getConnectionFigure().focus();
	}

	public void onDeSelect() {
		getRConnectionFigure().onDeSelect();
		getRConnectionFigure().removeDecoration(propertiesDecoration_);
		detailFigure_.detach();
	}

	public void dispose() {
		if(detailFigure_.isAttached())
			detailFigure_.detach();
		getRConnectionFigure().dispose();
	}

	public void onSelect() {

		detailFigure_.updateSize();
		getRConnectionFigure().onSelect();
		BasicConnectionLocation loc = new BasicConnectionLocation(
				GraphicsConstants.MIDDLE);

		SimplePanel ball = new SimplePanel();
		ball
				.setStylePrimaryName(GraphicsConstants.CONNECTION_HANDLE_FLOAT_STYLE);

		loc.setConnectionLengthRelativeOffset(.5);
		detailFigure_.addStyleDependentName("small");
		propertiesDecoration_ = new BasicDecoration(loc, ball);
		getRConnectionFigure().addDecoration(propertiesDecoration_);
		getRConnectionFigure().update();
		detailFigure_.attach(getRConnectionFigure().getDiagram(),
				propertiesDecoration_);
	}

	public RectilinearConnection getRConnectionFigure() {
		return (RectilinearConnection) getConnectionFigure();
	}

	public ModelEditPart getParent() {
		return null;
	}

	public void setParent(ModelEditPart parent) {
	}

}
