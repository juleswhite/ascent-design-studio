package org.gems.ajax.client.model.resources;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ModelParameterRef extends ModelResource {
	private String parameterRef_;

	public ModelParameterRef(){}
	
	public ModelParameterRef(String parameterRef) {
		super();
		parameterRef_ = parameterRef;
	}

	public String getParameterRef() {
		return parameterRef_;
	}

	public void setParameterRef(String parameterRef) {
		parameterRef_ = parameterRef;
	}

}