package org.gems.ajax.client.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
public class ModelRegistry {
	
	private static ModelRegistry instance_ = new ModelRegistry();
	
	public static ModelRegistry getInstance(){
		return instance_;
	}
	
	private Map<String, ClientModelObject> mapping_ = new HashMap<String, ClientModelObject>();
	
	public void add(ClientModelObject mo){
		mapping_.put(mo.getId(),mo);
	}
	
	public void remove(ClientModelObject mo){
		mapping_.remove(mo.getId());
	}

	public ClientModelObject get(String key) {
		return mapping_.get(key);
	}

	public boolean isEmpty() {
		return mapping_.isEmpty();
	}

	public Set<String> keySet() {
		return mapping_.keySet();
	}

	public ClientModelObject put(String key, ClientModelObject value) {
		return mapping_.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends ClientModelObject> m) {
		mapping_.putAll(m);
	}

	public int size() {
		return mapping_.size();
	}
	
	 
}
