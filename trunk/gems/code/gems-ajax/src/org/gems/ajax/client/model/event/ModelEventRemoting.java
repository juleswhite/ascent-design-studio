package org.gems.ajax.client.model.event;

import java.util.Map;

import org.gems.ajax.client.model.ModelElement;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.resources.ModelResource;
import org.gems.ajax.client.util.dojo.CometCallback;
import org.gems.ajax.client.util.dojo.CometMessage;
import org.gems.ajax.client.util.dojo.DojoUtil;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ModelEventRemoting implements ModelResourceListener, CometCallback {

	public static final String MODEL_EVENT_CHANNEL = "/model";
	public static final String COMETD_PATH = "/cometd";
	
	
	private boolean sendVetoedEvents_ = false;
	private ModelEventMarshaller marshaller_;
	private ModelEventReplay replayer_;

	public ModelEventRemoting(ModelHelper modelHelper) {
		super();
		marshaller_ = new ModelEventMarshaller(modelHelper);
		replayer_ = new ModelEventReplay(modelHelper);
	}

	public void start(String host) {

		if (host.endsWith("/"))
			host = host.substring(0, host.length() - 1);

		String path = host + COMETD_PATH;

		DojoUtil.connectToCometdHost(path);
		DojoUtil.subscribeToChannel(MODEL_EVENT_CHANNEL, this);

		EventDispatcher.get().getPostDispatchResourceListeners().add(this);
	}

	public void stop() {

	}

	public void resourceChanged(ModelResourceEvent evt) {
		if (!evt.getEvent().vetoed() || sendVetoedEvents_) {
			evt.setSourceClient(DojoUtil.getCometClientId());
			Map<String, String> data = marshaller_.marshall(evt);
			DojoUtil.publishToChannel(MODEL_EVENT_CHANNEL, data);
		}
	}

	public void recv(CometMessage data) {
		Map<String,String> map = data.asMap();
		ModelResourceEvent revt = marshaller_.unmarshall(map);
		ModelEvent evt = revt.getEvent();
		ModelElement sender = revt.getSender();
		ModelResource res = revt.getResource();
		if(!DojoUtil.getCometClientId().equals(revt.getSourceClient())){
			replayer_.replay(revt);
		}
		else {
			
		}
	}

	
}
