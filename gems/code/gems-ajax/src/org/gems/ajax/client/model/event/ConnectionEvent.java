package org.gems.ajax.client.model.event;

import org.gems.ajax.client.model.ModelElement;


/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class ConnectionEvent extends ModelEvent {

	private Object target_;

	public ConnectionEvent(){}
	
	public ConnectionEvent(ModelElement source, ModelElement target, boolean add) {
		super(source, CONNECTION_ADDED);
		target_ = target;
		if (!add)
			setType(CONNECTION_REMOVED);
	}

	public Object getTarget() {
		return target_;
	}

	public void setTarget(Object target) {
		target_ = target;
	}

	public boolean isAdd() {
		return getType() == CONNECTION_ADDED;
	}

	
	public void dispatchImpl(ModelListener l) {
		if(isAdd())
			l.connectionAdded(this);
		else
			l.connectionRemoved(this);
	}

	
	
}
