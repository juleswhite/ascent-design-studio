package org.gems.ajax.server.figures.templates;

import java.util.Map;

import org.gems.ajax.client.figures.templates.ClientTemplateUpdater;
import org.gems.ajax.client.model.ClientModelObject;
import org.gems.ajax.client.model.Property;


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

	public String exec(TemplateExecData data) {
		String t = template_;
		
		ClientTemplateUpdater ctu = new ClientTemplateUpdater(template_);
		t = ctu.updateTemplateDirect(data);
		
		if (t != null) {
			for (String key : data.keySet()) {
				t = t.replaceAll("\\$\\{" + key + "\\}", data.get(key));
			}
		}
		
		ClientModelObject cmo = data.getClientModelObject();
		if(cmo != null){
			Map<String, Property> props = cmo.getProperties();
			for(String pname : props.keySet()){
				t = t.replaceAll("\\$\\{model." + pname + "\\}", props.get(pname).getValueAsString());
			}
		}
		return t;
	}

}
