package org.gems.ajax.client.model;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ModelLoaderAsync {
	public void loadModel(String id, AsyncCallback<ClientModelObject> callback);
}
