package org.gems.ajax.client.edit;

import org.gems.ajax.client.connection.Connection;
import org.gems.ajax.client.connection.ConnectionAnchor;
import org.gems.ajax.client.connection.ConnectionLayer;
import org.gems.ajax.client.edit.cmd.DisconnectCommand;
import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.model.ModelHelper;

import com.google.gwt.user.client.Element;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public abstract class AbstractConnectionEditPart extends AbstractEditPart
		implements ConnectionEditPart, EditConstants {

	private boolean attached_ = false;
	private ConnectionAnchor sourceAnchor_;
	private ConnectionAnchor targetAnchor_;
	private ConnectionLayer connectionLayer_;
	private ModelEditPart source_;
	private ModelEditPart target_;
	private Connection connectionFigure_;
	private EditDomain editDomain_;

	public AbstractConnectionEditPart(ModelHelper modelHelper,
			EditPartFactory fact, Object model) {
		super(modelHelper, fact, model);
	}

	public void attach(ModelEditPart src, ModelEditPart trg) {
		if (source_ != null) {
			detach();
		}
		if (src != null && trg != null && src.getFigure() != null
				&& trg.getModelFigure() != null) {

			attached_ = true;
			source_ = src;
			target_ = trg;

//			long start = System.currentTimeMillis();
			
			getModelHelper().connect(source_.getModel(), target_.getModel(),
					getModel());
			ConnectionAnchor ca = createSourceAnchor(src.getModelFigure());
			ConnectionAnchor cb = createTargetAnchor(trg.getModelFigure());
			
//			System.out.println("a "+(System.currentTimeMillis()-start));
//			start = System.currentTimeMillis();
			
			sourceAnchor_ = ca;
			targetAnchor_ = cb;
			
			
			
			Connection con = createConnection(
					src.getModelFigure().getDiagram(), ca, cb);

//			System.out.println("b "+(System.currentTimeMillis()-start));
//			start = System.currentTimeMillis();
			
			
			EditPartManager.mapPartToFigure((DiagramElement) con, this);
			for(Element e : con.getElements())
				EditPartManager.mapPartToElement(e, this);
			
			
//			System.out.println("c "+(System.currentTimeMillis()-start));
//			start = System.currentTimeMillis();
			
			
			con.setConnectionLayer(connectionLayer_);

			src.getModelFigure().attachOutput(con);
			trg.getModelFigure().attachInput(con);
			
//			System.out.println("d "+(System.currentTimeMillis()-start));
//			start = System.currentTimeMillis();
			
			
			connectionFigure_ = con;
			con.update();
			
//			System.out.println("update "+(System.currentTimeMillis()-start));
//			start = System.currentTimeMillis();
			
			ca.attach(con);
			cb.attach(con);
			ca.forceConnectionUpdate();
//			
//			System.out.println("attach "+(System.currentTimeMillis()-start));
//			start = System.currentTimeMillis();
//
//			System.out.println("e "+(System.currentTimeMillis()-start));
//			start = System.currentTimeMillis();
//			
			
//			src.getModelFigure().onMove();
//			trg.getModelFigure().onMove();
			
			con.update();
			
//			System.out.println("f "+(System.currentTimeMillis()-start));
//			start = System.currentTimeMillis();
			
		}
	}

	public abstract ConnectionAnchor createSourceAnchor(ConnectableDiagramElement src);

	public abstract ConnectionAnchor createTargetAnchor(ConnectableDiagramElement src);

	public abstract Connection createConnection(GEMSDiagram diagram,
			ConnectionAnchor srcanchor, ConnectionAnchor trganchor);

	public void detach() {
		attached_ = false;

		if (source_ != null && target_ != null && getModel() != null
				&& connectionFigure_ != null) {
			EditPartManager.unMap(connectionFigure_);
			getModelHelper().disconnect(source_.getModel(), target_.getModel(),
					getModel());
			source_.getModelFigure().detachOutput(connectionFigure_);
			target_.getModelFigure().detachInput(connectionFigure_);
			connectionFigure_.dispose();
//			sourceAnchor_.detach(connectionFigure_);
//			targetAnchor_.detach(connectionFigure_);
			sourceAnchor_.dispose();
			targetAnchor_.dispose();
			source_ = null;
			target_ = null;
			sourceAnchor_ = null;
			targetAnchor_ = null;
			connectionFigure_ = null;
		}
	}

	public ConnectionLayer getConnectionLayer() {
		return connectionLayer_;
	}

	public void setConnectionLayer(ConnectionLayer connectionLayer) {
		connectionLayer_ = connectionLayer;
	}

	public Command getCommand(Request r) {
		if (DELETE_REQUEST.equals(r)) {
			return new DisconnectCommand(this);
		}
		return null;
	}

	public DiagramElement getFigure() {
		return (DiagramElement) connectionFigure_;
	}

	public Connection getConnectionFigure() {
		return connectionFigure_;
	}

	public ModelEditPart getSource() {
		return source_;
	}

	public ModelEditPart getTarget() {
		return target_;
	}

	public boolean isAttached() {
		return attached_;
	}

	public void setAttached(boolean attached) {
		attached_ = attached;
	}

	public EditDomain getEditDomain() {
		return editDomain_;
	}

	public void setEditDomain(EditDomain editDomain) {
		editDomain_ = editDomain;
	}

}
