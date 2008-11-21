package org.ascent.injectors.annotations;

import java.util.List;
import java.util.Map;

import org.ascent.injectors.TemplateInjector;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class ForEachAnnotation extends AbstractAnnotation {

	public static final String TYPE = "for-each";

	public static final String LIST_OPTION = "list";

	public static final String AUTO_TRIM_COMMAS_OPTION = "trim-commas";

	private boolean autoTrimCommas_ = true;

	public ForEachAnnotation() {
		super(TYPE);
		addRequiredOption("list");
	}

	public boolean getAutoTrimCommas() {
		return autoTrimCommas_;
	}

	public void setAutoTrimCommas(boolean autoTrimCommas) {
		autoTrimCommas_ = autoTrimCommas;
	}

	@Override
	protected String handleImpl(String template, Map options, Map bindings, Map<String,Map> global) {
		String listname = (String) options.get(LIST_OPTION);
		List list = (List) bindings.get(listname);
		String result = "";

		TemplateInjector injector = getTemplateInjector();
		for (Object o : list) {
			result += injector.inject(template, o);
		}

		boolean trim = autoTrimCommas_;
		if (options.get(AUTO_TRIM_COMMAS_OPTION) != null) {
			trim = Boolean.parseBoolean(""
					+ options.get(AUTO_TRIM_COMMAS_OPTION));
		}

		if (trim && result.trim().endsWith(",")) {
			result = result.trim();
			result = result.substring(0, result.length()-1);
		}

		return result;
	}

}
