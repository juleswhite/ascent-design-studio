package org.gems.ajax.server;

import org.gems.ajax.client.GEMS;
import org.gems.ajax.client.model.ModelingPackage;
import org.gems.ajax.client.model.resources.ModelParameterRef;
import org.gems.ajax.client.model.resources.ModelResource;
import org.gems.ajax.server.model.ModelLoader;
import org.gems.ajax.server.util.ServerUtils;
import org.springframework.context.ApplicationContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class GEMSImpl extends RemoteServiceServlet implements GEMS {

	public static final String MODEL_LOADER = "modelLoader";

	private ModelLoader loader_;

	public GEMSImpl() {
		initGEMS();
	}

	private void initGEMS(){
		ApplicationContext ctx = ServerUtils.getInstance()
		.getServerApplicationContext();
		loader_ = (ModelLoader)ctx.getBean(MODEL_LOADER);
	}

	public ModelingPackage getModelPackage(ModelResource res) {
		if (res != null) {
			if (res instanceof ModelParameterRef) {
				ModelParameterRef ref = (ModelParameterRef) res;
				System.out.println("Received a request for : "
						+ ref.getParameterRef());
			}
			return loader_.loadModel(res);
		}
		return null;
	}

}
