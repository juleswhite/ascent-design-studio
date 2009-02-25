package org.gems.ajax.server.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.gems.ajax.client.model.ClientModelObject;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ClientServerModelMapping {

	private static ClientServerModelMapping instance_ = new ClientServerModelMapping();
	
	public static ClientServerModelMapping get(){
		return instance_;
	}
	
	private Map<ClientModelObject, Object> serverModelLookup_ = new HashMap<ClientModelObject, Object>();
	private Map<Object, ClientModelObject> clientModelLookup_ = new HashMap<Object, ClientModelObject>();

	private ClientServerModelMapping() {
	}

	public Set<ClientModelObject> serverKeySet() {
		return serverModelLookup_.keySet();
	}

	public Set<Object> clientKeySet() {
		return clientModelLookup_.keySet();
	}

	public Object put(ClientModelObject key, Object value) {
		return serverModelLookup_.put(key, value);
	}

	public Object put(Object key, ClientModelObject value) {
		return clientModelLookup_.put(key, value);
	}

	public Object removeServerObject(ClientModelObject key) {
		return serverModelLookup_.remove(key);
	}

	public Object removeClientObject(Object key) {
		return clientModelLookup_.remove(key);
	}

	public ClientModelObject getClientObject(Object key) {
		return clientModelLookup_.get(key);
	}

	public Object getServerObject(ClientModelObject key) {
		return serverModelLookup_.get(key);
	}

}
