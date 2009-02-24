package org.gems.ajax.client.edit;

import org.gems.ajax.client.GEMSEditor;

/******************************************************************************
 * Copyright (c) 2007 Jules White.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jules White - initial API and implementation 
 ****************************************************************************/

public class EditDomain {

	private GEMSEditor editor_;
	private EditManager editManager_;
	
	public EditDomain(EditManager editManager) {
		super();
		editManager_ = editManager;
	}

	public CommandStack getCommandStack() {
		return editManager_.getCommandStack();
	}

	public void setCommandStack(CommandStack stk) {
		editManager_.setCommandStack(stk);
	}

	public EditManager getEditManager() {
		return editManager_;
	}

	public void setEditManager(EditManager editManager) {
		editManager_ = editManager;
	}

	public GEMSEditor getEditor() {
		return editor_;
	}

	public void setEditor(GEMSEditor editor) {
		editor_ = editor;
	}
	
	
}
