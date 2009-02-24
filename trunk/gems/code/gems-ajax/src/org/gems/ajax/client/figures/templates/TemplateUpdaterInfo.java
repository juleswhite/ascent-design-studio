package org.gems.ajax.client.figures.templates;

import java.io.Serializable;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class TemplateUpdaterInfo implements Serializable{
	private String id_;
	private String template_;
	private boolean clientSide_;

	public TemplateUpdaterInfo() {
		super();
	}

	public TemplateUpdaterInfo(String id, String template, boolean clientSide) {
		super();
		id_ = id;
		template_ = template;
		clientSide_ = clientSide;
	}

	public String getTemplate() {
		return template_;
	}

	public void setTemplate(String template) {
		template_ = template;
	}

	public boolean isClientSide() {
		return clientSide_;
	}

	public void setClientSide(boolean clientSide) {
		clientSide_ = clientSide;
	}

	public String getId() {
		return id_;
	}

	public void setId(String id) {
		id_ = id;
	}

}
