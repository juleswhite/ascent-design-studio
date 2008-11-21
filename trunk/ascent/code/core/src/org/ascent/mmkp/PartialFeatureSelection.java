/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.mmkp;

import java.util.ArrayList;
import java.util.List;

public class PartialFeatureSelection {

	private List<Feature> features_ = new ArrayList<Feature>();
	
	public PartialFeatureSelection(List<Feature> features) {
		super();
		features_ = features;
	}
	public PartialFeatureSelection() {
		super();
	}
	public List<Feature> getFeatures() {
		return features_;
	}
	public void setFeatures(List<Feature> features) {
		features_ = features;
	}
	public String toString(){
		String str = "[";
		for(int i = 0; i < features_.size(); i++){
			str += features_.get(i).getName();
			if(i != features_.size()-1)
				str += ",";
		}
		str += "]";
		return str;		
	}
}
