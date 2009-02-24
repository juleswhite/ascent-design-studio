package org.gems.ajax.client.dnd;

import java.util.HashMap;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class DragSourceManager implements EventPreview {
	
	private static DragSourceManager instance_;
	
	public static DragSourceManager getInstance(){
		if(instance_ == null)
			instance_ = new DragSourceManager();
		
		return instance_;
	}
	
	private HashMap<Element, Boolean> sources_;
	
	public void add(DragSource ds, Element e){
		registerDragSource(e);
	}
	
	public void remove(DragSource ds, Element e){
		sources_.remove(e);
		if(sources_.size() == 0){
			sources_ = null;
			DOM.removeEventPreview(this);
		}
	}
	
	private void registerDragSource(Element el){
		if(sources_ == null){
			sources_ = new HashMap<Element, Boolean>(37);
			DOM.addEventPreview(this);
		}
		sources_.put(el, Boolean.TRUE);
	}

	public boolean onEventPreview(Event event) {
		if (sources_ != null) {
			if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
				Element el = DOM.eventGetTarget(event);
				if (sources_.get(el) != null)
					DOM.eventPreventDefault(event);
			}
		}
		return true;
	}
}
