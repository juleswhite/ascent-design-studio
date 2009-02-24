package org.gems.ajax.client.figures.properties;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesClickEvents;
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

public class TextPropertyViewer implements PropertyViewer {

	private Label label_ = new Label();
	
	public TextPropertyViewer(){}
	
	public TextPropertyViewer(String v){
		label_.setWordWrap(false);
		label_.setText(v);
	}
	
	public Widget getDisplayWidget() {
		return label_;
	}

	public Object getValue() {
		return label_.getText();
	}

	public void setValue(Object v) {
		label_.setText(""+v);
	}

	public SourcesClickEvents getEditTrigger() {
		return label_;
	}

}
