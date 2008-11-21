package org.ascent.injectors.annotations;

import java.util.Map;

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
public class IfEnabledAnnotation extends AbstractAnnotation implements
		SupportsDisable {

	public static final String TYPE = "if-enabled";
	
	public IfEnabledAnnotation() {
		super(TYPE);
	}

	
	protected String handleImpl(String template, Map options, Map bindings, Map<String,Map> gbindings) {
		return template;
	}

	
	public String handleDisabled(String template, Map options, Map bindings) {
		return "";
	}

}
