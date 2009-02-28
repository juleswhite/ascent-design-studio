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

public class PropertyEvent extends ModelEvent {

	private String propertyName_;
	private String oldValue_;
	private String newValue_;
	
	public PropertyEvent(){}
	
	public PropertyEvent(ModelElement source, String propertyname, String oldvalue, String newvalue) {
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

	public String getOldValue() {
		return oldValue_;
	}

	public void setOldValue(String oldValue) {
		oldValue_ = oldValue;
	}

	public String getNewValue() {
		return newValue_;
	}

	public void setNewValue(String newValue) {
		newValue_ = newValue;
	}

	public void dispatchImpl(ModelListener l) {
		l.propertyChanged(this);
	}
	
	

}
