package org.gems.ajax.client.views;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public abstract class AbstractView implements View {

	private String id_;
	private Object root_;

	public AbstractView(String id, Object root) {
		super();
		id_ = id;
		root_ = root;
	}

	public AbstractView(String id) {
		this(id, null);
	}

	public String getId() {
		return id_;
	}

	public void setId(String id) {
		id_ = id;
	}

	public Object getRoot() {
		return root_;
	}

	public void setRoot(Object root) {
		root_ = root;
	}
	
	public abstract boolean isVisible(Object mo);

}
