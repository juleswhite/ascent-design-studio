package org.gems.ajax.server.figures.templates;

import org.gems.ajax.client.figures.templates.TemplateData;
import org.gems.ajax.client.figures.templates.TemplateManager;
import org.gems.ajax.client.figures.templates.TemplateUpdaterInfo;
import org.gems.ajax.server.util.ServerUtils;
import org.springframework.context.ApplicationContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class TemplateManagerImpl extends RemoteServiceServlet implements
		TemplateManager {

	public static final String SERVER_TEMPLATE_MANAGER_BEAN = "serverTemplateManager";

	private ServerTemplateManager delegate_;

	public TemplateManagerImpl() {

	}

	public ServerTemplateManager getServerTemplateManager() {
		if (delegate_ == null) {
			ApplicationContext ctx = ServerUtils.getInstance()
					.getServerApplicationContext();
			delegate_ = (ServerTemplateManager) ctx
					.getBean(SERVER_TEMPLATE_MANAGER_BEAN);
		}
		return delegate_;
	}

	public String getTemplate(String id) {
		return getServerTemplateManager().getTemplate(id);
	}

	public TemplateUpdaterInfo getTemplateUpdaterInfo(String viewkey, String id) {
		return getServerTemplateManager().getTemplateUpdaterInfo(viewkey, id);
	}

	public String updateTemplate(String id, TemplateData data) {
		return getServerTemplateManager().updateTemplate(id, data);
	}

}
