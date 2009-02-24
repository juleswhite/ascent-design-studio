package org.gems.ajax.client.edit.cmd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.gems.ajax.client.SelectionManager;
import org.gems.ajax.client.edit.Command;
import org.gems.ajax.client.edit.ConnectionEditPart;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.EditPartManager;
import org.gems.ajax.client.edit.ModelEditPart;
import org.gems.ajax.client.model.ModelHelper;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class DeleteCommand implements Command {

	private class ConnectionInfo {
		public ModelEditPart source;
		public ModelEditPart target;
		public ConnectionEditPart connectionEditPart;

		public ConnectionInfo(ModelEditPart source, ModelEditPart target,
				ConnectionEditPart connectionEditPart) {
			super();
			this.source = source;
			this.target = target;
			this.connectionEditPart = connectionEditPart;
		}

		public ConnectionInfo(ConnectionEditPart connectionEditPart) {
			super();
			this.connectionEditPart = connectionEditPart;
			this.source = connectionEditPart.getSource();
			this.target = connectionEditPart.getTarget();
		}

	}

	private ModelHelper modelHelper_;
	private ModelEditPart child_;
	private ModelEditPart parent_;
	private HashSet<ConnectionInfo> connections_ = new HashSet<ConnectionInfo>();;

	public DeleteCommand(ModelHelper modelHelper, ModelEditPart child) {
		super();
		modelHelper_ = modelHelper;
		child_ = child;
		collectConnections(child_.getModel());
	}

	public boolean canExecute() {
		if (child_.getParent() == null)
			return true;

		return modelHelper_.canRemoveChild(child_.getParent().getModel(),
				child_.getModel());
	}

	public boolean canUndo() {
		return true;
	}

	public void execute() {
		if (child_.getParent() == null)
			return;

		SelectionManager.getInstance().removeFromSelection(
				child_);

		removeConnections();

		parent_ = child_.getParent();
		parent_.removeChild(child_);

		parent_.focus();
	}

	public void restoreConnections() {
		for (ConnectionInfo ci : connections_) {
			if (ci.source.getParent() != null && ci.target.getParent() != null && !ci.connectionEditPart.isAttached()) {
				ci.connectionEditPart.attach(ci.source, ci.target);
			}
		}
	}

	public void removeConnections() {
		for (ConnectionInfo ci : connections_) {
			if (ci.connectionEditPart.isAttached()) {
				ci.connectionEditPart.detach();
			}
		}
	}

	public void collectConnections(Object o) {
		List children = modelHelper_.getChildren(o);
		for (Object c : children)
			collectConnections(c);

		List cons = modelHelper_.getConnections(o);
		if (cons != null) {
			for (Object con : cons) {
				ConnectionEditPart ce = (ConnectionEditPart) EditPartManager
						.getEditPart(con);
				if(ce.getSource() != null && ce.getTarget() != null && ce.isAttached())
					connections_.add(new ConnectionInfo(ce));
			}
		}
	}

	public String getName() {
		return "Delete";
	}

	public void redo() {
		execute();
	}

	public void undo() {
		if (parent_ != null && child_ != null) {
			parent_.addChild(child_);
			restoreConnections();
			SelectionManager.getInstance().addToSelection(
					child_);
		}
	}

	public void setTarget(EditPart ep) {
		if(ep instanceof ModelEditPart){
			child_ = (ModelEditPart)ep;
			connections_.clear();
			collectConnections(child_.getModel());
		}
	}

}
