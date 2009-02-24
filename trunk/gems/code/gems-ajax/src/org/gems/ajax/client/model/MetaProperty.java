package org.gems.ajax.client.model;

import java.io.Serializable;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class MetaProperty implements PropertyConstants, Serializable {
	

	private String name_;
	private String type_;

	public MetaProperty(){}
	
	public MetaProperty(String name, String type) {
		super();
		name_ = name;
		type_ = type;
	}

	public String getName() {
		return name_;
	}

	public void setName(String name) {
		name_ = name;
	}

	public String getType() {
		return type_;
	}

	public void setType(String type) {
		type_ = type;
	}

	public boolean equals(Object o) {
		return o instanceof MetaProperty
				&& ((MetaProperty) o).name_.equals(name_)
				&& ((MetaProperty) o).type_.equals(type_);
	}
	
	public Property newInstance(){
		String val = "";
		if(INT.equals(type_) || DECIMAL.equals(type_)){
			val = "0";
		}
		else if(BOOLEAN.equals(type_)){
			val = "false";
		}
		else if(ENUM.equals(type_)){
			//to do...
		}
		return new Property(name_,type_,val);
	}
	
	public String toString(){
		return "MetaProperty("+name_+","+type_+")";
	}
}
