package org.gems.ajax.client.model.event;

import org.gems.ajax.client.model.ModelElement;

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

	private ModelElement child_;
	
	public ContainmentEvent(){}
	
	public ContainmentEvent(ModelElement source, ModelElement child, boolean add) {
		super(source, CHILD_ADDED);
		child_ = child;
		if(!add)
			setType(CHILD_REMOVED);
	}

	public ModelElement getParent(){
		return getSource();
	}
	
	public void setParent(ModelElement p){
		setSource(p);
	}

	public ModelElement getChild() {
		return child_;
	}

	public void setChild(ModelElement child) {
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
