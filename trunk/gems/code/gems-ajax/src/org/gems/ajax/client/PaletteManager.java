/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.gems.ajax.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gems.ajax.client.edit.EditManager;

import com.google.gwt.user.client.ui.Widget;

public abstract class PaletteManager {

	private EditManager editManager_;
	private List<ToolEntry> toolEntries_ = new ArrayList<ToolEntry>();
	private HashMap lookup_ = new HashMap();
	
	public PaletteManager(EditManager editManager) {
		super();
		editManager_ = editManager;
	}

	public EditManager getEditManager() {
		return editManager_;
	}

	public void setEditManager(EditManager editManager) {
		editManager_ = editManager;
	}

	public void addToolEntry(ToolEntry e){
		toolEntries_.add(e);
		Widget w = createToolWidget(e);
		lookup_.put(e,w);
		lookup_.put(w,e);
		addToolWidget(w);
	}
	
	public void removeToolEntry(ToolEntry e){
		toolEntries_.remove(e);
		Widget w = (Widget)lookup_.get(e);
		lookup_.remove(e);
		lookup_.remove(w);
		removeToolWidget(w);
	}
	
	protected abstract void addToolWidget(Widget w);
	protected abstract void removeToolWidget(Widget w);
	protected abstract Widget createToolWidget(ToolEntry e);
}
