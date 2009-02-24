package org.gems.ajax.client.figures.templates;


import com.google.gwt.user.client.rpc.AsyncCallback;

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
public interface TemplateManagerAsync {
	
	public void getTemplate(String id, AsyncCallback<String> callback);
	
	public void updateTemplate(String id, TemplateData data, AsyncCallback<String> callback);
	
	public void getTemplateUpdaterInfo(String viewkey, String id, AsyncCallback<TemplateUpdaterInfo> callback);
	
}
