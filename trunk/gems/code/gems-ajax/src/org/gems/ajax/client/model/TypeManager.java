package org.gems.ajax.client.model;

import java.util.HashMap;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class TypeManager {

	private static HashMap<String, MetaType> metaTypeMap_ = new HashMap<String, MetaType>();
	private static HashMap<String, ModelType> modelTypeMap_ = new HashMap<String, ModelType>();

	public static MetaType getTypeForName(String modeltype, String type) {
		return metaTypeMap_.get(getKey(modeltype, type));
	}

	public static MetaType getOrCreateTypeForName(String modeltype, String type) {
		MetaType t = getTypeForName(modeltype,type);
		if (t == null) {
			t = new MetaType(getOrCreateModelTypeForName(modeltype),type);
			metaTypeMap_.put(getKey(modeltype, type), t);
		}
		return t;
	}

	public static ModelType getModelTypeForName(String type) {
		return modelTypeMap_.get(type);
	}

	public static ModelType getOrCreateModelTypeForName(String type) {
		ModelType t = getModelTypeForName(type);
		if (t == null) {
			t = new ModelType(type);
			modelTypeMap_.put(type, t);
		}
		return t;
	}
	
	public static String getKey(String modeltype, String metatype){
		return modeltype+"::"+metatype;
	}
}
