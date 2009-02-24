package org.gems.ajax.client.dnd;

import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.edit.Command;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class DragSource extends MouseListenerAdapter implements
		GraphicsConstants, EventPreview {

	private boolean armed_;
	private DropTargetMatcher matcher_;
	private List<DragSourceListener> listeners_ = new ArrayList<DragSourceListener>();
	// private FocusPanel eventCapture_;
	private Widget avatar_;
	private FocusPanel widget_;
	private boolean dragging_ = false;
	private DropTarget current_;
	private Command command_;
	private int xOff_;
	private int yOff_;

	public DragSource(FocusPanel widget, Widget avatar) {
		this(widget, avatar, AnyTargetMatcher.INSTANCE);
	}

	public DragSource(FocusPanel widget, Widget avatar, String tag) {
		this(widget, avatar, new DropTargetTagMatcher(tag));
	}

	public DragSource(FocusPanel widget, Widget avatar, DropTargetMatcher m) {
		super();
		matcher_ = m;
		// eventCapture_ = new FocusPanel();
		// eventCapture_.setVisible(false);
		// RootPanel.get().add(eventCapture_, 0, 0);
		widget_ = widget;
		avatar_ = avatar;

		DragSourceManager.getInstance().add(this, widget_.getElement());

		widget_.addMouseListener(this);
		// eventCapture_.addMouseListener(this);

		avatar_.ensureDebugId("avatar");
	}

	public void dispose() {
		DragSourceManager.getInstance().remove(this, widget_.getElement());
		widget_.removeMouseListener(this);
		// eventCapture_.removeMouseListener(this);
		listeners_.clear();
		// RootPanel.get().remove(eventCapture_);
		widget_ = null;
		avatar_ = null;
		current_ = null;
		command_ = null;
	}

	public void onMouseDown(Widget sender, int x, int y) {
		DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {

			public void execute() {
				Util.cancelAllDocumentSelections();
			}
		});
//		startDrag(x, y);
		DOM.addEventPreview(this);
		armed_ = true;
	}

	public void onMouseMove(Widget sender, int x, int y) {
		if (dragging_) {
			Util.cancelAllDocumentSelections();
			DeferredCommand
					.addCommand(new com.google.gwt.user.client.Command() {

						public void execute() {
							Util.cancelAllDocumentSelections();
						}
					});
			int xoff = avatar_.getOffsetWidth() / 2;
			int yoff = avatar_.getOffsetHeight() / 2;
			RootPanel.get().setWidgetPosition(avatar_,
					x-xOff_,//(sender.getAbsoluteLeft() + x) - xOff_,
					y-yOff_);//(sender.getAbsoluteTop() + y) - yOff_);

			DropTarget dt = DnDManager.getInstance().getDropTarget(
					x,//(sender.getAbsoluteLeft() + x),
					y);//(sender.getAbsoluteTop() + y));
			if (dt != current_) {

				for (DragSourceListener dsl : listeners_) {
					dsl.onExitTarget(this, current_);
				}
				current_ = dt;

				for (DragSourceListener dsl : listeners_) {
					dsl.onEnterTarget(this, current_);
				}
			}
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
		armed_ = false;
//		Util.showErrorMessage("sender:" + (sender.getStyleName()) + " " + x
//				+ "," + y);

		DropTarget target = DnDManager.getInstance().getDropTarget(
				x,y);//(sender.getAbsoluteLeft() + x), (sender.getAbsoluteTop() + y));
		if (target != null && matcher_ != null && matcher_.matches(target)) {
			onDrop(target, x, y);

			for (DragSourceListener dsl : listeners_) {
				dsl.onDrop(this, target);
			}
		}
		stopDrag();
	}

	public void startDrag(int x, int y) {
		dragging_ = true;

		DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {

			public void execute() {
				Util.cancelAllDocumentSelections();
			}
		});

		xOff_ = x;
		yOff_ = y;

		RootPanel.get().add(avatar_, widget_.getAbsoluteLeft(),
				widget_.getAbsoluteTop());
		// DOM.setCapture(eventCapture_.getElement());
//		DOM.addEventPreview(this);

		for (DragSourceListener dsl : listeners_) {
			dsl.onDragStart(this);
		}
	}

	public void stopDrag() {

		dragging_ = false;
		// DOM.releaseCapture(eventCapture_.getElement());
		DOM.removeEventPreview(this);
		
		if (avatar_ != null && avatar_.getParent() != null) {
			RootPanel.get().remove(avatar_);

			for (DragSourceListener dsl : listeners_) {
				dsl.onDragEnd(this);
			}
		}
	}

	public boolean onEventPreview(Event event) {
		if(DOM.eventGetType(event) == Event.ONMOUSEMOVE){
			int x = DOM.eventGetClientX(event);
			int y = DOM.eventGetClientY(event);
			
			if(dragging_){
				onMouseMove(null, x, y);
				return false;
			}
			else if(armed_) {
				x = x - getWidget().getAbsoluteLeft();
				y = y - getWidget().getAbsoluteTop();
				startDrag(x, y);
				return false;
			}
		}
		else if(DOM.eventGetType(event) == Event.ONMOUSEUP){
			if(dragging_){
				int x = DOM.eventGetClientX(event);
				int y = DOM.eventGetClientY(event);
				onMouseUp(null, x, y);
				return false;
			}
			else {
				armed_ = false;
				DOM.removeEventPreview(this);
				return false;
			}
		}
		return true;
	}

	public boolean addListener(DragSourceListener e) {
		return listeners_.add(e);
	}

	public boolean removeListener(Object o) {
		return listeners_.remove(o);
	}

	public boolean canDrop(DropTarget dt) {
		if (command_ != null) {
			command_.setTarget(dt.getEditPart());
			return command_.canExecute();
		}
		return true;
	}

	public void onDrop(DropTarget dt, int x, int y) {

		if (command_ != null) {
			command_.setTarget(dt.getEditPart());
			if (command_.canExecute())
				dt.getEditPart().getEditDomain().getCommandStack().execute(
						command_);
		}
	}

	public Command getCommand() {
		return command_;
	}

	public void setCommand(Command command) {
		command_ = command;
	}

	public Widget getAvatar() {
		return avatar_;
	}

	public void setAvatar(Widget avatar) {
		avatar_ = avatar;
	}

	public FocusPanel getWidget() {
		return widget_;
	}

	public void setWidget(FocusPanel widget) {
		widget_ = widget;
	}

	public boolean getDragging() {
		return dragging_;
	}

	public void setDragging(boolean dragging) {
		dragging_ = dragging;
	}

	public DropTarget getCurrent() {
		return current_;
	}

	public void setCurrent(DropTarget current) {
		current_ = current;
	}

	protected int getXOff() {
		return xOff_;
	}

	protected void setXOff(int off) {
		xOff_ = off;
	}

	protected int getYOff() {
		return yOff_;
	}

	protected void setYOff(int off) {
		yOff_ = off;
	}

}
