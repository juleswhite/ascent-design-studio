package org.gems.ajax.client.edit;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class Request {

	private String id_;

	public Request(String id) {
		super();
		id_ = id;
	}

	public String getId() {
		return id_;
	}

	public void setId(String id) {
		id_ = id;
	}

}
