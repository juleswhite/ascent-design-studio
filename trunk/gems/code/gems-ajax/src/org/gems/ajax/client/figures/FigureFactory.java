package org.gems.ajax.client.figures;

import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.EditPartManager;
import org.gems.ajax.client.figures.templates.Template;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;

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

public class FigureFactory {

	public static ConnectableDiagramElement createFigure(Template t, EditPart ep){
		DiagramPanel p = new DiagramPanel(ep.getDiagram());
		String html = "<p>Attributes:</p>";//t.getHtml();
		
		HTML w = new HTML(html);
		((AbsolutePanel)p.getBodyPanel()).add(w);
		EditPartManager.mapPartToElement(w.getElement(), ep);
		return p;
	}
}
