package org.gems.ajax.client.edit.cmd;

import org.gems.ajax.client.SelectionManager;
import org.gems.ajax.client.edit.ConnectionEditPart;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.ModelEditPart;
import org.gems.ajax.client.figures.GEMSDiagram;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class DisconnectCommand extends AbstractConnectionCommand {

	private ModelEditPart source_;
	private ModelEditPart target_;
	private ConnectionEditPart connectionEditPart_;

	

	public DisconnectCommand(ConnectionEditPart connectionEditPart) {
		super();
		connectionEditPart_ = connectionEditPart;
		source_ = connectionEditPart.getSource();
		target_ = connectionEditPart.getTarget();
	}

	public boolean canExecute() {
		return source_ != null && target_ != null;
	}

	public boolean canUndo() {
		return true;
	}

	public void execute() {
		GEMSDiagram d = connectionEditPart_.getFigure().getDiagram();
		connectionEditPart_.detach();
		if(d!=null)
			d.focus();
	}

	public String getName() {
		return "Disconnect";
	}

	public void redo() {
		execute();
	}

	public void undo() {
		connectionEditPart_.attach(source_, target_);
		SelectionManager.getInstance().addToSelection(connectionEditPart_);
	}

	public void setTarget(EditPart ep) {
		if(ep instanceof ConnectionEditPart){
			connectionEditPart_ = (ConnectionEditPart)ep;
			source_ = connectionEditPart_.getSource();
			target_ = connectionEditPart_.getTarget();
		}
	}

}
