package org.gems.ajax.client.model.event;

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

public class ContainmentEvent extends ModelEvent {

	private Object child_;
	
	public ContainmentEvent(Object source, Object child, boolean add) {
		super(source, CHILD_ADDED);
		child_ = child;
		if(!add)
			setType(CHILD_REMOVED);
	}

	public Object getParent(){
		return getSource();
	}
	
	public void setParent(Object p){
		setSource(p);
	}

	public Object getChild() {
		return child_;
	}

	public void setChild(Object child) {
		child_ = child;
	}
	
	public boolean isAdd(){
		return getType() == CHILD_ADDED;
	}

	public void dispatchImpl(ModelListener l) {
		if(isAdd())
			l.childAdded(this);
		else
			l.childRemoved(this);
	}
	
	
}
