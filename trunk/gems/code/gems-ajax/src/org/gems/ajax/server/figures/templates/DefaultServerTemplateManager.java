package org.gems.ajax.server.figures.templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.gems.ajax.client.figures.templates.TemplateData;
import org.gems.ajax.client.figures.templates.TemplateUpdaterInfo;
import org.gems.ajax.client.model.ClientModelObject;
import org.gems.ajax.client.model.MetaType;
import org.gems.ajax.client.model.ModelRegistry;
import org.gems.ajax.server.model.ClientServerModelMapping;
import org.gems.ajax.server.util.file.DirectoryWatcher;
import org.gems.ajax.server.util.file.IFileListener;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class DefaultServerTemplateManager implements ServerTemplateManager {

	public static final String CLIENTSIDE_FLAG = "<!--GEMS_Client-->";
	public static final String TEMPLATE_TYPE_FLAG = "<!--TemplateType:";

	private Logger logger_ = Logger
			.getLogger(DefaultServerTemplateManager.class.getName());

	private Map<String, ExecutorFactory> executorFactories_ = new HashMap<String, ExecutorFactory>();
	private Map<String, TemplateUpdaterInfo> updaters_ = new HashMap<String, TemplateUpdaterInfo>();
	private Map<String, TemplateExecutor> serverTemplates_ = new HashMap<String, TemplateExecutor>();
	private String defaultTemplateType_ = "Simple";
	private TemplateFinder templateFinder_;


	public String getTemplate(String id) {
		return "<div style=\"width:'100%'; height:'100%'\"><img src=\"img/block.png\" style=\"width:'100%'; height:'100%'\" height=\"100%\" width=\"100%\"></div><div style=\"position:absolute; top:0; left:0; margin:10 10 10 10;\">foo bar</div>";
	}

	public String updateTemplate(String id, TemplateData tdata) {

		TemplateExecData data = new TemplateExecData(tdata);
		TemplateExecutor t = serverTemplates_.get(id);
		
		ClientModelObject cmo = ModelRegistry.getInstance().get(""+data.getObjectId());
		
		if(cmo != null){
			data.setClientModelObject(cmo);
			Object severobj = ClientServerModelMapping.get().getServerObject(cmo);
			if(severobj != null){
				data.setServerModelObject(severobj);
			}
		}
		
		String result = null;
		if (t != null) {
			result = t.exec(data);
		}

		System.out.println(t);

		return result;
	}

	public TemplateUpdaterInfo getTemplateUpdaterInfo(String viewkey, String id) {
		String t = "<div style=\"width:${width}; height:${height}\"><img src=\"img/block.png\" style=\"width:${width}; height:${height}\" height=\"${height}\" width=\"${width}\"></div><div style=\"position:absolute; top:0; left:0; margin:10 10 10 10;\">foo bar</div>";

		ClientModelObject mo = ModelRegistry.getInstance().get(id);
		MetaType mt = mo.getTypes().get(0);
		String modeltype = mt.getModelType().getName();
		String type = mt.getName();

		return loadTemplate(modeltype, type);
	}

	public String getTemplateType(String t) {
		String type = null;
		if (t.contains(TEMPLATE_TYPE_FLAG)) {
			int start = t.indexOf(TEMPLATE_TYPE_FLAG)
					+ TEMPLATE_TYPE_FLAG.length();
			int end = t.indexOf("-->", start);
			if (start >= end)
				type = defaultTemplateType_;
			else
				type = t.substring(start, end);
		} else {
			type = defaultTemplateType_;
		}
		return type;
	}

	public TemplateUpdaterInfo loadTemplate(String modeltype, String type) {
		String key = modeltype + "/" + type;
		TemplateUpdaterInfo upi = updaters_.get(key);

		if (upi == null) {

			String t = null;
			boolean clientside = false;

			try {
//				File f = new File(getTemplateRepository(), key + ".htm");
				ResolvedTemplate res = templateFinder_.findTemplate(modeltype, type);
				InputStream in = res.getInputStream();
				
				if (in != null) {
					t = IOUtils.toString(in);
					t = t.trim();
					clientside = t.startsWith(CLIENTSIDE_FLAG);

					if (!clientside) {
						loadExecutor(key, t, res.getType());
						t = "loading...";
					}

				}
			} catch (Exception e) {
				logger_.log(Level.SEVERE, "Unable to load template for key:"
						+ key, e);
			}

			upi = new TemplateUpdaterInfo(key, t, clientside);
			updaters_.put(key, upi);
		}

		return upi;
	}
	
	public void loadExecutor(String key, String t, String type) {
		ExecutorFactory factory = executorFactories_.get(type);
		if (factory != null) {
			TemplateExecutor exec = factory.createExecutor(t);
			serverTemplates_.put(key, exec);
		}
		else {
			logger_.severe("No executor factory found for template type:"+type+" while trying to execute template for key:"+key+" with template contents:"+t);
		}
	}

	
	public Map<String, TemplateExecutor> getServerTemplates() {
		return serverTemplates_;
	}

	public void setServerTemplates(Map<String, TemplateExecutor> serverTemplates) {
		serverTemplates_ = serverTemplates;
	}

	public Map<String, ExecutorFactory> getExecutorFactories() {
		return executorFactories_;
	}

	public void setExecutorFactories(
			Map<String, ExecutorFactory> executorFactories) {
		executorFactories_ = executorFactories;
	}

	public TemplateFinder getTemplateFinder() {
		return templateFinder_;
	}

	public void setTemplateFinder(TemplateFinder templateFinder) {
		templateFinder_ = templateFinder;
	}

	
	

}
