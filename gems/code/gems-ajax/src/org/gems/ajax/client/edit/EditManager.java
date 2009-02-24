package org.gems.ajax.client.edit;

import org.gems.ajax.client.event.UIEventListenerAdapter;
import org.gems.ajax.client.event.UIKeyListener;
import org.gems.ajax.client.figures.DiagramElement;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class EditManager extends UIEventListenerAdapter implements
		UIKeyListener {

	private CommandStack commandStack_ = new CommandStack();
	private Tool currentTool_;
	
	public void setCurrentTool(Tool t) {
		if(currentTool_ != null)
			currentTool_.deActivate();
		
		t.setCommandStack(commandStack_);
		currentTool_ = t;
		currentTool_.activate();
	}

	public void onFocus(DiagramElement el) {
		currentTool_.onFocus(el);
	}

	public void onKeyDown(DiagramElement sender, char keyCode, int modifiers) {
		currentTool_.onKeyDown(sender, keyCode, modifiers);
	}

	public void onKeyPress(DiagramElement sender, char keyCode, int modifiers) {
		currentTool_.onKeyPress(sender, keyCode, modifiers);
	}

	public void onKeyUp(DiagramElement sender, char keyCode, int modifiers) {
		currentTool_.onKeyUp(sender, keyCode, modifiers);
	}

	public void onLostFocus(DiagramElement el) {
		currentTool_.onLostFocus(el);
	}

	public void onMouseDown(DiagramElement el, int x, int y) {
		currentTool_.onMouseDown(el, x, y);
	}

	public void onMouseEnter(DiagramElement sender) {
		currentTool_.onMouseEnter(sender);
	}

	public void onMouseLeave(DiagramElement sender) {
		currentTool_.onMouseLeave(sender);
	}

	public void onMouseMove(DiagramElement sender, int x, int y) {
		currentTool_.onMouseMove(sender, x, y);
	}

	public void onMouseUp(DiagramElement sender, int x, int y) {
		currentTool_.onMouseUp(sender, x, y);
	}

	public CommandStack getCommandStack() {
		return commandStack_;
	}

	public void setCommandStack(CommandStack commandStack) {
		commandStack_ = commandStack;
	}

	public Tool getCurrentTool() {
		return currentTool_;
	}

}
