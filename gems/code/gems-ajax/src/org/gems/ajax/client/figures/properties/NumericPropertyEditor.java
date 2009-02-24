package org.gems.ajax.client.figures.properties;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Widget;

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

public class NumericPropertyEditor extends TextPropertyEditor {

	public NumericPropertyEditor() {
		super();
		setType(SINGLE_LINE);
	}

	public void prepareEditor() {
		super.prepareEditor();
		getEditor().addKeyboardListener(new KeyboardListenerAdapter(){
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				if(Character.isLetter(keyCode) || Character.isSpace(keyCode)){
					cancelKey();
				}
				else if(keyCode==KeyboardListener.KEY_ENTER){
					stopEditing();
				}
			}			
		});
	}

	public double getNumber(){
		try{
			return Double.parseDouble(getText());
		}catch (Exception e) {
			return 0;
		}
	}

	public String getValue() {
		return ""+getNumber();
	}
	
	
}
