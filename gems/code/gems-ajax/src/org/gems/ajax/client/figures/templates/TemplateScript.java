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
			String initf, String upf, String checkf) {
		if (spath != null) {
			TemplateScript ts = scripts_.get(spath);
			if (ts == null) {
				ts = new TemplateScript(spath,Util.getScriptLoader(spath), initf, upf, checkf);
				scripts_.put(spath, ts);
			}
			return ts;
		} else {
			return new TemplateScript(null, src, null, null, null);
		}
	}

	private String checkLoadedCode_;
	private int tryCount_ = 15;
	
	private String scriptPath_;
	private String script_;
	private String initFunction_;
	private String updateFunction_;
	private boolean loaded_ = false;
	private boolean ready_ = false;

	private TemplateScript(String scriptpath, String script, String initFunction, String upfunc, String checkloaded) {
		super();
		script_ = script;
		initFunction_ = initFunction;
		updateFunction_ = upfunc;
		scriptPath_ = scriptpath;
		checkLoadedCode_ = checkloaded;
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
			
			if(scriptPath_ != null)
				Util.addScript(scriptPath_);		
			else
				Util.eval(script_);
		
			if (initFunction_ != null && checkLoadedCode_ != null) {
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						tryInit();
					}
				});
			}
			else {
				ready_ = true;
			}
		}
		if(updateFunction_ != null && ready_){
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					Util.eval(updateFunction_);
				}
			});
		}
	}
	
	private void tryInit(){
		Object loaded = Util.evalBoolean(checkLoadedCode_);
		if(loaded != null && Boolean.TRUE.equals(loaded)){
			Util.eval(initFunction_);
			ready_ = true;
		}
		else if(tryCount_ > 0)
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					tryCount_--;
					tryInit();
				}
			});
	}

}
