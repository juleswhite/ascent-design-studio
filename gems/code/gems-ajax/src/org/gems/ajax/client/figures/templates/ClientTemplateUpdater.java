package org.gems.ajax.client.figures.templates;


/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ClientTemplateUpdater implements TemplateUpdater {

	private String template_;

	public ClientTemplateUpdater() {
	}

	public ClientTemplateUpdater(String template) {
		super();
		template_ = template;
	}

	public String getTemplate() {
		return template_;
	}

	public void setTemplate(String template) {
		template_ = template;
	}

	public void updateTemplate(TemplateData data,
			TemplateUpdateCallback callback) {
		String t = template_;
		for (String key : data.keySet()) {
			t = t.replaceAll("\\$\\{" + key + "\\}", data.get(key));
		}
		
		callback.setTemplate(t);
	}

}
