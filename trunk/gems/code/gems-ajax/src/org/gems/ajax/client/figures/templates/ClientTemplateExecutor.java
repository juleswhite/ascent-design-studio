package org.gems.ajax.client.figures.templates;

import org.gems.ajax.client.model.ClientModelObject;

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
 * This interface should be implemented if you want to create a
 * new executor type that is translated into javascript and run
 * on the client. 
 */
public interface ClientTemplateExecutor {

	/**
	 * This method takes the template data and current object
	 * of the figure and updates the html used by the figure's
	 * view.
	 * 
	 * @param data
	 * @param cmo
	 * @return
	 */
	public String exec(TemplateData data, ClientModelObject cmo);
	
}
