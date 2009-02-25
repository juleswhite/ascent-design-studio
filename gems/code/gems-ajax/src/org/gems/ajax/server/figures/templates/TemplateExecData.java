package org.gems.ajax.server.figures.templates;

import org.gems.ajax.client.figures.templates.TemplateData;
import org.gems.ajax.client.model.ClientModelObject;

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
public class TemplateExecData extends TemplateData{

	private ClientModelObject modelObject_;
	private Object serverModelObject_;
	
	public TemplateExecData(TemplateData td){
		putAll(td.getData());
	}
	
	public ClientModelObject getClientModelObject() {
		return modelObject_;
	}

	public void setClientModelObject(ClientModelObject modelObject) {
		modelObject_ = modelObject;
	}

	public Object getServerModelObject() {
		return serverModelObject_;
	}

	public void setServerModelObject(Object serverModelObject) {
		serverModelObject_ = serverModelObject;
	}

	
}
