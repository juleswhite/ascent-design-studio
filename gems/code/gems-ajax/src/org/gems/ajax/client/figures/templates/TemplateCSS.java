package org.gems.ajax.client.figures.templates;

import java.util.HashMap;
import java.util.Map;

import org.gems.ajax.client.util.Util;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class TemplateCSS extends TemplateElement {

	private static Map<String, TemplateCSS> scripts_ = new HashMap<String, TemplateCSS>();

	public static TemplateCSS getCSS(String spath) {
		TemplateCSS ts = scripts_.get(spath);
		if (ts == null) {
			ts = new TemplateCSS(getCSSLoader(spath), spath);
			scripts_.put(spath, ts);
		}
		return ts;
	}

	public static String getCSSLoader(String spath) {

		String script = " "
				+ "   var head = document.getElementsByTagName(\"head\")[0];\r\n"
				+ "   var script = document.createElement('link');\r\n"
				+ "   script.type = 'text/css';\r\n" + "   script.href = \""
				+ spath + "\";\r\n" + "   head.appendChild(script);";
		
		return script;
	}

	private String script_;
	private String cssPath_;
	private boolean loaded_ = false;

	private TemplateCSS(String script, String csspath) {
		super();
		script_ = script;
		cssPath_ = csspath;
	}

	public String getScript() {
		return script_;
	}

	public void setScript(String script) {
		script_ = script;
	}

	public String getInitFunction() {
		return cssPath_;
	}

	public void setInitFunction(String initFunction) {
		cssPath_ = initFunction;
	}

	public void load() {
		if(!loaded_){
			loaded_ = true;
			Util.addCSS(cssPath_);
		}
	}

}
