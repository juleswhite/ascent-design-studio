package org.gems.ajax.server.model;

import org.gems.ajax.client.model.ModelingPackage;
import org.gems.ajax.client.model.resources.ModelResource;

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
public interface ModelLoader {
	
	public ModelingPackage loadModel(ModelResource res);
}
