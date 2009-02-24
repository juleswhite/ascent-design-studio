package org.gems.ajax.client.figures.properties;

import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.dnd.DnDManager;
import org.gems.ajax.client.dnd.DropTarget;
import org.gems.ajax.client.edit.DiagramElementEditPart;
import org.gems.ajax.client.util.GraphicsConstants;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class InlineEditor extends DeckPanel implements PropertyEditorListener, GraphicsConstants, PropertyEditor {
	private boolean dynamicSizing_ = false;
	private PropertyEditor propertyEditor_;
	private PropertyViewer propertyLabel_;
	private List<PropertyEditorListener> listeners_ = new ArrayList<PropertyEditorListener>();

	public InlineEditor(PropertyEditor propertyEditor,
			PropertyViewer propertyLabel) {
		super();
		propertyEditor_ = propertyEditor;
		propertyLabel_ = propertyLabel;

		propertyLabel_.getEditTrigger().addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				showEditor();
			}
		});
		propertyEditor.addListener(this);

		add(propertyEditor.getWidget());
		add(propertyLabel_.getDisplayWidget());
		
		setStylePrimaryName(INLINE_EDITOR_STYLE);
		
		showLabel();
	}

	public void onEditingComplete(Object value) {
		propertyLabel_.setValue(value);
		showLabel();
		stopEditing();
	}

	public void onEditingStart() {
		startEditing();
	}

	public void onEditingUpdate(Object newvalue) {
		for (PropertyEditorListener l : listeners_)
			l.onEditingUpdate(newvalue);
	}

	public void showEditor() {
		if(dynamicSizing_){
			int w = propertyEditor_.getWidget().getOffsetWidth();
			int h = propertyEditor_.getWidget().getOffsetHeight();
			setSize(w+"px", h+"px");
		}
		showWidget(0);
		propertyEditor_.edit(propertyLabel_.getValue());
		
		
	}

	public void showLabel() {
		if(dynamicSizing_)
			setSize(propertyLabel_.getDisplayWidget().getOffsetWidth()+"px", propertyLabel_.getDisplayWidget().getOffsetHeight()+"px");
		showWidget(1);
	}

	public void edit(Object v) {
		propertyLabel_.setValue(v);
	}

	public Object getValue() {
		return propertyLabel_.getValue();
	}

	public Widget getWidget() {
		return this;
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

	public boolean usingDynamicSizing() {
		return dynamicSizing_;
	}

	public void setDynamicSizing(boolean dynamicSizing) {
		dynamicSizing_ = dynamicSizing;
	}
	
}
