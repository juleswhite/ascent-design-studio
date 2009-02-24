package org.gems.ajax.client.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.gems.ajax.client.util.GraphicsConstants;

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

public class BasicDecoration implements ConnectionDecoration, GraphicsConstants {

	private static Map<Integer, String> DIRS;

	static {
		DIRS = new HashMap<Integer, String>();
		DIRS.put(UP, "-up");
		DIRS.put(DOWN, "-down");
		DIRS.put(LEFT, "-left");
		DIRS.put(RIGHT, "-right");
	}

	private Widget widget_;

	private String styleBase_ = "";

	private ConnectionLocation connectionLocation_;
	private ArrayList<DecorationListener> listeners_ = new ArrayList<DecorationListener>(1);
	
	public BasicDecoration(String styleBase, ConnectionLocation loc) {
		super();
		styleBase_ = styleBase;
		widget_ = createWidget();
		widget_.setStylePrimaryName(styleBase);
		connectionLocation_ = loc;
	}
	
	public BasicDecoration(ConnectionLocation loc, Widget w) {
		super();
		styleBase_ = null;
		widget_ = w;
		connectionLocation_ = loc;
	}
	
	public BasicDecoration(String stylebase, ConnectionLocation loc, Widget w) {
		super();
		styleBase_ = stylebase;
		widget_ = w;
		connectionLocation_ = loc;
	}

	public boolean delegatesEvents() {
		return false;
	}

	protected Widget createWidget() {
		return new SimplePanel();
	}

	public void dispose() {
		widget_.removeFromParent();
		
		for(DecorationListener l : listeners_)
			l.onDispose(this);
	}

	public ConnectionLocation getLocation() {
		return connectionLocation_;
	}

	public String getStyleBase() {
		return styleBase_;
	}

	public void setStyleBase(String styleBase) {
		styleBase_ = styleBase;
	}

	public ConnectionLocation getConnectionLocation() {
		return connectionLocation_;
	}

	public void setConnectionLocation(ConnectionLocation connectionLocation) {
		connectionLocation_ = connectionLocation;
	}

	public void setWidget(Widget widget) {
		widget_ = widget;
	}

	public Widget getWidget() {
		return widget_;
	}

	public void setDirection(int direction) {
		if(styleBase_ != null)
			widget_.setStylePrimaryName(styleBase_ + DIRS.get(direction));
	}

	public void update() {
		for(DecorationListener l : listeners_)
			l.onUpdate(this);
	}

	public void addListener(DecorationListener e) {
		listeners_.add(e);
	}

	public void removeListener(DecorationListener o) {
		listeners_.remove(o);
	}

}
