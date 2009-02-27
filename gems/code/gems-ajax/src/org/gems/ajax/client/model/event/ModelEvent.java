package org.gems.ajax.client.model.event;

import java.util.List;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public abstract class ModelEvent {
	
	public static int CHILD_ADDED = 1;
	public static int CHILD_REMOVED = 2;
	public static int CONNECTION_ADDED = 3;
	public static int CONNECTION_REMOVED = 4;
	public static int PROPERTY_CHANGED = 5;
	
	private boolean vetoed_ = false;
	private Object source_;
	private int type_;

	public ModelEvent(Object source, int type) {
		super();
		source_ = source;
	}

	public Object getSource() {
		return source_;
	}

	public void setSource(Object source) {
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
