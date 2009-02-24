package org.gems.ajax.client.dnd;

import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class DropTarget implements Comparable<DropTarget> {

	private List<String> tags_;
	private EditPart editPart_;
	private Widget widget_;

	public DropTarget(EditPart ep, Widget widget) {
		this(ep, widget, new String[0]);
	}

	public DropTarget(EditPart ep, Widget widget, String tag) {
		this(ep, widget, new String[] { tag });
	}

	public DropTarget(EditPart ep, Widget widget, String[] tags) {
		super();
		widget_ = widget;
		editPart_ = ep;

		if (tags != null) {
			for (String s : tags)
				getTags().add(s);
		}
	}

	public Widget getWidget() {
		return widget_;
	}

	public void setWidget(Widget widget) {
		widget_ = widget;
	}

	public boolean intersects(int x, int y) {
		return widget_.getAbsoluteLeft() < x && widget_.getAbsoluteTop() < y
				&& widget_.getAbsoluteLeft() + widget_.getOffsetWidth() > x
				&& widget_.getAbsoluteTop() + widget_.getOffsetHeight() > y;
	}

	public int compareTo(DropTarget t2) {
		if (widget_.getElement() == t2.getWidget().getElement()
				|| editPart_ == t2.editPart_) {
			return 0;
		} else if (Util.isAncestor(editPart_, t2.editPart_)) {
			return 1;
		} else if (Util.isAncestor(t2.editPart_, editPart_)) {
			return -1;
		} else {
			return 0;
		}
	}

	public EditPart getEditPart() {
		return editPart_;
	}

	public void setEditPart(EditPart editPart) {
		editPart_ = editPart;
	}

	public List<String> getTags() {
		if (tags_ == null)
			tags_ = new ArrayList<String>(1);
		return tags_;
	}

	public void setTags(List<String> tags) {
		tags_ = tags;
	}

	public String toString(){
		return "DropTarget[editpart:"+editPart_+"]";
	}
}
