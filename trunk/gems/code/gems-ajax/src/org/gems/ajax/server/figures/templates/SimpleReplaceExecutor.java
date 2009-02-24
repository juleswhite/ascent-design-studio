package org.gems.ajax.server.figures.templates;

import org.gems.ajax.client.figures.templates.TemplateData;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class SimpleReplaceExecutor implements TemplateExecutor {

	private String template_;

	public SimpleReplaceExecutor(String template) {
		super();
		template_ = template;
	}

	public String getTemplate() {
		return template_;
	}

	public void setTemplate(String template) {
		template_ = template;
	}

	public String exec(TemplateData data) {
		String t = template_;
		if (t != null) {
			for (String key : data.keySet()) {
				t = t.replaceAll("\\$\\{" + key + "\\}", data.get(key));
			}
		}
		return t;
	}

}
