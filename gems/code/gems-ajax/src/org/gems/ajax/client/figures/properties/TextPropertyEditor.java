package org.gems.ajax.client.figures.properties;

import org.gems.ajax.client.geometry.Dimension;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusListenerAdapter;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBoxBase.TextAlignConstant;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class TextPropertyEditor extends AbstractPropertyEditor {

	public static final int SINGLE_LINE = 0;
	public static final int MULTI_LINE = 1;

	private String value_;

	private FocusPanel editorPanel_;

	private TextBoxBase editor_;

	private int type_ = -1;

	private Dimension editorSize_;

	public TextPropertyEditor() {
		super();
		editorPanel_ = new FocusPanel();
	}

	public int autoDetectType(String v) {
		if (v.indexOf("\n") > -1) {
			return MULTI_LINE;
		} else {
			return SINGLE_LINE;
		}
	}

	public void edit(Object v) {
		value_ = "" + v;
		editor_ = createEditor();
		prepareEditor();
	}

	public TextBoxBase createEditor() {
		int type = type_;
		if (type_ == -1)
			type = autoDetectType(value_);

		TextBoxBase editor = null;
		editorPanel_.clear();
		if (type == MULTI_LINE) {
			editor = new TextArea();
			editorPanel_.setSize("100px", "100px");
			editor.setSize("100%", "100%");
		} else if (type == SINGLE_LINE) {
			editor = new TextBox();
			editor.addKeyboardListener(new KeyboardListener() {

				public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				}

				public void onKeyPress(Widget sender, char keyCode,
						int modifiers) {
				}

				public void onKeyDown(Widget sender, char keyCode, int modifiers) {
					if (keyCode == KEY_ENTER)
						stopEditing();
				}

			});
		}
		return editor;
	}

	public void prepareEditor() {
		editor_.setText(value_);

		if (editorSize_ != null) {
			editorPanel_.setSize(editorSize_.width + "px", editorSize_.height
					+ "px");
			editor_
					.setSize(editorSize_.width + "px", editorSize_.height
							+ "px");
		}

		editorPanel_.add(editor_);
		editor_.addFocusListener(new FocusListenerAdapter() {

			public void onLostFocus(Widget sender) {
				stopEditing();
			}

			public void onFocus(Widget sender) {
				startEditing();
			}

		});

		editor_.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				editingUpdate();
			}
		});

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				editor_.setFocus(true);
				editor_.selectAll();
			}
		});

	}

	public Widget getWidget() {
		return editorPanel_;
	}

	public String getValue() {
		return getText();
	}

	public void setValue(String value) {
		value_ = value;
		if (editor_ != null) {
			editor_.setText(value);
		}
	}

	public FocusPanel getEditorPanel() {
		return editorPanel_;
	}

	public void setEditorPanel(FocusPanel editorPanel) {
		editorPanel_ = editorPanel;
	}

	public TextBoxBase getEditor() {
		return editor_;
	}

	public void setEditor(TextBoxBase editor) {
		editor_ = editor;
	}

	public int getType() {
		return type_;
	}

	public void setType(int type) {
		type_ = type;
	}

	public Dimension getEditorSize() {
		return editorSize_;
	}

	public void setEditorSize(Dimension editorSize) {
		editorSize_ = editorSize;
	}

	public void addChangeListener(ChangeListener listener) {
		editor_.addChangeListener(listener);
	}

	public void addClickListener(ClickListener listener) {
		editor_.addClickListener(listener);
	}

	public void addFocusListener(FocusListener listener) {
		editor_.addFocusListener(listener);
	}

	public void addKeyboardListener(KeyboardListener listener) {
		editor_.addKeyboardListener(listener);
	}

	public void addStyleDependentName(String styleSuffix) {
		editor_.addStyleDependentName(styleSuffix);
	}

	public void addStyleName(String style) {
		editor_.addStyleName(style);
	}

	public void cancelKey() {
		editor_.cancelKey();
	}

	public int getCursorPos() {
		return editor_.getCursorPos();
	}

	public String getSelectedText() {
		return editor_.getSelectedText();
	}

	public int getSelectionLength() {
		return editor_.getSelectionLength();
	}

	public String getStyleName() {
		return editor_.getStyleName();
	}

	public String getStylePrimaryName() {
		return editor_.getStylePrimaryName();
	}

	public int getTabIndex() {
		return editor_.getTabIndex();
	}

	public String getText() {
		return editor_.getText();
	}

	public boolean isAttached() {
		return editor_.isAttached();
	}

	public boolean isEnabled() {
		return editor_.isEnabled();
	}

	public boolean isReadOnly() {
		return editor_.isReadOnly();
	}

	public void removeChangeListener(ChangeListener listener) {
		editor_.removeChangeListener(listener);
	}

	public void removeClickListener(ClickListener listener) {
		editor_.removeClickListener(listener);
	}

	public void removeFocusListener(FocusListener listener) {
		editor_.removeFocusListener(listener);
	}

	public void removeKeyboardListener(KeyboardListener listener) {
		editor_.removeKeyboardListener(listener);
	}

	public void removeStyleDependentName(String styleSuffix) {
		editor_.removeStyleDependentName(styleSuffix);
	}

	public void removeStyleName(String style) {
		editor_.removeStyleName(style);
	}

	public void selectAll() {
		editor_.selectAll();
	}

	public void setAccessKey(char key) {
		editor_.setAccessKey(key);
	}

	public void setCursorPos(int pos) {
		editor_.setCursorPos(pos);
	}

	public void setEnabled(boolean enabled) {
		editor_.setEnabled(enabled);
	}

	public void setFocus(boolean focused) {
		editor_.setFocus(focused);
	}

	public void setKey(char key) {
		editor_.setKey(key);
	}

	public void setName(String name) {
		editor_.setName(name);
	}

	public void setReadOnly(boolean readOnly) {
		editor_.setReadOnly(readOnly);
	}

	public void setSelectionRange(int pos, int length) {
		editor_.setSelectionRange(pos, length);
	}

	public void setStyleName(String style) {
		editor_.setStyleName(style);
	}

	public void setStylePrimaryName(String style) {
		editor_.setStylePrimaryName(style);
	}

	public void setTabIndex(int index) {
		editor_.setTabIndex(index);
	}

	public void setText(String text) {
		editor_.setText(text);
	}

	public void setTextAlignment(TextAlignConstant align) {
		editor_.setTextAlignment(align);
	}

	public void setTitle(String title) {
		editor_.setTitle(title);
	}

}
