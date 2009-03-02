package org.gems.ajax.client.figures.templates;

import java.util.List;

import org.gems.ajax.client.figures.templates.TemplateParser.Token;


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
	private String initWidth_;
	private String initHeight_;
	
	public ClientTemplateUpdater() {
	}

	public ClientTemplateUpdater(String template) {
		super();
		template_ = template;
		
		Token t = TemplateParser.parseDirective(template_, "GEMS_Props");
		if (t != null && t.data != null) {
			try {
				List<AttributeSet> attrs = CSSParser.parseAttributes("gems {"
						+ t.data + "}");
				if (attrs.size() > 0) {
					initWidth_ = attrs.get(0).get(ProcessedTemplate.INIT_WIDTH);
					initHeight_ = attrs.get(0).get(ProcessedTemplate.INIT_HEIGHT);
				}
			} catch (Exception e) {
			}
		}
	}

	public String getTemplate() {
		return template_;
	}

	public void setTemplate(String template) {
		template_ = template;
	}

	public void updateTemplate(TemplateData data,
			TemplateUpdateCallback callback) {
		
		callback.setTemplate(updateTemplateDirect(data));
	}
	
	public String updateTemplateDirect(TemplateData data) {
		
		if(data.get(TemplateData.HEIGHT) == null)
			data.put(TemplateData.HEIGHT,initHeight_);
		if(data.get(TemplateData.WIDTH) == null)
			data.put(TemplateData.WIDTH,initWidth_);
			
		
		String t = template_;
		for (String key : data.keySet()) {
			t = t.replaceAll("\\$\\{" + key + "\\}", data.get(key));
		}
		
		return t;
	}

}
