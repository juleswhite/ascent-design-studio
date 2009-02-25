package org.gems.ajax.client.figures.templates;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.gems.ajax.client.model.ClientModelObject;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class TemplateData implements Serializable {

	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String OBJECT_ID = "id";

	private HashMap<String, String> data_ = new HashMap<String, String>();

	public void setSize(int w, int h) {
		data_.put(WIDTH, "" + w + "px");
		data_.put(HEIGHT, "" + h + "px");
	}

	public void setSize(String w, String h) {
		data_.put(WIDTH, w);
		data_.put(HEIGHT, h);
	}

	public void setObjectId(String id) {
		data_.put(OBJECT_ID, id);
	}

	public String getObjectId() {
		return data_.get(OBJECT_ID);
	}

	public boolean containsKey(Object key) {
		return data_.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return data_.containsValue(value);
	}

	public Set<Entry<String, String>> entrySet() {
		return data_.entrySet();
	}

	public String get(Object key) {
		return data_.get(key);
	}

	public boolean isEmpty() {
		return data_.isEmpty();
	}

	public Set<String> keySet() {
		return data_.keySet();
	}

	public String put(String key, String value) {
		return data_.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends String> m) {
		data_.putAll(m);
	}

	public String remove(Object key) {
		return data_.remove(key);
	}

	public int size() {
		return data_.size();
	}

	public Collection<String> values() {
		return data_.values();
	}

	public HashMap<String, String> getData() {
		return data_;
	}

	public void setData(HashMap<String, String> data) {
		data_ = data;
	}

}
