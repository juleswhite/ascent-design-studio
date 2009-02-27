package org.gems.ajax.client;

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
import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.edit.EditConstants;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.Tool;
import org.gems.ajax.client.edit.tools.SelectionTool;

public class SelectionManager implements EditConstants{

	private static SelectionManager instance_;

	public static SelectionManager getInstance() {
		if (instance_ == null)
			instance_ = new SelectionManager();

		return instance_;
	}

	private List<EditPart> selection_ = new ArrayList<EditPart>(5);

	private SelectionTool selectionTool_;

	private SelectionManager() {
		selectionTool_ = new SelectionTool(this);
	}

	public List<EditPart> getSelection() {
		return selection_;
	}
	
	public void clearSelection(){
		for(EditPart el : selection_){
			el.onDeSelect();
		}
		selection_.clear();
	}

	public void addToSelection(EditPart el) {
		if (!selection_.contains(el)) {
			selection_.add(el);
			el.onSelect();
		}
	}

	public void removeFromSelection(EditPart el) {
		if (selection_.contains(el)) {
			selection_.remove(el);
			el.onDeSelect();
		}
	}

	public void setSelection(List<EditPart> selection) {
		clearSelection();
		selection_.addAll(selection);
		for (int i = 0; i < selection_.size(); i++) {
			((EditPart) selection_.get(i)).onSelect();
		}
	}

	public Tool getSelectionTool() {
		return selectionTool_;
	}

}
