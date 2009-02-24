package org.gems.ajax.client.util.dojo;

import com.google.gwt.user.client.Element;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class FadeIn extends DojoAnimation {
	private Element element_;

	public FadeIn(Element element, int dur, int dly) {
		super(DojoUtil.createfadeIn(element, dur, dly));
		element_ = element;
	}

	public Element getElement() {
		return element_;
	}

	public void setElement(Element element) {
		element_ = element;
	}

}
