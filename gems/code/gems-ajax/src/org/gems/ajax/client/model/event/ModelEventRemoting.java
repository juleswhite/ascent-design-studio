package org.gems.ajax.client.model.event;

import java.util.HashMap;
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

	private static final String COMETD_PATH = "/cometd";
	private static final String MODEL_EVENT_CHANNEL = "/model";
	private static final String RESOURCE_KEY = "resource";
	private static final String SENDER_KEY = "sender";
	private static final String SOURCE_KEY = "source";
	private static final String TYPE_KEY = "type";
	private static final String VETOED_KEY = "vetoed";
	private static final String NEW_VALUE_KEY = "new_value";
	private static final String OLD_VALUE_KEY = "old_value";
	private static final String PROPERTY_KEY = "property";
	private static final String TARGET_KEY = "target";
	private static final String CHILD_KEY = "child";
	
	private boolean sendVetoedEvents_ = false;
	private ModelHelper modelHelper_;

	public ModelEventRemoting(ModelHelper modelHelper) {
		super();
		modelHelper_ = modelHelper;
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

	public void resourceChanged(ModelResource res, ModelElement el,
			ModelEvent evt) {
		if (!evt.vetoed() || sendVetoedEvents_) {
			Map<String, String> data = toMap(evt);
			data.put(SENDER_KEY, modelHelper_.getId(el));
			data.put(RESOURCE_KEY, res.getUri());
			data.put("proposal", ""+(evt instanceof ProposedEvent));
			DojoUtil.publishToChannel(MODEL_EVENT_CHANNEL, data);
		}
	}

	public void recv(CometMessage data) {
		System.out.println("Incoming event -->"+data.asMap());
	}

	public Map<String, String> toMap(ModelEvent evt) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(VETOED_KEY, "" + evt.vetoed());
		map.put(TYPE_KEY, "" + evt.getType());

		if (evt.getSource() != null)
			map.put(SOURCE_KEY, modelHelper_.getId(evt.getSource()));

		switch (evt.getType()) {

		case ModelEvent.CHILD_ADDED:
			map.put(CHILD_KEY, modelHelper_.getId(((ContainmentEvent) evt)
					.getChild()));
			break;
		case ModelEvent.CHILD_REMOVED:
			map.put(CHILD_KEY, modelHelper_.getId(((ContainmentEvent) evt)
					.getChild()));
			break;
		case ModelEvent.CONNECTION_ADDED:
			map.put(TARGET_KEY, modelHelper_.getId(((ConnectionEvent) evt)
					.getTarget()));
			break;
		case ModelEvent.CONNECTION_REMOVED:
			map.put(TARGET_KEY, modelHelper_.getId(((ConnectionEvent) evt)
					.getTarget()));
			break;
		case ModelEvent.PROPERTY_CHANGED:
			map.put(PROPERTY_KEY, ((PropertyEvent) evt)
					.getPropertyName());
			map.put(OLD_VALUE_KEY,((PropertyEvent) evt)
					.getOldValue());
			map.put(NEW_VALUE_KEY, ((PropertyEvent) evt)
					.getNewValue());
			break;
		}

		return map;
	}
}
