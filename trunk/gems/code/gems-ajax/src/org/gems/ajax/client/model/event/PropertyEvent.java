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

public class PropertyEvent extends ModelEvent {

	private String propertyName_;
	private Object oldValue_;
	private Object newValue_;
	
	public PropertyEvent(Object source, String propertyname, Object oldvalue, Object newvalue) {
		super(source, PROPERTY_CHANGED);
		propertyName_ = propertyname;
		oldValue_ = oldvalue;
		newValue_=  newvalue;
	}

	public String getPropertyName() {
		return propertyName_;
	}

	public void setPropertyName(String propertyName) {
		propertyName_ = propertyName;
	}

	public Object getOldValue() {
		return oldValue_;
	}

	public void setOldValue(Object oldValue) {
		oldValue_ = oldValue;
	}

	public Object getNewValue() {
		return newValue_;
	}

	public void setNewValue(Object newValue) {
		newValue_ = newValue;
	}

	public void dispatchImpl(ModelListener l) {
		l.propertyChanged(this);
	}
	
	

}
