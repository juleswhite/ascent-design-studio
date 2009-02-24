package org.gems.ajax.client.views;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class DefaultView implements View {

	private Object root_;

	public DefaultView(Object root) {
		super();
		root_ = root;
	}

	public String getId() {
		return "Model";
	}

	public Object getRoot() {
		return root_;
	}

	public boolean isVisible(Object mo) {
		return true;
	}

	public View clone(Object nrootobj) {
		return new DefaultView(nrootobj);
	}

}
