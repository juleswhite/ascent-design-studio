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
		MetaType t = getTypeForName(modeltype, type);
		if (t == null) {
			t = new MetaType(getOrCreateModelTypeForName(modeltype), type);
			metaTypeMap_.put(getKey(modeltype, type), t);
		}
		return t;
	}

	public static MetaType getOrCreateAssocTypeForName(String modeltype,
			String type, MetaType srctype, MetaType trgtype) {
		MetaAssociation t = (MetaAssociation) getTypeForName(modeltype, type);
		if (t == null) {
			t = new MetaAssociation(getOrCreateModelTypeForName(modeltype),
					type, srctype, trgtype);
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

	public static String getKey(String modeltype, String metatype) {
		return modeltype + "::" + metatype;
	}

	public MetaType getOrCreateMetaType(String basetype, String mtype,
			String name) {

		if (MetaType.META_TYPE_ID.equals(basetype)) {
			return getOrCreateTypeForName(mtype, name);
		} else {
			return null;
		}
	}

	public Type getOrCreateTypeFromFullName(String fullname) {
		Type t = null;

		if (fullname != null) {
			String[] parts = fullname.split(Type.NAME_PART_SEPARATOR);
			if (parts.length == 3) {
				String basetype = parts[0];
				String mtype = parts[1];
				String typename = parts[2];

				if (MetaType.META_TYPE_ID.equals(basetype)) {
					t = getOrCreateMetaType(basetype, mtype, typename);
				}
			} else if (parts.length == 9) {
				String basetype = parts[0];
				String mtype = parts[1];
				String typename = parts[2];

				String sbasetype = parts[3];
				String smtype = parts[4];
				String sname = parts[5];

				String tbasetype = parts[6];
				String tmtype = parts[7];
				String tname = parts[8];

				if (MetaAssociation.META_ASSOCIATION_ID.equals(basetype)) {
					t = getOrCreateAssocTypeForName(mtype, typename,
							getOrCreateMetaType(sbasetype, smtype, sname),
							getOrCreateMetaType(tbasetype, tmtype, tname));
				}
			} else if (parts.length == 1) {
				t = getOrCreateModelTypeForName(parts[0]);
			}
		}

		return t;
	}
}
