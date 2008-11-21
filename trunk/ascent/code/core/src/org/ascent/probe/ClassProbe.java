/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.probe;

public class ClassProbe implements FeatureProbe {

	private String className_;

	private String property_;

	private int enabled_;

	public ClassProbe() {
	}

	public ClassProbe(String property, String className) {
		super();
		className_ = className;
		property_ = property;
	}

	public String getClassName() {
		return className_;
	}

	public void setClassName(String className) {
		className_ = className;
	}

	public String getProperty() {
		return property_;
	}

	public void setProperty(String property) {
		property_ = property;
	}

	public void run() {
		try {
			Class c = Class.forName(getClassName());
			enabled_ = 1;
		} catch (Throwable e) {
			enabled_ = 0;
		}
	}
	
	

	public int getEnabled() {
		return enabled_;
	}

	public Object getFeature() {
		return getProperty();
	}

}
