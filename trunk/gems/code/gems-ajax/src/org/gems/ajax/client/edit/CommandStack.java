package org.gems.ajax.client.edit;

import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.SelectionManager;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class CommandStack {

	private int current_ = -1;
	private List commands_ = new ArrayList();

	public void execute(Command c) {
		if (current_ != commands_.size() - 1){
			int count = commands_.size() - (current_ + 1);
			for (int i = 0; i < count; i++)
				commands_.remove(commands_.size() - 1);
		}
		
		SelectionManager.getInstance().clearSelection();
		
		commands_.add(c);
		c.execute();
		current_++;
	}

	public void undo() {
		if (current_ > -1) {
			SelectionManager.getInstance().clearSelection();
			Command c = (Command) commands_.get(current_);
			current_--;
			c.undo();
		}
	}

	public void redo() {
		if (current_ != commands_.size() - 1) {
			SelectionManager.getInstance().clearSelection();
			current_++;
			Command c = (Command) commands_.get(current_);
			c.redo();
		}
	}

	public int getCurrent() {
		return current_;
	}

	public void setCurrent(int current) {
		current_ = current;
	}

	public List getCommands() {
		return commands_;
	}

	public void setCommands(List commands) {
		commands_ = commands;
	}

}
