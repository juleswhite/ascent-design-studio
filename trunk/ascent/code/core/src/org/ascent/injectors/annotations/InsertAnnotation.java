package org.ascent.injectors.annotations;

import java.util.Map;

import org.ascent.injectors.BasicTemplateInjector;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class InsertAnnotation extends AbstractAnnotation {

	public static final String TYPE = "insert";
	
	
	public InsertAnnotation(){
		super(TYPE);
	}
	
	/* (non-Javadoc)
	 * @see org.refresh.injectors.xml.AbstractAnnotation#handleImpl(java.lang.String, java.util.Map, java.util.Map)
	 */
	@Override
	protected String handleImpl(String template, Map options, Map bindings, Map<String,Map> gbinds) {
		return BasicTemplateInjector.getInstance().inject(template, ""+bindings.get(FEATURE_KEY), gbinds);
	}

	
}
