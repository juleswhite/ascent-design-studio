package org.gems.ajax.client.edit.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gems.ajax.client.SelectionManager;
import org.gems.ajax.client.edit.BatchCommand;
import org.gems.ajax.client.edit.Command;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.EditPartManager;
import org.gems.ajax.client.edit.Tool;
import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.ui.KeyboardListener;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class SelectionTool extends Tool {

	private boolean allowMultipleSelection_ = true;

	private boolean hasShiftModifier_ = false;
	
	private boolean hasCtrlModifier_ = false;

	private SelectionManager selectionManager_;

	public SelectionTool(SelectionManager selectionManager) {
		super();
		selectionManager_ = selectionManager;
	}

	public void activate() {
		hasCtrlModifier_ = false;
		super.activate();
	}

	public void deActivate() {
		hasCtrlModifier_ = false;
		super.deActivate();
	}

	public void onMouseEnter(DiagramElement sender) {

	}

	public void onMouseLeave(DiagramElement sender) {

	}

	public void onMouseDown(DiagramElement el, int x, int y) {
		if(hasShiftModifier_){
			ConnectionTool.getInstance(getCommandStack()).onMouseDown(el, x, y);
			return;
		}
		else if (!hasCtrlModifier_ || !allowMultipleSelection_) {
			for (int i = 0; i < selectionManager_.getSelection().size(); i++) {
				EditPart ep = selectionManager_.getSelection().get(i);
//				Util.setZIndex(ep.getFigure().getDiagramWidget(), GraphicsConstants.DEFAULT_MODEL_LAYER);
				ep.onDeSelect();
			}
			selectionManager_.getSelection().clear();
		}

		EditPart ep = EditPartManager.getEditPart(el);
		if (ep != null) {
			if (!selectionManager_.getSelection().contains(ep)) {
				selectionManager_.getSelection().add(ep);
//				Util.setZIndex(ep.getFigure().getDiagramWidget(), GraphicsConstants.SELECTED_MODEL_LAYER);
				ep.onSelect();
			} else if (hasCtrlModifier_ && allowMultipleSelection_) {
				selectionManager_.getSelection().remove(ep);
//				Util.setZIndex(ep.getFigure().getDiagramWidget(), GraphicsConstants.DEFAULT_MODEL_LAYER);
				ep.onDeSelect();
			}
		}
	}

	public void onKeyDown(DiagramElement sender, char keyCode, int modifiers) {
		if (keyCode == KeyboardListener.KEY_CTRL) {
			hasCtrlModifier_ = true;
		}
		else if(keyCode == KeyboardListener.KEY_SHIFT){
			hasShiftModifier_ = true;
		} else {
			super.onKeyDown(sender, keyCode, modifiers);
		}
	}

	public void onKeyPress(DiagramElement sender, char keyCode, int modifiers) {
		if (keyCode == KeyboardListener.KEY_CTRL) {
			hasCtrlModifier_ = false;
		} 
		else if (keyCode == KeyboardListener.KEY_SHIFT) {
			hasShiftModifier_ = false;
		}
		else if (keyCode == KeyboardListener.KEY_DELETE) {
			deleteSelectedElements();
		} else if (modifiers == KeyboardListener.MODIFIER_CTRL
				&& (keyCode == 'Z' || keyCode == 'z')) {
			// Undo
			getCommandStack().undo();
		} else if (modifiers == KeyboardListener.MODIFIER_CTRL
				&& (keyCode == 'Y' || keyCode == 'y')) {
			// Redo
			getCommandStack().redo();
		} else {
			super.onKeyPress(sender, keyCode, modifiers);
		}
	}

	public void deleteSelectedElements() {
		ArrayList<EditPart> todelete = new ArrayList<EditPart>();
		todelete.addAll(selectionManager_.getSelection());

		List<Command> cmds = new ArrayList<Command>();
		for (int i = 0; i < todelete.size(); i++) {
			EditPart ep = todelete.get(i);
			if (ep != null) {
				Command c = ep.getCommand(DELETE_REQUEST);
				if (c != null && c.canExecute()) {
					ep.onDeSelect();
					cmds.add(c);
				}
			}
		}

		Collections.sort(cmds, COMMAND_COMPARATOR);
		BatchCommand bc = new BatchCommand("Delete Selection", cmds);
		execute(bc);
	}

	public void onKeyUp(DiagramElement sender, char keyCode, int modifiers) {
		if (keyCode == KeyboardListener.KEY_CTRL) {
			hasCtrlModifier_ = false;
		} 
		else if (keyCode == KeyboardListener.KEY_SHIFT) {
			hasShiftModifier_ = false;
		}else if (keyCode == KeyboardListener.KEY_DELETE) {
			deleteSelectedElements();
		} else if ((modifiers == KeyboardListener.MODIFIER_CTRL || hasCtrlModifier_)
				&& (keyCode == 'Z' || keyCode == 'z')) {
			// Undo
			getCommandStack().undo();
		} else if ((modifiers == KeyboardListener.MODIFIER_CTRL || hasCtrlModifier_)
				&& (keyCode == 'Y' || keyCode == 'y')) {
			// Redo
			getCommandStack().redo();
		}
	}

	public boolean getAllowMultipleSelection() {
		return allowMultipleSelection_;
	}

	public void setAllowMultipleSelection(boolean allowMultipleSelection) {
		allowMultipleSelection_ = allowMultipleSelection;
	}
}
