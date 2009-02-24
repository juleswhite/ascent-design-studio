package org.gems.ajax.client.figures.templates;


import com.google.gwt.user.client.rpc.RemoteService;

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
public interface TemplateManager extends RemoteService{
	
	public String getTemplate(String id);
	
	public String updateTemplate(String id, TemplateData data);
	
	public TemplateUpdaterInfo getTemplateUpdaterInfo(String viewkey, String id);
	
}
