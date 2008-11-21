package org.ascent.injectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class BasicMatcher implements FeatureMatcher {
	public static final String FEATURE_WILDCARD = "*";

	
	public List match(String pattern, Map<String, List> features) {
		int index = pattern.indexOf(FEATURE_WILDCARD);
		
		ArrayList result = new ArrayList();
		
		if (index > -1) {
			String pre = pattern;
			String post = "";

			if (index == 0 && pattern.length() == 1) {
				pre = "";
				post = "";
			} else if (index > -1) {

				if (index == 0) {
					pre = "";
					post = pattern.substring(index + 1);
				} else {
					pre = pattern.substring(0, index);
					post = "";
				}
			}
			
			for(String key : features.keySet()){
				if(key.startsWith(pre) && key.endsWith(post) && features.get(key) != null && features.get(key).size() > 0){
					result.add(key);
				}
			}
		}
		else{
			if(features.get(pattern) != null && features.get(pattern).size() > 0){
				result.add(pattern);
			}
		}

		return result;
	}

}
