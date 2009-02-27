package org.gems.ajax.client.model;

import java.io.Serializable;

import org.gems.ajax.client.model.resources.ModelResource;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ModelingPackage implements Serializable {
	private ModelElement rootObject_;
	private ModelResource modelResource_;
	private ModelType modelType_;
	private ModelHelper modelHelper_;

	public ModelElement getRootObject() {
		return rootObject_;
	}

	public void setRootObject(ModelElement rootObject) {
		rootObject_ = rootObject;
	}

	public ModelResource getModelResource() {
		return modelResource_;
	}

	public void setModelResource(ModelResource modelResource) {
		modelResource_ = modelResource;
	}

	public ModelType getModelType() {
		return modelType_;
	}

	public void setModelType(ModelType modelType) {
		modelType_ = modelType;
	}

	public ModelHelper getModelHelper() {
		return modelHelper_;
	}

	public void setModelHelper(ModelHelper modelHelper) {
		modelHelper_ = modelHelper;
	}

}
