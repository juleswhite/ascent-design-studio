package org.gems.ajax.client.model.event;

import java.util.HashMap;
import java.util.Map;

import org.gems.ajax.client.model.ModelElement;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.ModelRegistry;
import org.gems.ajax.client.model.resources.ModelResource;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ModelEventMarshaller {

	private static final String CONNECTION_OBJECT_KEY = "connection_object";
	public static final String INSTANTIATED_ELEMENT_ID_KEY = "instantiated_element_id";
	public static final String INSTANTIATION_MODEL_TYPE_KEY = "instantiation_model_type";
	public static final String INSTANTIATION_ELEMENT_TYPE_KEY = "instantiation_element_type";
	public static final String SOURCE_CLIENT_KEY = "source_client";
	public static final String PROPOSAL_KEY = "proposal";
	public static final String RESOURCE_KEY = "resource";
	public static final String SENDER_KEY = "sender";
	public static final String SOURCE_KEY = "source";
	public static final String TYPE_KEY = "type";
	public static final String VETOED_KEY = "vetoed";
	public static final String NEW_VALUE_KEY = "new_value";
	public static final String OLD_VALUE_KEY = "old_value";
	public static final String PROPERTY_KEY = "property";
	public static final String TARGET_KEY = "target";
	public static final String CHILD_KEY = "child";

	private ModelHelper modelHelper_;

	public ModelEventMarshaller(ModelHelper modelHelper) {
		super();
		modelHelper_ = modelHelper;
	}

	public Map<String, String> marshall(ModelResourceEvent res) {
		Map<String, String> data = toMap(res.getEvent());
		data.put(SENDER_KEY, modelHelper_.getId(res.getSender()));
		data.put(RESOURCE_KEY, res.getResource().getUri());
		data.put(PROPOSAL_KEY, "" + (res.getEvent() instanceof ProposedEvent));
		data.put(SOURCE_CLIENT_KEY, res.getSourceClient());
		return data;
	}

	public ModelResourceEvent unmarshall(Map<String, String> map) {
		ModelEvent evt = fromMap(map);
		ModelElement sender = getModelObject(SENDER_KEY, map);
		ModelResource res = new ModelResource(map.get(RESOURCE_KEY));
		ModelResourceEvent mre = new ModelResourceEvent(evt, res, sender);
		mre.setSourceClient(map.get(SOURCE_CLIENT_KEY));
		return mre;
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
			map.put(CONNECTION_OBJECT_KEY, modelHelper_
					.getId(((ConnectionEvent) evt).getConnection()));
			map.put(TARGET_KEY, modelHelper_.getId(((ConnectionEvent) evt)
					.getTarget()));
			break;
		case ModelEvent.CONNECTION_REMOVED:
			map.put(CONNECTION_OBJECT_KEY, modelHelper_
					.getId(((ConnectionEvent) evt).getConnection()));
			map.put(TARGET_KEY, modelHelper_.getId(((ConnectionEvent) evt)
					.getTarget()));
			break;
		case ModelEvent.PROPERTY_CHANGED:
			map.put(PROPERTY_KEY, ((PropertyEvent) evt).getPropertyName());
			map.put(OLD_VALUE_KEY, ((PropertyEvent) evt).getOldValue());
			map.put(NEW_VALUE_KEY, ((PropertyEvent) evt).getNewValue());
			break;
		case ModelEvent.INSTANTIATION:
			InstantiationEvent ie = (InstantiationEvent) evt;
			map.put(INSTANTIATION_ELEMENT_TYPE_KEY, ie.getTypeName());
			map.put(INSTANTIATION_MODEL_TYPE_KEY, ie.getModelType());
			map.put(INSTANTIATED_ELEMENT_ID_KEY, ie.getElementId());
			break;
		}

		return map;
	}

	public boolean getBoolean(String key, Map<String, String> vals) {
		String v = vals.get(key);
		try {
			return Boolean.parseBoolean(v);
		} catch (Exception e) {
			return false;
		}
	}

	public int getInt(String key, Map<String, String> vals) {
		String v = vals.get(key);
		try {
			return Integer.parseInt(v);
		} catch (Exception e) {
			return -1;
		}
	}

	public ModelElement getModelObject(String key, Map<String, String> vals) {
		String v = vals.get(key);
		try {
			ModelElement obj = ModelRegistry.getInstance().get(v);
			return obj;
		} catch (Exception e) {
			return null;
		}
	}

	public ModelEvent fromMap(Map<String, String> evt) {

		boolean vetoed = getBoolean(VETOED_KEY, evt);
		int type = getInt(TYPE_KEY, evt);
		ModelElement src = getModelObject(SOURCE_KEY, evt);
		boolean proposal = getBoolean(PROPOSAL_KEY, evt);

		ModelEvent mevt = null;

		switch (type) {

		case ModelEvent.CHILD_ADDED:
			if (!proposal)
				mevt = new ContainmentEvent(src,
						getModelObject(CHILD_KEY, evt), true);
			else
				mevt = new ProposedContainmentEvent(src, getModelObject(
						CHILD_KEY, evt), true);
			break;
		case ModelEvent.CHILD_REMOVED:
			if (!proposal)
				mevt = new ContainmentEvent(src,
						getModelObject(CHILD_KEY, evt), false);
			else
				mevt = new ProposedContainmentEvent(src, getModelObject(
						CHILD_KEY, evt), false);
			break;
		case ModelEvent.CONNECTION_ADDED:
			if (!proposal)
				mevt = new ConnectionEvent(src,
						getModelObject(TARGET_KEY, evt), getModelObject(
								CONNECTION_OBJECT_KEY, evt), true);
			else
				mevt = new ProposedConnectionEvent(src, getModelObject(
						TARGET_KEY, evt), getModelObject(CONNECTION_OBJECT_KEY,
						evt), true);
			break;
		case ModelEvent.CONNECTION_REMOVED:
			if (!proposal)
				mevt = new ConnectionEvent(src,
						getModelObject(TARGET_KEY, evt), getModelObject(
								CONNECTION_OBJECT_KEY, evt), false);
			else
				mevt = new ProposedConnectionEvent(src, getModelObject(
						TARGET_KEY, evt), getModelObject(CONNECTION_OBJECT_KEY,
						evt), false);
			break;
		case ModelEvent.PROPERTY_CHANGED:
			String pname = evt.get(PROPERTY_KEY);
			String nval = evt.get(NEW_VALUE_KEY);
			String oval = evt.get(OLD_VALUE_KEY);

			if (!proposal)
				mevt = new PropertyEvent(src, pname, oval, nval);
			else
				mevt = new ProposedPropertyEvent(src, pname, oval, nval);

			break;
		case ModelEvent.INSTANTIATION:
			String mtype = evt.get(INSTANTIATION_MODEL_TYPE_KEY);
			String etype = evt.get(INSTANTIATION_ELEMENT_TYPE_KEY);
			String eid = evt.get(INSTANTIATED_ELEMENT_ID_KEY);
			mevt = new InstantiationEvent(mtype, etype, eid);
			break;
		}

		if (vetoed)
			mevt.veto();

		return mevt;
	}
}
