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
	
	private boolean vetoed_ = false;
	private ModelElement source_;
	private int type_;

	public ModelEvent(){}
	
	public ModelEvent(ModelElement source, int type) {
		super();
		source_ = source;
		type_ = type;
	}

	public ModelElement getSource() {
		return source_;
	}

	public void setSource(ModelElement source) {
		source_ = source;
	}

	public int getType() {
		return type_;
	}

	public void setType(int type) {
		type_ = type;
	}

	public void dispatch(List<ModelListener> listeners){
		for(ModelListener l : listeners)
			dispatchImpl(l);
	}
	
	public void veto(){
		vetoed_ = true;
	}
	
	public boolean vetoed(){
		return vetoed_;
	}
	
	public abstract void dispatchImpl(ModelListener l);
}
