package org.ascent.injectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ascent.injectors.annotations.AnnotationHandler;
import org.ascent.injectors.annotations.ForEachAnnotation;
import org.ascent.injectors.annotations.IfEnabledAnnotation;
import org.ascent.injectors.annotations.InjectionException;
import org.ascent.injectors.annotations.InsertAnnotation;
import org.ascent.injectors.annotations.InvalidInjectionException;
import org.ascent.injectors.annotations.XMLReplaceAnnotation;
import org.ascent.util.ParsingUtil;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class XMLInjector extends CommentBasedInjector {

	public static XMLInjector createDefaultInjector() {
		XMLInjector injector = new XMLInjector();
		injector.addAnnotationHandler(new ForEachAnnotation());
		injector.addAnnotationHandler(new InsertAnnotation());
		injector.addAnnotationHandler(new XMLReplaceAnnotation());
		injector.addAnnotationHandler(new IfEnabledAnnotation());
		return injector;
	}

	public static final String MULTI_LINE_COMMENT_START = "<!--";
	public static final String MULTI_LINE_COMMENT_END = "-->";
	
	public XMLInjector(){
		super(MULTI_LINE_COMMENT_START,MULTI_LINE_COMMENT_END);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.refresh.injectors.Injector#inject(java.util.Map, java.util.Map,
	 *      java.lang.String)
	 */
	protected String injectImpl(Map<String, List> features,
			Map<String, Map> values, String conf) {

		
		String result = super.injectImpl(features, values, conf);

		if (getStripSpaces()) {
			result = result.replaceAll("\\>[\\s]+\\<+", "><");
			result = result.replaceAll("[\\n]", "");
		}

		return result;
	}

}
