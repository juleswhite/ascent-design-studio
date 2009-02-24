package org.gems.ajax.client.model;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ModelLoader extends RemoteService{

	public ClientModelObject loadModel(String id);
	
}
