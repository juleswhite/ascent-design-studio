package org.gems.ajax.client.figures.properties;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class EnumerationPropertyEditor extends AbstractPropertyEditor {

	private List<String> values_;

	private ListBox listBox_;

	public EnumerationPropertyEditor(List<String> values) {
		super();
		values_ = values;
		init();
	}

	public EnumerationPropertyEditor(String[] values) {
		super();
		values_ = Arrays.asList(values);
		init();
	}

	protected void init() {
		listBox_ = new ListBox();
		listBox_.setVisibleItemCount(1);
		listBox_.setMultipleSelect(false);
		listBox_.addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				editingUpdate();
			}

		});
		listBox_.addFocusListener(new FocusListener() {

			public void onLostFocus(Widget sender) {
				stopEditing();
			}

			public void onFocus(Widget sender) {
				startEditing();
			}

		});
		for (String s : values_)
			listBox_.addItem(s);
		
		listBox_.setSelectedIndex(0);
		listBox_.setSize("100px", "22px");
	}

	public List<String> getValues() {
		return values_;
	}

	public void setValues(List<String> values) {
		values_ = values;
	}

	public void edit(Object v) {
		int index = values_.indexOf(v);
		if(index > -1)
			listBox_.setSelectedIndex(index);
	}

	public Widget getWidget() {
		if(listBox_.getOffsetWidth() == 0){
			listBox_.setSize("100px", "22px");
		}
		return listBox_;
	}

	public Object getValue() {
		return values_.get(listBox_.getSelectedIndex());
	}

}
