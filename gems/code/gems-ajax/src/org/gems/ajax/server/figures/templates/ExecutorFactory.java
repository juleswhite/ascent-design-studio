package org.gems.ajax.server.figures.templates;
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

/**
 * An executor factory is used to create executors for a given template
 * language type. The executor factories should be registered with the
 * DefaultServerTemplateManager.
 */
public interface ExecutorFactory {
	
	/**
	 * Create a template executor from a string of template
	 * data.
	 * 
	 * @param template
	 * @return
	 */
	public TemplateExecutor createExecutor(String template);
}
