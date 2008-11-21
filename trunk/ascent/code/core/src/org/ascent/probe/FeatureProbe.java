/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.probe;


public interface FeatureProbe extends Probe{
	public Object getFeature();
	
	/*
	 * If the probe discovers that the feature is
	 * enabled, it should return 1. If the feature
	 * is disabled, it should return 0. If the probe
	 * was unable to determine the status of the 
	 * feature, it should return -1.
	 * 
	 * This method should only be called after
	 * run() is called.
	 */
	public int getEnabled();
}
