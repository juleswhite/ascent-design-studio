package org.gems.ajax.client.model.event;

import org.gems.ajax.client.model.ModelElement;
import org.gems.ajax.client.model.resources.ModelResource;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ModelResourceEvent {
	private ModelEvent event_;
	private ModelResource resource_;
	private ModelElement sender_;
	private String sourceClient_;

	public ModelResourceEvent(ModelEvent event, ModelResource resource,
			ModelElement sender) {
		super();
		event_ = event;
		resource_ = resource;
		sender_ = sender;
	}

	public ModelEvent getEvent() {
		return event_;
	}

	public void setEvent(ModelEvent event) {
		event_ = event;
	}

	public ModelResource getResource() {
		return resource_;
	}

	public void setResource(ModelResource resource) {
		resource_ = resource;
	}

	public ModelElement getSender() {
		return sender_;
	}

	public void setSender(ModelElement sender) {
		sender_ = sender;
	}

	public String getSourceClient() {
		return sourceClient_;
	}

	public void setSourceClient(String sourceClient) {
		sourceClient_ = sourceClient;
	}

}
