package org.ascent.mmkp;

import java.util.HashMap;

/******************************************************************************
 * Copyright (c) 2007 Jules White.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jules White - initial API and implementation 
 ****************************************************************************/
public class FeatureMap extends HashMap<Integer,Feature>{

	private Feature root_;
	
	public FeatureMap(Feature root){
		root_ = root;
		traverse(root_);
	}
	
	private void traverse(Feature f){
		put(f.getId(),f);
		for(Feature c : f.getRequiredChildren())
			traverse(c);
		for(Feature c : f.getXorChildren())
			traverse(c);
		for(Feature c : f.getOptionalChildren())
			traverse(c);
	}
}
