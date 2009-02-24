package org.gems.ajax.client.model;

import java.io.Serializable;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/

public class Property implements Serializable, PropertyConstants {
	
	private MetaProperty type_;
	private String value_;

	public Property() {
		type_ = new MetaProperty(null,null);
	}

	public Property(String name, String type, Object value) {
		super();
		type_ = new MetaProperty(name,type);
		setValue(value);
	}
	
	public Property(MetaProperty type, Object value) {
		super();
		type_ = type;
		setValue(value);
	}

	public String getType() {
		return type_.getType();
	}

	public void setType(String type) {
		type_.setType(type);
	}
	
	public void setType(MetaProperty type) {
		type_ = type;
	}

	public Object getValue() {
		return value_;
	}

	public void setValue(Object value) {
		value_ = ""+value;
	}

	public String getName() {
		return type_.getName();
	}

	public void setName(String name) {
		type_.setName(name);
	}

}
