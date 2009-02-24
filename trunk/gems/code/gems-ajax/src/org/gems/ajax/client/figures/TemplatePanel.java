package org.gems.ajax.client.figures;

import org.gems.ajax.client.edit.EditPartManager;
import org.gems.ajax.client.event.UIEventConnector;
import org.gems.ajax.client.figures.templates.Template;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
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

public class TemplatePanel extends DiagramPanel {

	private class InitialResize implements Command{
		private String width_;
		private String height_;
		
		public InitialResize(String w, String h){
			width_ = w;
			height_ = h;
		}

		public void execute() {
			setSize(width_, height_);
		}		
	}
	
	private Template template_;
	private HTMLPanel body_;
	
	public TemplatePanel(GEMSDiagram diagram, boolean withheader,
			boolean moveable, boolean resizeable, Template template) {
		super(diagram, withheader, template.getMoveable(), template.getResizable(), false);
		template_ = template;
//		setAutoSize(false);
		createHTMLPanel();
		setStylePrimaryName(template.getStylePrimaryName());
		getBodyHTML().setStylePrimaryName(template.getStylePrimaryName());
		
		String w = template_.getRootAttributes().get("initwidth");
		String h = template_.getRootAttributes().get("initheight");
		if(w != null && h != null){
			DeferredCommand.addCommand(new InitialResize(w,h));
		}
	}

	public TemplatePanel(GEMSDiagram diagram, boolean withheader, Template template) {
		this(diagram, withheader, true, false, template);
	}

	public TemplatePanel(GEMSDiagram diagram, Template template) {
		this(diagram, false, true, false, template);
	}

	protected Widget createBodyPanel() {
		return new AbsolutePanel();
	}
	
	protected void createHTMLPanel(){
		body_ = new HTMLPanel(template_.getHtml());
		((AbsolutePanel)getBodyPanel()).add(body_);
	}

	public HTMLPanel getBodyHTML(){
		return body_;
	}
}
