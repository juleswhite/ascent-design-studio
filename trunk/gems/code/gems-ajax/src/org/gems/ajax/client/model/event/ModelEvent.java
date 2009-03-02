package org.gems.ajax.client.model.event;

import java.io.Serializable;
import java.util.List;

import org.gems.ajax.client.model.ModelElement;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public abstract class ModelEvent implements Serializable{
	
	public static final int CHILD_ADDED = 1;
	public static final int CHILD_REMOVED = 2;
	public static final int CONNECTION_ADDED = 3;
	public static final int CONNECTION_REMOVED = 4;
	public static final int PROPERTY_CHANGED = 5;
	public static final int INSTANTIATION = 6;
	
	private boolean vetoed_ = false;
	private ModelElement source_;
	private int type_;

	public ModelEvent(){}
	
	public ModelEvent(ModelElement source, int type) {
		super();
		source_ = source;
		type_ = type;
	}

	/**
	 * The model element that fired the event.
	 * @return
	 */
	public ModelElement getSource() {
		return source_;
	}

	/**
	 * Sets the model element that fired the event.
	 * @return
	 */
	public void setSource(ModelElement source) {
		source_ = source;
	}

	/**
	 * The type of event.
	 * @return
	 */
	public int getType() {
		return type_;
	}

	public void setType(int type) {
		type_ = type;
	}

	/**
	 * Dispatch the event to all of the listeners
	 * in the provided list. 
	 * @param listeners
	 */
	public void dispatch(List<ModelListener> listeners){
		for(ModelListener l : listeners)
			dispatchImpl(l);
	}
	
	/**
	 * Prevent the event from being applied.
	 * This method only works on events that
	 * implement the ProposedEvent interface.
	 */
	public void veto(){
		vetoed_ = true;
	}
	
	/**
	 * Has this event been vetoed.
	 * 
	 * @return
	 */
	public boolean vetoed(){
		return vetoed_;
	}
	
	/**
	 * This method should dispatch the event to the
	 * appropriate method on the provided listener.
	 * 
	 * @param l
	 */
	public abstract void dispatchImpl(ModelListener l);
}
