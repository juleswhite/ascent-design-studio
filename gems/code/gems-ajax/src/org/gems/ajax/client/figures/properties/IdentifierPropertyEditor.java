package org.gems.ajax.client.figures.properties;

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

public class IdentifierPropertyEditor extends TextPropertyEditor {

	public IdentifierPropertyEditor() {
		super();
		setType(SINGLE_LINE);
	}

	public void prepareEditor() {
		super.prepareEditor();
		getEditor().addKeyboardListener(new KeyboardListenerAdapter(){
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				if(Character.isSpace(keyCode)){
					cancelKey();
					return;
				}
				else if(!Character.isLetter(keyCode)){
					if(getText().length() == 0){
						cancelKey();
					}
					else if(!Character.isDigit(keyCode)){
						if(keyCode != '_' && keyCode != '.' && keyCode != ':' && keyCode != '-'){
							cancelKey();
						}
					}
				}
			}			
		});
	}
	
}
