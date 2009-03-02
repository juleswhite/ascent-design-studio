/******************************************************************************
 * Copyright (c) 2005, 2006 Jules White.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jules White - initial API and implementation 
 ****************************************************************************/
package org.gems.ajax.client.figures.templates;

import java.util.HashMap;

public class AttributeSet extends HashMap<String, String> {
	
	private String selector_;

	public AttributeSet(String sel) {
		selector_ = sel;
	}

	/**
	 * @return the selector
	 */
	public String getSelector() {
		return selector_;
	}

	/**
	 * @param selector
	 *            the selector to set
	 */
	public void setSelector(String selector) {
		selector_ = selector;
	}

}
