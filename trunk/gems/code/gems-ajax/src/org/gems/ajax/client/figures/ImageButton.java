package org.gems.ajax.client.figures;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class ImageButton extends FocusPanel implements MouseListener {

	private boolean pressed_ = false;
	private boolean active_ = false;
	private String style_;
	private String mouseOverStyle_;
	private String pressedStyle_;
	private String pressedMouseOverStyle_;

	public ImageButton(String plainstyle, String moverstyle,
			String pressedstyle, String pressedmouseoverstyle) {
		super();

		style_ = plainstyle;
		mouseOverStyle_ = moverstyle;
		pressedStyle_ = pressedstyle;
		pressedMouseOverStyle_ = pressedmouseoverstyle;

		addMouseListener(this);

		updateStyle();
	}

	public void onMouseMove(Widget sender, int x, int y) {
	}

	public void onMouseEnter(Widget sender) {
		active_ = true;
		updateStyle();
	}

	public void onMouseLeave(Widget sender) {
		active_ = false;
		updateStyle();
	}

	public void onMouseUp(Widget sender, int x, int y) {
		pressed_ = false;
		updateStyle();
	}

	public void onMouseDown(Widget sender, int x, int y) {
		pressed_ = true;
		updateStyle();
	}

	public void updateStyle() {
		if (pressed_) {
			if (active_) {
				setStylePrimaryName(pressedMouseOverStyle_);
			} else {
				setStylePrimaryName(pressedStyle_);
			}
		} else {
			if (active_) {
				setStylePrimaryName(mouseOverStyle_);
			} else {
				setStylePrimaryName(style_);
			}
		}
	}

	public boolean getPressed() {
		return pressed_;
	}

	public void setPressed(boolean pressed) {
		pressed_ = pressed;
	}

	public boolean getActive() {
		return active_;
	}

	public void setActive(boolean active) {
		active_ = active;
	}

	public String getStyle() {
		return style_;
	}

	public void setStyle(String style) {
		style_ = style;
	}

	public String getMouseOverStyle() {
		return mouseOverStyle_;
	}

	public void setMouseOverStyle(String mouseOverStyle) {
		mouseOverStyle_ = mouseOverStyle;
	}

	public String getPressedStyle() {
		return pressedStyle_;
	}

	public void setPressedStyle(String pressedStyle) {
		pressedStyle_ = pressedStyle;
	}

	public String getPressedMouseOverStyle() {
		return pressedMouseOverStyle_;
	}

	public void setPressedMouseOverStyle(String pressedMouseOverStyle) {
		pressedMouseOverStyle_ = pressedMouseOverStyle;
	}

}
