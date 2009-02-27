package org.gems.ajax.client.figures;

import org.gems.ajax.client.geometry.Point;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class Resizer extends FocusPanel implements GraphicsConstants,
		MouseListener, EventPreview {

	private int dw_ = 0;
	private int dh_ = 0;
	private SimplePanel ghost_;
	private Resizeable target_;
	private Point resizeStartPosition_;
	private boolean handleShowing_ = false;
	private boolean resizing_ = false;
	private int dragHandleWidth_ = 0;
	private int dragHandleHeight_ = 0;

	public Resizer(Resizeable target) {
		this(target, false);
	}

	public Resizer(Resizeable target, boolean auto) {
		super();
		setStyleName(RESIZER_STYLE);
		target_ = target;

		if (auto)
			target_.addMouseListener(this);

		addMouseListener(new MouseListenerAdapter() {
			public void onMouseDown(Widget sender, int x, int y) {
				DOM.eventGetCurrentEvent().preventDefault();
				startResize(x, y);
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						Util.cancelAllDocumentSelections();
					}
				});
			}

			public void onMouseLeave(Widget sender) {
				// hideDragHandle();
			}
		});
		ghost_ = new SimplePanel();
		ghost_.setStyleName(RESIZER_GHOST_STYLE);

		RootPanel.get().add(this, 0, 0);
		dragHandleHeight_ = getOffsetHeight();
		dragHandleWidth_ = getOffsetWidth();
		RootPanel.get().remove(this);
	}

	public void dispose() {
		if (target_ != null) {
			target_.removeMouseListener(this);
			target_ = null;
		}
	}

	public boolean isResizing() {
		return resizing_;
	}

	public void startResize(int x, int y) {
		resizing_ = true;
		resizeStartPosition_ = new Point(getAbsoluteLeft() + x,
				getAbsoluteTop() + y);
		// hideDragHandle();
		showResizeGhost();
		DOM.addEventPreview(this);
	}

	public void showResizeGhost() {
		ghost_.setSize(target_.getTargetWidget().getOffsetWidth() + "px",
				target_.getTargetWidget().getOffsetHeight() + "px");
		RootPanel.get().add(ghost_,
				target_.getTargetWidget().getAbsoluteLeft(),
				target_.getTargetWidget().getAbsoluteTop());
		Util.bringToFront(ghost_);
	}

	public void hideResizeGhost() {
		RootPanel.get().remove(ghost_);
	}

	public void endResize() {
		resizing_ = false;
		resizeStartPosition_ = null;
		DOM.removeEventPreview(this);
		target_.setSize(
				target_.getTargetWidget().getOffsetWidth() + dw_ + "px",
				target_.getTargetWidget().getOffsetHeight() + dh_ + "px");
		dh_ = 0;
		dw_ = 0;
		hideResizeGhost();
		updateDragHandle();
	}

	public void updateSize(int x, int y) {
		dw_ = x - resizeStartPosition_.x;
		dh_ = y - resizeStartPosition_.y;
		ghost_.setSize((target_.getTargetWidget().getOffsetWidth() + dw_)
				+ "px", (target_.getTargetWidget().getOffsetHeight() + dh_)
				+ "px");
	}

	public void showDragHandle() {
		if (target_ != null && target_.getTargetParent() != null) {
			handleShowing_ = true;
			Point p = Util.getRelativeLocationToAncestor(target_
					.getTargetWidget(), target_.getTargetParent());
			target_.getTargetParent().add(
					this,
					p.x + target_.getTargetWidget().getOffsetWidth()
							- getOffsetWidth(),
					p.y + target_.getTargetWidget().getOffsetHeight()
							- getOffsetHeight());
			updateDragHandle(p);
		}
	}

	public void hideDragHandle() {
		if (target_ != null && target_.getTargetParent() != null) {
			handleShowing_ = false;
			target_.getTargetParent().remove(this);
		}
	}

	public void updateDragHandle() {
		Point p = Util.getRelativeLocationToAncestor(target_.getTargetWidget(),
				target_.getTargetParent());
		updateDragHandle(p);
	}

	public void updateDragHandle(Point p) {
		if (this.getParent() == target_.getTargetParent()) {
			target_.getTargetParent().setWidgetPosition(
					this,
					p.x + target_.getTargetWidget().getOffsetWidth()
							- getOffsetWidth(),
					p.y + target_.getTargetWidget().getOffsetHeight()
							- getOffsetHeight());
			Util.bringToFront(this);
		}
	}

	public void onMouseDown(Widget sender, int x, int y) {
	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {

	}

	public void onMouseMove(Widget sender, int x, int y) {

		if (!handleShowing_) {
			if (target_.inDragHandle(x, y, dragHandleWidth_, dragHandleHeight_)) {
				showDragHandle();
			}
		} else {
			if (!target_
					.inDragHandle(x, y, dragHandleWidth_, dragHandleHeight_)) {
				hideDragHandle();
			}
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
	}

	public boolean onEventPreview(Event event) {
		event.preventDefault();

		if (DOM.eventGetType(event) == Event.ONMOUSEMOVE) {
			int x = DOM.eventGetClientX(event);
			int y = DOM.eventGetClientY(event);
			updateSize(x, y);
			return false;
		} else if (DOM.eventGetType(event) == Event.ONMOUSEUP) {
			endResize();
			return false;
		}
		return true;
	}

	public boolean handleShowing() {
		return handleShowing_;
	}

}
