package org.gems.ajax.server.figures.templates;

import java.io.InputStream;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ResolvedTemplate {
	private InputStream inputStream_;
	private String type_;

	public ResolvedTemplate(InputStream inputStream, String type) {
		super();
		inputStream_ = inputStream;
		type_ = type;
	}

	public InputStream getInputStream() {
		return inputStream_;
	}

	public void setInputStream(InputStream inputStream) {
		inputStream_ = inputStream;
	}

	public String getType() {
		return type_;
	}

	public void setType(String type) {
		type_ = type;
	}

}
