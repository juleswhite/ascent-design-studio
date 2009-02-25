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
 * This interface should be implemented to create a new template
 * language and plug it into the server-side template engine.
 * An executor can be registered with the DefaultServerTemplateManager
 * and run whenever a template is requested of the type handled
 * by this template executor.
 */
public interface TemplateExecutor {

	/**
	 * This method should execute the template with
	 * the provided data and produce html that can
	 * be displayed by the view on the client-side.
	 * 
	 * @param data
	 * @return
	 */
	public String exec(TemplateExecData data);
	
}
