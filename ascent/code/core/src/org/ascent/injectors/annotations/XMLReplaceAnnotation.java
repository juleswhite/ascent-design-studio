package org.ascent.injectors.annotations;
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
public class XMLReplaceAnnotation extends ReplaceAnnotation {

	
	public String[] getTargetRegex(String target, String replacementval){
		String replaceex = null;
		
		if (target.startsWith("@")) {
			replaceex = "[\\s]+" + target.substring(1)
					+ "[\\s]*\\=[\\s]*\\\"[^\\\"]*\\\"";
			replacementval = " " + target.substring(1) + "=\""
					+ replacementval + "\"";
		} else if (target.equals("#value")) {
			replaceex = "\\>[^\\<]*\\<";
			replacementval = ">"+replacementval+"<";
		} 
		
		return new String[]{replaceex,replacementval};
	}
}
