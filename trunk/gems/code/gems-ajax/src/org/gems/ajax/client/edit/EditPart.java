package org.gems.ajax.client.edit;

import org.gems.ajax.client.edit.exdata.ExtendedData;
import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.resources.ModelResource;
import org.gems.ajax.client.views.View;

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

public interface EditPart {
	
	/**
	 * Disposes of the EditPart
	 * and releases any resources
	 * it holds.
	 */
	public void dispose();

	/**
	 * This method is called by the
	 * SelectionManager when the
	 * EditPart's figure is selected
	 * by the user.
	 */
	public void onSelect();
	
	/**
	 * This method is called by the
	 * SelectionManager when the
	 * EditPart's figure is deselected
	 * by the user.
	 */
	public void onDeSelect();
	
	/**
	 * This method should cause
	 * the figure managed by the
	 * EditPart to grab focus.
	 */
	public void focus();
	
	/**
	 * This method returns the ModelHelper
	 * that is being used by the EditPart.
	 * @return
	 */
	public ModelHelper getModelHelper();
	
	/**
	 * This method returns the model
	 * object that the EditPart is
	 * managing.
	 * @return
	 */
	public Object getModel();
	
	/**
	 * This method should return a 
	 * Command object that can perform
	 * the request specified by the
	 * request argument. If the
	 * EditPart does not support the
	 * request, it should return null.
	 * 
	 * @param r
	 * @return
	 */
	public Command getCommand(Request r);
	
	/**
	 * This method returns the figure
	 * that the EditPart manages.
	 * @return
	 */
	public DiagramElement getFigure();
	
	/**
	 * This method should return the
	 * factory that was used to create
	 * the EditPart.
	 * @return
	 */
	public EditPartFactory getFactory();
	
	/**
	 * This method is called to set the
	 * EditDomain that should be used by
	 * the EditPart. 
	 * @param ed
	 */
	public void setEditDomain(EditDomain ed);
	
	/**
	 * This method returns the EditDomain
	 * that is currently being used by
	 * the EditPart.
	 * @return
	 */
	public EditDomain getEditDomain();	
	
	/**
	 * This method should return the
	 * parent EditPart or null if it
	 * does not have a parent.
	 * @return
	 */
	public ModelEditPart getParent();
	
	/**
	 * This method sets the EditPart's
	 * parent EditPart.
	 * @param parent
	 */
	public void setParent(ModelEditPart parent);
	
	/**
	 * The ExtendedData for an EditPart is a data
	 * store that can be used to store non-model
	 * data to associate with a model element.
	 * A typical usage example is storing graphical
	 * information, such as layout positions,
	 * that should not be stored in the model
	 * itself. This method can be called to set
	 * the data store. If you inherit from
	 * AbstractEditPart, this method is taken
	 * care of for you.
	 * @param o
	 */
	public void setExtendedData(ExtendedData d);
	
	/**
	 * The ExtendedData for an EditPart is a data
	 * store that can be used to store non-model
	 * data to associate with a model element.
	 * A typical usage example is storing graphical
	 * information, such as layout positions,
	 * that should not be stored in the model
	 * itself. This method can be called to get
	 * the data store. If you inherit from
	 * AbstractEditPart, this method is taken
	 * care of for you.
	 * @param o
	 */
	public ExtendedData getExtendedData();
	
	/**
	 * Sets the diagram that the edit part
	 * is attached to.
	 * @param d
	 */
	public void setDiagram(GEMSDiagram d);
	
	/**
	 * Returns the diagram associated with
	 * the EditPart.
	 * @return
	 */
	public GEMSDiagram getDiagram();
	
	/**
	 * Gets the current view that this
	 * EditPart is a member of.
	 * @return
	 */
	public View getView();
	
	/**
	 * Sets the current view that this
	 * EditPart is a member of.
	 * @return
	 */
	public void setView(View v);
	
	
}
