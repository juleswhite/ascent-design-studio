package org.gems.ajax.client.event;

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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.ui.KeyboardListener;

public final class GlobalKeyboardListener {
	
	private static final class WindowCloseListenerImpl implements
			WindowCloseListener {
		
		public native void onWindowClosed()
		/*-{
		  $doc.onkeydown = null;
		  $doc.onkeypress = null;
		  $doc.onkeyup = null;
		}-*/;

		public String onWindowClosing() {
			return null;
		}

		private native void init()
		/*-{
		  $doc.onkeydown = function(evt) {
		    @org.gems.ajax.client.event.GlobalKeyboardListener::onKeyDown(Lcom/google/gwt/user/client/Event;)(evt
		|| $wnd.event);
		  }
		  $doc.onkeypress = function(evt) {
		    @org.gems.ajax.client.event.GlobalKeyboardListener::onKeyPress(Lcom/google/gwt/user/client/Event;)(evt
		|| $wnd.event);
		  }
		  $doc.onkeyup = function(evt) {
		    @org.gems.ajax.client.event.GlobalKeyboardListener::onKeyUp(Lcom/google/gwt/user/client/Event;)(evt
		|| $wnd.event);
		  }
		}-*/;
	}

	private static GlobalKeyboardListener instance_;

	public static GlobalKeyboardListener getInstance() {
		if (instance_ == null) {
			instance_ = new GlobalKeyboardListener();
			WindowCloseListenerImpl closeListener = new WindowCloseListenerImpl();
			Window.addWindowCloseListener(closeListener);
			closeListener.init();
		}
		return instance_;
	}

	public static void onKeyDown(Event event) {
		char keyCode = (char) DOM.eventGetKeyCode(event);
		int modifiers = getModifiers(event);

		for (KeyboardListener l : getInstance().listeners_)
			l.onKeyDown(null, keyCode, modifiers);
	}

	public static void onKeyPress(Event event) {
		char keyCode = (char) DOM.eventGetKeyCode(event);
		int modifiers = getModifiers(event);

		for (KeyboardListener l : getInstance().listeners_)
			l.onKeyPress(null, keyCode, modifiers);
	}

	public static void onKeyUp(Event event) {
		char keyCode = (char) DOM.eventGetKeyCode(event);
		int modifiers = getModifiers(event);

		for (KeyboardListener l : getInstance().listeners_)
			l.onKeyUp(null, keyCode, modifiers);
	}

	private static int getModifiers(Event event) {
		int modifiers = 0;

		if (DOM.eventGetCtrlKey(event))
			modifiers = modifiers | KeyboardListener.MODIFIER_CTRL;
		if (DOM.eventGetAltKey(event))
			modifiers = modifiers | KeyboardListener.MODIFIER_ALT;
		if (DOM.eventGetShiftKey(event))
			modifiers = modifiers | KeyboardListener.MODIFIER_SHIFT;

		return modifiers;
	}

	private List<KeyboardListener> listeners_ = new ArrayList<KeyboardListener>();

	private GlobalKeyboardListener() {}

	public void add(int index, KeyboardListener element) {
		listeners_.add(index, element);
	}

	public boolean add(KeyboardListener e) {
		return listeners_.add(e);
	}

	public KeyboardListener remove(int index) {
		return listeners_.remove(index);
	}

	public boolean remove(Object o) {
		return listeners_.remove(o);
	}

}
