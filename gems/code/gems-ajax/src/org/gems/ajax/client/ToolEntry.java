/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.gems.ajax.client;

import org.gems.ajax.client.edit.Tool;

public class ToolEntry {

	private String icon_;
	private String label_;
	private String toolTip_;
	private String activatedStyle_ = "active-tool-entry";
	private String deActivatedStyle_ = "tool-entry";
	private Tool tool_;

	public ToolEntry(String icon, String label, String toolTip,
			String activatedStyle, String deActivatedStyle, Tool tool) {
		super();
		icon_ = icon;
		label_ = label;
		toolTip_ = toolTip;
		activatedStyle_ = activatedStyle;
		deActivatedStyle_ = deActivatedStyle;
		tool_ = tool;
	}
	
	public ToolEntry(String icon, String label, String toolTip, Tool tool) {
		super();
		icon_ = icon;
		label_ = label;
		toolTip_ = toolTip;
		tool_ = tool;
	}

	public String getIcon() {
		return icon_;
	}

	public void setIcon(String icon) {
		icon_ = icon;
	}

	public String getLabel() {
		return label_;
	}

	public void setLabel(String label) {
		label_ = label;
	}

	public String getToolTip() {
		return toolTip_;
	}

	public void setToolTip(String toolTip) {
		toolTip_ = toolTip;
	}

	public Tool getTool() {
		return tool_;
	}

	public void setTool(Tool tool) {
		tool_ = tool;
	}

	public String getActivatedStyle() {
		return activatedStyle_;
	}

	public void setActivatedStyle(String activatedStyle) {
		activatedStyle_ = activatedStyle;
	}

	public String getDeActivatedStyle() {
		return deActivatedStyle_;
	}

	public void setDeActivatedStyle(String deActivatedStyle) {
		deActivatedStyle_ = deActivatedStyle;
	}



}
