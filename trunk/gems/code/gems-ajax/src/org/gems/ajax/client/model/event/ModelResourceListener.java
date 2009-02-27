package org.gems.ajax.client.model.event;

import org.gems.ajax.client.model.ModelElement;
import org.gems.ajax.client.model.resources.ModelResource;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public interface ModelResourceListener {
	public void resourceChanged(ModelResource res, ModelElement el,
			ModelEvent evt);
}
