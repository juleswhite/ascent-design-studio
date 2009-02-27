package org.gems.ajax.client.edit.cmd;

import org.gems.ajax.client.SelectionManager;
import org.gems.ajax.client.edit.Command;
import org.gems.ajax.client.edit.DiagramElementEditPart;
import org.gems.ajax.client.edit.EditPart;

public class AddChildCommand implements Command {

	private EditPart parent_;
	private EditPart child_; 
	
	public boolean canExecute() {
		return parent_ != null && child_ != null;
	}

	public boolean canUndo() {
		return true;
	}

	public void execute() {
		((DiagramElementEditPart)parent_).addChild(child_);
		SelectionManager.getInstance().addToSelection(parent_);
		parent_.onSelect();
	}

	public String getName() {
		return "Add Child";
	}

	public void redo() {
		execute();
	}

	public void setTarget(EditPart ep) {
		parent_ = ep;
	}

	public void undo() {
		((DiagramElementEditPart)parent_).removeChild(child_);
	}

	public EditPart getChild() {
		return child_;
	}

	public void setChild(EditPart child) {
		child_ = child;
	}

}
