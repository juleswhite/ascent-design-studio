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

import org.gems.ajax.client.figures.DiagramElement;


public class UIEventDispatcher {

	private static UIEventDispatcher instance_;
	
	public static UIEventDispatcher getInstance(){
		
		if(instance_ == null){
			instance_ = new UIEventDispatcher();
//			GlobalKeyboardListener.getInstance().add(new KeyboardListener() {
//			
//				public void onKeyUp(Widget sender, char keyCode, int modifiers) {
//					UIEventDispatcher.onKeyUp(null, keyCode, modifiers);
//				}
//			
//				public void onKeyPress(Widget sender, char keyCode, int modifiers) {
//					UIEventDispatcher.onKeyPress(null, keyCode, modifiers);
//				}
//			
//				public void onKeyDown(Widget sender, char keyCode, int modifiers) {
//					UIEventDispatcher.onKeyDown(null, keyCode, modifiers);
//				}			
//			});
		}
			
		return instance_;
	}
	
	private List uiListeners_ = new ArrayList();
	
	private List keyListeners_ = new ArrayList();
	
	public static void onKeyUp(DiagramElement sender, char keyCode, int modifiers) {
		getInstance().doKeyUp(sender, keyCode, modifiers);
	}

	public static void onKeyPress(DiagramElement sender, char keyCode, int modifiers) {
		getInstance().doKeyPress(sender, keyCode, modifiers);
	}

	public static void onKeyDown(DiagramElement sender, char keyCode, int modifiers) {
		getInstance().doKeyDown(sender, keyCode, modifiers);
	}
	
	public void doKeyUp(DiagramElement sender, char keyCode, int modifiers) {
		for(int i = 0; i < keyListeners_.size(); i++)
			((UIKeyListener)keyListeners_.get(i)).onKeyUp(sender, keyCode, modifiers);
	}

	public void doKeyPress(DiagramElement sender, char keyCode, int modifiers) {
		for(int i = 0; i < keyListeners_.size(); i++)
			((UIKeyListener)keyListeners_.get(i)).onKeyPress(sender, keyCode, modifiers);
	}

	public void doKeyDown(DiagramElement sender, char keyCode, int modifiers) {
		for(int i = 0; i < keyListeners_.size(); i++)
			((UIKeyListener)keyListeners_.get(i)).onKeyDown(sender, keyCode, modifiers);
	}
	
	public static void onMouseEnter(DiagramElement sender) {
		getInstance().doMouseEnter(sender);
	}

	public static void onMouseLeave(DiagramElement sender) {
		getInstance().doMouseLeave(sender);
	}

	public static void onMouseMove(DiagramElement sender, int x, int y) {
		getInstance().doMouseMove(sender, x, y);
	}
	
	public static void onMouseDown(DiagramElement el, int x, int y) {
		getInstance().doMouseDown(el, x, y);
	}

	public static void onMouseUp(DiagramElement sender, int x, int y) {
		getInstance().doMouseUp(sender, x, y);
	}
	
	public void doMouseEnter(DiagramElement sender) {
		for(int i = 0; i < uiListeners_.size(); i++)
			((UIEventListener)uiListeners_.get(i)).onMouseEnter(sender);
	}

	public void doMouseLeave(DiagramElement sender) {
		for(int i = 0; i < uiListeners_.size(); i++)
			((UIEventListener)uiListeners_.get(i)).onMouseLeave(sender);
	}

	public void doMouseMove(DiagramElement sender, int x, int y) {
		for(int i = 0; i < uiListeners_.size(); i++)
			((UIEventListener)uiListeners_.get(i)).onMouseMove(sender,x,y);
	}

	public void doMouseDown(DiagramElement sender, int x, int y) {
		for(int i = 0; i < uiListeners_.size(); i++)
			((UIEventListener)uiListeners_.get(i)).onMouseDown(sender,x,y);
	}
	
	public void doMouseUp(DiagramElement sender, int x, int y) {
		for(int i = 0; i < uiListeners_.size(); i++)
			((UIEventListener)uiListeners_.get(i)).onMouseUp(sender,x,y);
	}

	public static void onFocus(DiagramElement el) {
		getInstance().doFocus(el);
	}

	public static void onLostFocus(DiagramElement el) {
		getInstance().doLostFocus(el);
	}
	
//	public static void onClick(DiagramElement el) {
//	getInstance().doClick(el);
//}
	
//	public void doClick(DiagramElement el){
//		for(int i = 0; i < uiListeners_.size(); i++)
//			((UIEventListener)uiListeners_.get(i)).onMouseDown(el);
//	}
//	
	public void doFocus(DiagramElement el){
		for(int i = 0; i < uiListeners_.size(); i++)
			((UIEventListener)uiListeners_.get(i)).onFocus(el);
	}
	
	public void doLostFocus(DiagramElement el){
		for(int i = 0; i < uiListeners_.size(); i++)
			((UIEventListener)uiListeners_.get(i)).onLostFocus(el);
	}

	public boolean addUIListener(Object e) {
		return uiListeners_.add(e);
	}

	public boolean removeUIListener(Object o) {
		return uiListeners_.remove(o);
	}

	public boolean addKeyListener(Object e) {
		return keyListeners_.add(e);
	}

	public boolean removeKeyListener(Object o) {
		return keyListeners_.remove(o);
	}
	
	
}
