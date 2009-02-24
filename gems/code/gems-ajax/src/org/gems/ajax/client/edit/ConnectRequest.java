package org.gems.ajax.client.edit;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class ConnectRequest extends Request {

	private String associationType_;

	public ConnectRequest(String id, String atype) {
		super(id);
		associationType_ = atype;
	}

	public String getAssociationType() {
		return associationType_;
	}

	public void setAssociationType(String associationType) {
		associationType_ = associationType;
	}

}
