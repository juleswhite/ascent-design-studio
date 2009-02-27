package org.gems.ajax.client.model;

import java.io.Serializable;
import java.util.List;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public interface ModelHelper extends Serializable{

	/**
	 * Returns the unique id associated with
	 * the specified model object.
	 * @param o - the object to get the id for
	 * @return the id
	 */
	public String getId(Object o);
	
	
	public Type getTypeForName(String modeltype, String name);	
	/**
	 * This method creates a clone/copy of
	 * the model object passed in.
	 * 
	 * @param o the element to clone
	 * @return a clone of the element
	 */
	public Object clone(Object o);
	
	/**
	 * This method returns the name of the type of a model object.
	 * The list of types should be sorted so that the most specific
	 * types are at the front (i.e. lower indexes) of the list. The
	 * exact type of the object should be at index 0.
	 * @param o
	 * @return
	 */
	public Type[] getTypes(Object o);
	
	/**
	 * Tags are a way of marking model elements to
	 * distinguish them from each other. This method
	 * should return a list of all the tag names 
	 * associated with an element. The default 
	 * EditParts will add a CSS class for each type
	 * to the rendering figure that follows the naming
	 * convention: FIGURE_TYPE-TAG. For a standard
	 * DiagramPanelFigure, with tags returned: [foo, bar],
	 * GEMS will add the following CSS class names to
	 * the diagram's body panel (and all other
	 * constituent parts (e.g. header, etc.) 
	 * IN THIS ORDER: gems-body-panel-foo,
	 * gems-body-panel-bar. So, the tags should
	 * be ordered so that they are sorted in
	 * increasing precedence (more important
	 * stuff at the end).
	 * @param o
	 * @return
	 */
	public List<String> getTags(Object o);

	/**
	 * This method attaches a listener to the specified object so that it will
	 * send events to the provided listener.
	 * 
	 * @param o
	 * @param l
	 */
	public void addListener(Object o, ModelListener l);

	/**
	 * This method removes a listener from the specified object so that it will
	 * not send events to the provided listener.
	 * 
	 * @param o
	 * @param l
	 */
	public void removeListener(Object o, ModelListener l);

	/**
	 * This method returns a human readable label for the model element. If no
	 * label is desired, the method will return null.
	 * 
	 * @param model
	 * @return
	 */
	public String getLabel(Object model);

	/**
	 * This method returns the parent of a given model element.
	 * 
	 * @param child
	 * @return the child's parent
	 */
	public Object getParent(Object child);

	/**
	 * This method returns a list containing the children of the specified model
	 * element.
	 * 
	 * @param model
	 * @return
	 */
	public List getChildren(Object model);
	
	/**
	 * This method returns the associations (connections) that involved
	 * the specified model object.
	 * @param model
	 * @return the list of association handles
	 */
	public List getConnections(Object model);

	/**
	 * This method returns true if the specified child can be added to the
	 * parent.
	 * 
	 * @param parent
	 * @param child
	 * @return
	 */
	public boolean canAddChild(Object parent, Object child);

	/**
	 * This method adds the specified child to the parent.
	 * 
	 * @param parent
	 * @param child
	 */
	public void addChild(Object parent, Object child);

	/**
	 * This method returns true if the specified child can be removed from the
	 * parent.
	 * 
	 * @param parent
	 * @param child
	 * @return
	 */
	public boolean canRemoveChild(Object parent, Object child);

	/**
	 * This method removes the specified child from the parent.
	 * 
	 * @param parent
	 * @param child
	 */
	public void removeChild(Object parent, Object child);

	/**
	 * This method returns a list of all properties associated with the
	 * specified model element.
	 * 
	 * @param model
	 * @return
	 */
	public List<Property> getProperties(Object model);

	/**
	 * This method returns the value of the specified property on the given
	 * model element.
	 * 
	 * @param model
	 * @param property
	 * @return
	 */
	public Object getProperty(Object model, String property);

	/**
	 * This method checks to see if the provided property value is a valid value
	 * that can be set on the specified model element.
	 * 
	 * @param model
	 * @param property
	 * @param value
	 * @return
	 */
	public String validatePropertyValue(Object model, String property,
			Object value);

	/**
	 * This method sets the value of the specified property on the model element
	 * to the passed in value.
	 * 
	 * @param model
	 * @param property
	 * @param value
	 */
	public void setProperty(Object model, String property, Object value);

	/**
	 * This method returns true if a connection of type assoctype can be created
	 * from the specified src element to the trg element.
	 * 
	 * @param src
	 * @param trg
	 * @param assoctype
	 * @return
	 */
	public boolean canConnect(Object src, Object trg, Object assoctype);

	/**
	 * This method returns a list of the String identifiers of the association
	 * types that can be created between two model elements.
	 * 
	 * @param src
	 * @param trg
	 * @return
	 */
	public List getAssociationTypes(Object src, Object trg);

	/**
	 * This method creates a connection of type assoctype between the specified
	 * model elements and returns a handle that can be used to refer to the
	 * connection. The handle may be another model element that itself contains
	 * properties, etc.
	 * 
	 * @param src
	 * @param trg
	 * @param assoctype
	 * @return
	 */
	public void connect(Object src, Object trg, Object assoc);

	/**
	 * Creates an object representing the specified association.
	 * 
	 * @param assoctype
	 *            the type of association object to create
	 * @return the new association object
	 */
	public Object createAssociation(Object assoctype);
	
	/**
	 * Returns the object that is the source
	 * of the provided association.
	 * @param assoc - the association object
	 * @return the source of the association
	 */
	public Object getSource(Object assoc);
	
	/**
	 * Returns the object that is the target
	 * of the specified association.
	 * @param assoc - the association object
	 * @return the target of the association
	 */
	public Object getTarget(Object assoc);

	/**
	 * This method removes the connection between the src and trg objects
	 * represented by the given association handle.
	 * 
	 * @param src
	 * @param trg
	 * @param assochandle
	 */
	public void disconnect(Object src, Object trg, Object assochandle);
	
	/**
	 * This method creates a new instance of the provided
	 * type.
	 * 
	 * @param type to create an instance of
	 * @return the new instance of the type
	 */
	public Object createInstance(Object type);
	
	/**
	 * This method returns the types that can be
	 * children of instances of the provided type.
	 * 
	 * @param type to find valid child types for
	 * @return the list of valid child types
	 */
	public List getAllowedChildTypes(Object type);
}
