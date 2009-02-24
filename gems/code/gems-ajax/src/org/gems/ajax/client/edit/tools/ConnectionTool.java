/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.gems.ajax.client.edit.tools;

import org.gems.ajax.client.edit.CommandStack;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.EditPartManager;
import org.gems.ajax.client.edit.ModelEditPart;
import org.gems.ajax.client.edit.Tool;
import org.gems.ajax.client.edit.cmd.ConnectCommand;
import org.gems.ajax.client.figures.DiagramElement;

public class ConnectionTool extends Tool {

	private static ConnectionTool instance_;

	public static ConnectionTool getInstance() {
		return getInstance(null);
	}

	public static ConnectionTool getInstance(CommandStack stk) {
		if (instance_ == null) {
			instance_ = new ConnectionTool();
			instance_.setCommandStack(stk);
		} else {
			instance_.setCommandStack(stk);
		}
		return instance_;
	}

	private ConnectCommand command_;

	private ConnectionTool() {
	}

	@Override
	public void onMouseDown(DiagramElement el, int x, int y) {
		if (command_ == null) {
			EditPart ep = EditPartManager.getEditPart(el);
			if (ep != null) {
				command_ = (ConnectCommand) ep
						.getCommand(ADD_CONNECTION_REQUEST);
				if (command_ != null)
					command_.setSource((ModelEditPart) EditPartManager
							.getEditPart(el));
			}
		} else {
			command_.setTarget((ModelEditPart) EditPartManager.getEditPart(el));
		}

		if (command_ != null && command_.canExecute()) {
			execute(command_);
			command_ = null;
		}
	}

	@Override
	public void onMouseEnter(DiagramElement sender) {
		if (command_ != null) {
			// figure out if valid
			// connection target
		}
	}

	@Override
	public void onMouseLeave(DiagramElement sender) {

	}

	public void deActivate() {
		command_ = null;
		super.deActivate();
	}

}
