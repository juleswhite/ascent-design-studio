package org.gems.ajax.client.figures.properties;

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

public class BooleanPropertyEditor extends EnumerationPropertyEditor {

	public BooleanPropertyEditor() {
		super(new String[]{"true","false"});
	}

	public Object getValue(){
		return super.getValue().equals("true");
	}

	public void edit(Object v) {
		if(v instanceof Boolean){
			v = ""+v;
		}
		super.edit(v);
	}
	
	
}
