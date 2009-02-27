package org.gems.ajax.client.figures;

import org.gems.ajax.client.figures.templates.Template;
import org.gems.ajax.client.util.dojo.DojoUtil;

import com.google.gwt.user.client.ui.HTMLPanel;

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

public class DiagramTemplatePanel extends AbstractDiagramElement {

	private Template template_;
	private HTMLPanel htmlPanel_;
	
	public DiagramTemplatePanel(GEMSDiagram diagram, Template template, boolean moveable) {
		super(diagram);
		template_ = template;
		
		htmlPanel_ = new HTMLPanel(template_.getHtml());
		add(htmlPanel_);
		
		if(moveable)
			DojoUtil.makeMoveable(getElement(),this);
	}

	public void onDeSelect() {
	}

	public void onSelect() {
		System.out.println("Selected");
	}

	public HTMLPanel getBodyHTML() {
		return htmlPanel_;
	}

	public void getBodyHTML(HTMLPanel htmlPanel) {
		htmlPanel_ = htmlPanel;
	}

}
