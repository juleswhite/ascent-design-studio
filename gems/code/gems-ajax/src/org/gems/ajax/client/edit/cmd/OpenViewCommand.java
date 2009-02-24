package org.gems.ajax.client.edit.cmd;

import org.gems.ajax.client.GEMSEditor;
import org.gems.ajax.client.edit.Command;
import org.gems.ajax.client.edit.EditDomain;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.EditPartFactory;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.views.View;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class OpenViewCommand implements Command {
	private View view_;
	private String viewName_;
	private Object root_;
	private ModelHelper modelHelper_;
	private EditPartFactory editPartFactory_;
	private EditDomain editDomain_;

	public boolean canExecute() {
		return viewName_ != null && root_ != null && modelHelper_ != null
				&& editPartFactory_ != null && editDomain_ != null;
	}

	public boolean canUndo() {
		return false;
	}

	public String getName() {
		return "Open View:" + viewName_;
	}

	public void redo() {
	}

	public void setTarget(EditPart ep) {		
		root_ = ep.getModel();
		modelHelper_ = ep.getModelHelper();
		editPartFactory_ = ep.getFactory();
		editDomain_ = ep.getEditDomain();
		viewName_ = modelHelper_.getLabel(root_);
		view_ = ep.getView().clone(root_);
	}

	public void undo() {
	}

	public void execute() {
		GEMSEditor editor = editDomain_.getEditor();
		if (!editor.hasOpenView(root_)) {
			editor.open(modelHelper_, editPartFactory_, root_, view_);
		} else {
			editor.switchView(root_);
		}
	}

	public String getViewName() {
		return viewName_;
	}

	public void setViewName(String viewName) {
		viewName_ = viewName;
	}

}
