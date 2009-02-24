package org.gems.ajax.client.figures;

import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class ImageToggleButton extends ImageButton {

	public ImageToggleButton(String plainstyle, String moverstyle,
			String pressedstyle, String pressedmouseoverstyle) {
		super(plainstyle, moverstyle, pressedstyle, pressedmouseoverstyle);
	}

	public void onMouseDown(Widget sender, int x, int y) {

	}

	public void onMouseUp(Widget sender, int x, int y) {
		setPressed(!getPressed());
		updateStyle();
	}

}
