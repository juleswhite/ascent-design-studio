package org.gems.ajax.client.event;

import org.gems.ajax.client.figures.DiagramElement;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
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

public class UIEventConnector implements MouseListener, KeyboardListener {

	private Widget sender_;
	private DiagramElement element_;

	public UIEventConnector(FocusPanel fp, DiagramElement element) {
		super();
		element_ = element;

		fp.addMouseListener(this);
		fp.addKeyboardListener(this);
	}

	public DiagramElement getElement() {
		return element_;
	}

	public void setElement(DiagramElement element) {
		element_ = element;
	}

	public void onMouseEnter(Widget sender) {
		UIEventDispatcher.onMouseEnter(element_);
	}

	public void onMouseLeave(Widget sender) {
		UIEventDispatcher.onMouseLeave(element_);
	}

	public void onMouseMove(Widget sender, int x, int y) {
		UIEventDispatcher.onMouseMove(element_, x, y);
	}

	public void onMouseUp(Widget sender, int x, int y) {
		UIEventDispatcher.onMouseUp(element_, x, y);
	}

	public void onMouseDown(Widget sender, int x, int y) {
		UIEventDispatcher.onMouseDown(element_, x, y);
	}

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
		UIEventDispatcher.onKeyUp(element_, keyCode, modifiers);
	}

	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		UIEventDispatcher.onKeyPress(element_, keyCode, modifiers);
	}

	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		UIEventDispatcher.onKeyDown(element_, keyCode, modifiers);
	}
}
