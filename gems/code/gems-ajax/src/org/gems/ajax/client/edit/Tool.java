package org.gems.ajax.client.edit;

import org.gems.ajax.client.event.UIEventListenerAdapter;
import org.gems.ajax.client.event.UIKeyListener;
import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class Tool extends UIEventListenerAdapter implements UIKeyListener,
		GraphicsConstants, EditConstants {

	private boolean active_ = false;
	private Command command_;
	private CommandStack commandStack_;
	private ToolListener toolListener_;

	public void activate() {
		active_ = true;
		if(toolListener_ != null)
			toolListener_.activated(this);
	}

	public void deActivate() {
		active_ = false;
		Util.showNormalCursor();
		if(toolListener_ != null)
			toolListener_.deActivated(this);
	}

	public void onMouseEnter(DiagramElement sender) {
		updateCursor();
	}

	public void onMouseLeave(DiagramElement sender) {
		updateCursor();
	}

	public void updateCursor() {
		Command c = getCommand();
		if (c != null && c.canExecute()) {
			Util.showValidCursor();
		} else {
			Util.showInvalidCursor();
		}
	}

	public void onKeyDown(DiagramElement sender, char keyCode, int modifiers) {
	}

	public void onKeyPress(DiagramElement sender, char keyCode, int modifiers) {
	}

	public void onKeyUp(DiagramElement sender, char keyCode, int modifiers) {
	}

	public Command getCommand() {
		return command_;
	}

	public void setCommand(Command command) {
		command_ = command;
	}

	public CommandStack getCommandStack() {
		return commandStack_;
	}

	public void setCommandStack(CommandStack commandStack) {
		commandStack_ = commandStack;
	}

	public void execute(Command c){
		try{
			getCommandStack().execute(c);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ToolListener getToolListener() {
		return toolListener_;
	}

	public void setToolListener(ToolListener toolListener) {
		toolListener_ = toolListener;
	}
	
	
}
