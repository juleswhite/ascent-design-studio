package org.gems.ajax.client.figures.templates;

import java.util.HashMap;
import java.util.Map;

import org.gems.ajax.client.model.Type;

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

public class Template {
	public static final String STYLE_PRIMARY_NAME = "styleprimaryname";
	public static final String TYPE_ATTR = "gemstype";
	public static final String CHILD_MAPPINGS_ATTR = "childmappings";
	public static final String RESIZABLE = "resizable";
	public static final String MOVEABLE = "moveable";
	
	private Map<String,String> rootAttributes_ = new HashMap<String, String>();
	private Type renderedType_;
	private Type containingParentType_;
	private String viewId_;
	private String editPartId_;
	private String stylePrimaryName_;
	private boolean moveable_ = true;
	private boolean resizable_ = true;
	private Map<Type,String> containerIds_;
	private String html_;
	
	public Type getRenderedType() {
		return renderedType_;
	}
	public void setRenderedType(Type renderedType) {
		renderedType_ = renderedType;
	}
	public Type getContainingParentType() {
		return containingParentType_;
	}
	public void setContainingParentType(Type containingParentType) {
		containingParentType_ = containingParentType;
	}
	public String getViewId() {
		return viewId_;
	}
	public void setViewId(String viewId) {
		viewId_ = viewId;
	}
	public String getEditPartId() {
		return editPartId_;
	}
	public void setEditPartId(String editPartId) {
		editPartId_ = editPartId;
	}
	public Map<Type, String> getContainerIds() {
		return containerIds_;
	}
	public void setContainerIds(Map<Type, String> containerIds) {
		containerIds_ = containerIds;
	}
	public String getHtml() {
		return html_;
	}
	public void setHtml(String html) {
		html_ = html;
	}
	public static String getCHILD_MAPPINGS_ATTR() {
		return CHILD_MAPPINGS_ATTR;
	}
	public String getStylePrimaryName() {
		return stylePrimaryName_;
	}
	public void setStylePrimaryName(String stylePrimaryName) {
		stylePrimaryName_ = stylePrimaryName;
	}
	public Map<String, String> getRootAttributes() {
		return rootAttributes_;
	}
	public void setRootAttributes(Map<String, String> rootAttributes) {
		rootAttributes_ = rootAttributes;
	}
	public boolean getMoveable() {
		return moveable_;
	}
	public void setMoveable(boolean moveable) {
		moveable_ = moveable;
	}
	public boolean getResizable() {
		return resizable_;
	}
	public void setResizable(boolean resizable) {
		resizable_ = resizable;
	}
	
}
