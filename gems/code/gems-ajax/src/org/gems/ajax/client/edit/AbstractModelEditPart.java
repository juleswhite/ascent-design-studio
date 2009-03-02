package org.gems.ajax.client.edit;

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

import org.gems.ajax.client.edit.cmd.ConnectCommand;
import org.gems.ajax.client.edit.cmd.DeleteCommand;
import org.gems.ajax.client.figures.DiagramPanel;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.Type;
import org.gems.ajax.client.model.event.ConnectionEvent;
import org.gems.ajax.client.model.event.ContainmentEvent;
import org.gems.ajax.client.model.event.PropertyEvent;
import org.gems.ajax.client.model.resources.ModelResource;

public abstract class AbstractModelEditPart extends AbstractEditPart implements
		ModelEditPart, EditConstants {

	private List<String> currentTags_ = new ArrayList<String>();
	private List<EditPart> children_ = new ArrayList<EditPart>();
	private ModelEditPart parent_;
	private EditDomain editDomain_;

	public AbstractModelEditPart(ModelHelper modelHelper,
			EditPartFactory factory, Object model) {
		super(modelHelper, factory, model);
	}

	public Command getCommand(Request r) {
		if (DELETE_REQUEST.equals(r)) {
			return new DeleteCommand(getModelHelper(), this);
		}
		else if(ADD_CONNECTION_REQUEST.equals(r)){
			ConnectCommand c = new ConnectCommand(getModelHelper(),getFactory());
			c.setSource(this);
			return c;
		}
		return null;
	}
	
	public void updateTags(){
		List<String> tags = getModelHelper().getTags(getModel());
		ArrayList<String> toadd = new ArrayList<String>();
		ArrayList<String> toremove = new ArrayList<String>(currentTags_.size());
		toremove.addAll(currentTags_);
		
		if(tags != null){		
			toadd.addAll(tags);
			toremove.removeAll(tags);
		}
		
		for(String t : toremove){
			((DiagramPanel)getModelFigure()).removeGlobalStyleDependentName(t);
		}
		
		toadd.removeAll(currentTags_);
		for(String t : toadd){
			((DiagramPanel)getModelFigure()).addGlobalStyleDependentName(t);
		}
	}

	public ModelEditPart getParent() {
		return parent_;
	}

	public void setParent(ModelEditPart parent) {
		parent_ = parent;
	}

	public List<EditPart> getChildren() {
		return children_;
	}

	public void addChild(EditPart child) {
		child.setParent(this);
		children_.add(child);
		getModelHelper().addChild(getModel(), child.getModel());
	}

	public void removeChild(EditPart child) {
		child.setParent(null);
		children_.remove(child);
		getModelHelper().removeChild(getModel(), child.getModel());
	}

	public void connectTo(ModelEditPart target, String assoctype) {
		ModelResource res = getModelHelper().getContainingResource(getModel());
		Type[] t = getModelHelper().getTypes(getModel());
		Type mtype = getModelHelper().getModelType(t[0]);
		Type atype = getModelHelper().getTypeForName(mtype.getName(), assoctype);
		Object chandle = getModelHelper().createAssociation(res,atype);
		ConnectionEditPart cep = getFactory().createConnectionEditPart(getView(),chandle);
		cep.setConnectionLayer(getModelFigure().getDiagram()
				.getConnectionLayer());
		cep.attach(this, target);
	}

	public EditDomain getEditDomain() {
		return editDomain_;
	}

	public void setEditDomain(EditDomain editDomain) {
		editDomain_ = editDomain;
	}

	public void childAdded(ContainmentEvent evt) {
	}

	public void childRemoved(ContainmentEvent evt) {
	}

	public void connectionAdded(ConnectionEvent evt) {
	}

	public void connectionRemoved(ConnectionEvent evt) {
	}

	public void propertyChanged(PropertyEvent evt) {
	}

}