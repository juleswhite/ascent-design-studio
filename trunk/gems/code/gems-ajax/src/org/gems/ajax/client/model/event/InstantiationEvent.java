package org.gems.ajax.client.model.event;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class InstantiationEvent extends ModelEvent {

	private String typeName_;
	private String elementId_;

	public InstantiationEvent() {
		super();
		setType(INSTANTIATION);
	}

	public InstantiationEvent(String typename, String eid) {
		super(null, INSTANTIATION);
		typeName_ = typename;
		elementId_ = eid;
	}

	public String getTypeName() {
		return typeName_;
	}

	public void setTypeName(String typeName) {
		typeName_ = typeName;
	}

	public String getElementId() {
		return elementId_;
	}

	public void setElementId(String elementId) {
		elementId_ = elementId;
	}

	/**
	 * This method does nothing. This type of event should be dispatched
	 * directly through the EventDispatcher.
	 */
	public void dispatchImpl(ModelListener l) {
	}
}
