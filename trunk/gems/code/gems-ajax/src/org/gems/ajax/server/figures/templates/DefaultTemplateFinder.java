package org.gems.ajax.server.figures.templates;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.gems.ajax.client.model.MetaType;
import org.gems.ajax.client.model.TypeManager;
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
public class DefaultTemplateFinder implements TemplateFinder {
	private Logger logger_ = Logger.getLogger(DefaultTemplateFinder.class
			.getName());

	private Map<String, Boolean> mappedModels_ = new HashMap<String, Boolean>();
	private Map<MetaType, String> templateTypes_ = new HashMap<MetaType, String>();
	private Map<MetaType, File> templateMap_ = new HashMap<MetaType, File>();

	private File templateRepository_;
	private DefaultServerTemplateManager manager_;
	private int fileWatchInterval_ = 1;
	private boolean watchFiles_ = false;

	public void mapDir(String mtype, File dir) {
		
		File[] templates = dir.listFiles();
		for (File template : templates) {
			if (template.isFile()) {
				String type = template.getName();
				int dot = type.lastIndexOf(".");
				if (dot > 0) {
					String ttype = type.substring(dot + 1);
					type = type.substring(0, dot);
					MetaType mt = TypeManager.getTypeForName(mtype, type);
					if (mt != null) {
						templateMap_.put(mt, template);
						templateTypes_.put(mt, ttype);
					}
				}
			}
		}
	}

	public void mapModelTemplates(String mtype) {
		File dir = new File(templateRepository_, mtype);
		if (dir.exists() && dir.isDirectory()) {
			mapDir(mtype,dir);
		}
	}

	public ResolvedTemplate findTemplate(String modeltype, String objecttype) {
		if (mappedModels_.get(modeltype) == null
				|| !mappedModels_.get(modeltype)) {
			mapModelTemplates(modeltype);
		}

		FileInputStream str = null;
		File f = null;
		MetaType mt = TypeManager.getTypeForName(modeltype, objecttype);
		while (f == null && mt != null) {
			f = templateMap_.get(mt);
			if (f != null)
				break;

			mt = mt.getParentType();
		}
		if (f != null && f.exists()) {
			try {
				str = new FileInputStream(f);
			} catch (Exception e) {
				logger_.log(Level.SEVERE,
						"Error getting input stream for template model:"
								+ modeltype + " type:" + objecttype, e);
			}

		}
		return new ResolvedTemplate(str, templateTypes_.get(mt));
	}

	public File getTemplateRepository() {
		return templateRepository_;
	}

	public String getTemplateRepositoryPath() {
		return templateRepository_.getAbsolutePath();
	}

	public void setTemplateRepositoryPath(String templateRepository) {
		templateRepository_ = new File(templateRepository);
		System.out.println(templateRepository_.getAbsolutePath());
	}

	public boolean getFileWatching() {
		return watchFiles_;
	}

	public Map<String, Boolean> getMappedModels() {
		return mappedModels_;
	}

	public void setMappedModels(Map<String, Boolean> mappedModels) {
		mappedModels_ = mappedModels;
	}

	public Map<MetaType, String> getTemplateTypes() {
		return templateTypes_;
	}

	public void setTemplateTypes(Map<MetaType, String> templateTypes) {
		templateTypes_ = templateTypes;
	}

	public Map<MetaType, File> getTemplateMap() {
		return templateMap_;
	}

	public void setTemplateMap(Map<MetaType, File> templateMap) {
		templateMap_ = templateMap;
	}

	public DefaultServerTemplateManager getManager() {
		return manager_;
	}

	public void setManager(DefaultServerTemplateManager manager) {
		manager_ = manager;
	}

	public int getFileWatchInterval() {
		return fileWatchInterval_;
	}

	public void setFileWatchInterval(int fileWatchInterval) {
		fileWatchInterval_ = fileWatchInterval;
	}

	public void setTemplateRepository(File templateRepository) {
		templateRepository_ = templateRepository;
	}

	public void setFileWatching(boolean watch) {
		watchFiles_ = watch;
		if (watchFiles_) {

			for (File f : getTemplateRepository().listFiles()) {
				if (f.isDirectory()) {
					DirectoryWatcher dw = new DirectoryWatcher(f
							.getAbsolutePath(), fileWatchInterval_);
					dw.addListener(new IFileListener() {

						public void onStop(Object notMonitoredResource) {
						}

						public void onStart(Object monitoredResource) {
						}

						public void onDelete(Object deletedResource) {
						}

						public void onChange(Object changedResource) {
							if (changedResource instanceof File) {
								File file = (File) changedResource;
								if (file.isFile()) {
									String dir = file.getParentFile().getName();
									String fname = file.getName();
									int dot = fname.lastIndexOf(".");
									if (dot > 0) {
										fname = fname.substring(0, dot);
										String key = dir + "/" + fname;
										
										MetaType mt = TypeManager.getTypeForName(dir, fname);
										if (mt != null && manager_.getServerTemplates().get(
												key) != null) {
											try {
												String t = IOUtils
														.toString(new FileInputStream(
																file));
												t = t.trim();
												manager_.loadExecutor(key, t, templateTypes_.get(mt));

											} catch (Exception e) {
												logger_.log(Level.SEVERE,
														"Unable to load template for key:"
																+ key, e);
											}
										}
									}
								}

							}
						}

						public void onAdd(Object newResource) {
						}
					});
					dw.start();
				}
			}
		}
	}
}
