package org.gems.ajax.client.edit;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class BatchCommand implements Command {

	private List<Command> commands_ = new ArrayList<Command>(7);
	private String name_;

	public BatchCommand(String name) {
		super();
		name_ = name;
	}

	public BatchCommand(String name, List<Command> commands) {
		super();
		name_ = name;
		commands_ = commands;
	}

	public boolean canExecute() {
		return commands_.size() > 0;
	}

	public boolean canUndo() {
		for (Command c : commands_)
			if (!c.canUndo())
				return false;
		return true;
	}

	public void execute() {
		for (Command c : commands_)
			c.execute();
	}

	public String getName() {
		return name_;
	}

	public void redo() {
		for (Command c : commands_)
			c.redo();
	}

	public void undo() {
		for (Command c : commands_)
			c.undo();
	}

	public void setTarget(EditPart ep) {
		for (Command c : commands_)
			c.setTarget(ep);
	}

}
