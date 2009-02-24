/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.gems.ajax.client.model;

import java.io.Serializable;

public class Type implements Serializable{

	private String name_;

	public Type(String name) {
		super();
		name_ = name;
	}

	public String getName() {
		return name_;
	}

	public void setName(String name) {
		name_ = name;
	}
	
	public String toString(){
		return getName();
	}

	public boolean equals(Object obj) {
		if(obj instanceof Type)
			return ((Type)obj).name_.equals(name_);
		return super.equals(obj);
	}

	public int hashCode() {
		return name_.hashCode();
	}
	
	
}
