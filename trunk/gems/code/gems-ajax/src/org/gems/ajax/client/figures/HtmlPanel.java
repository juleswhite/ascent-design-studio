package org.gems.ajax.client.figures;


import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.figures.templates.Template;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
public class HtmlPanel extends DiagramPanel {

	private List<HtmlPanelListener> listeners_ = new ArrayList<HtmlPanelListener>();
	private Template template_;
	private VerticalPanel panel_ = new VerticalPanel();
	private HTMLPanel htmlPanel_;
	
	public HtmlPanel(GEMSDiagram diagram, Template template, boolean moveable) {
		super(diagram, true, moveable, true, false, true);
		template_ = template;
		
		htmlPanel_ = new HTMLPanel(template_.getHtml());
		panel_.add(htmlPanel_);
		panel_.setSize("100%", "100%");
		panel_.setStyleName("debug");
		
		init();

//		addTitleBar();

		setResizeable(true);
		setCollapsible(false);
		setMoveable(moveable);
	}

	public void onDeSelect() {
	}

	public void onSelect() {
		System.out.println("Selected");
	}

	public HTMLPanel getBodyHTML() {
		return htmlPanel_;
	}
	
	public void setBodyHtml(HTMLPanel p){
		panel_.remove(htmlPanel_);
		htmlPanel_ = p;
		panel_.add(htmlPanel_);
	}

	public void getBodyHTML(HTMLPanel htmlPanel) {
		htmlPanel_ = htmlPanel;
	}

	public Widget createBodyPanel() {
		return panel_;
	}

	public void setHeight(String height) {
		super.setHeight(height);
		htmlPanel_.setHeight(height);
		panel_.setHeight(height);
	}

	public void setWidth(String width) {
		super.setWidth(width);		
		htmlPanel_.setWidth(width);
		panel_.setWidth(width);
	}
	
	public void setHtml(String html){
		panel_.remove(htmlPanel_);
		htmlPanel_ = new HTMLPanel(html);
		panel_.add(htmlPanel_);
	}

	public void resize(String w, String h) {
		for(HtmlPanelListener l : listeners_)
			l.resizeRequested(w, h);
		panel_.remove(htmlPanel_);
		super.resize(w, h);		
		panel_.add(htmlPanel_);
	}

	public boolean addHtmlPanelListener(HtmlPanelListener e) {
		return listeners_.add(e);
	}

	public boolean removeHtmlPanelListener(Object o) {
		return listeners_.remove(o);
	}
	
	
}
