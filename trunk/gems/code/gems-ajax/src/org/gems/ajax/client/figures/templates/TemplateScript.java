package org.gems.ajax.client.figures.templates;

import java.util.HashMap;
import java.util.Map;

import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class TemplateScript extends TemplateElement{

	private static Map<String, TemplateScript> scripts_ = new HashMap<String, TemplateScript>();

	public static TemplateScript getScript(String spath, String src,
			String initf, String upf) {
		if (spath != null) {
			TemplateScript ts = scripts_.get(spath);
			if (ts == null) {
				ts = new TemplateScript(Util.getScriptLoader(spath), initf, upf);
				scripts_.put(spath, ts);
			}
			return ts;
		} else {
			return new TemplateScript(src, null, null);
		}
	}

	

	private String script_;
	private String initFunction_;
	private String updateFunction_;
	private boolean loaded_ = false;

	private TemplateScript(String script, String initFunction, String upfunc) {
		super();
		script_ = script;
		initFunction_ = initFunction;
		updateFunction_ = upfunc;
	}

	public String getScript() {
		return script_;
	}

	public void setScript(String script) {
		script_ = script;
	}

	public String getInitFunction() {
		return initFunction_;
	}

	public void setInitFunction(String initFunction) {
		initFunction_ = initFunction;
	}

	public void load() {
		if (!loaded_) {
			loaded_ = true;
			Util.eval(script_);
			if (initFunction_ != null) {
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						Util.eval(initFunction_);
					}
				});
			}
		}
		if(updateFunction_ != null){
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					Util.eval(updateFunction_);
				}
			});
		}
	}

}
