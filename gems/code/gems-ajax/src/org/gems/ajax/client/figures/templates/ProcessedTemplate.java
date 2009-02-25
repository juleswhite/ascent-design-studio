package org.gems.ajax.client.figures.templates;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ProcessedTemplate {
	private String html_;
	private List<TemplateElement> elementsToLoad_ = new ArrayList<TemplateElement>();

	public ProcessedTemplate(String html, List<TemplateElement> elementsToLoad) {
		super();
		html_ = html;
		elementsToLoad_ = elementsToLoad;
	}

	public String getHtml() {
		return html_;
	}

	public void setHtml(String html) {
		html_ = html;
	}

	public List<TemplateElement> getElementsToLoad() {
		return elementsToLoad_;
	}

	public void setElementsToLoad(List<TemplateElement> elementsToLoad) {
		elementsToLoad_ = elementsToLoad;
	}

}
