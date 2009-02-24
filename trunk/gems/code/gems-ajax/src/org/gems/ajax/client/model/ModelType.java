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
public class ModelType implements Serializable{
	
	private String name_;

	public ModelType() {
	}

	public ModelType(String name) {
		super();
		name_ = name;
	}

	public String getName() {
		return name_;
	}

	public void setName(String name) {
		name_ = name;
	}

}
