/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.mmkp;

public class BasicItem {
	private double value_;

	public BasicItem(double value) {
		super();
		value_ = value;
	}

	public double getValue() {
		return value_;
	}

	public void setValue(double value) {
		value_ = value;
	}

}
