package org.gems.ajax.client.figures.properties;

import java.util.ArrayList;
import java.util.List;

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

public abstract class AbstractPropertyEditor implements PropertyEditor{

	private List<PropertyEditorListener> listeners_ = new ArrayList<PropertyEditorListener>();

	public AbstractPropertyEditor() {
		super();
	}

	public void addListener(PropertyEditorListener l) {
		listeners_.add(l);
	}

	public void removeListener(PropertyEditorListener l) {
		listeners_.remove(l);
	}

	public void stopEditing() {
		for (PropertyEditorListener l : listeners_)
			l.onEditingComplete(getValue());
	}

	public void startEditing() {
		for (PropertyEditorListener l : listeners_)
			l.onEditingStart();
	}

	public void editingUpdate() {
		for (PropertyEditorListener l : listeners_)
			l.onEditingUpdate(getValue());
	}

	public List<PropertyEditorListener> getListeners() {
		return listeners_;
	}

	public void setListeners(List<PropertyEditorListener> listeners) {
		listeners_ = listeners;
	}

}