package org.ascent.injectors.annotations;

import java.util.Map;

import org.ascent.injectors.XMLInjector;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class ReplaceAnnotation extends AbstractAnnotation {

	public static final String TYPE = "replace";

	public ReplaceAnnotation() {
		super(TYPE);
	}

	@Override
	protected String handleImpl(String template, Map options, Map bindings, Map<String,Map> gbinds) {
		String with = (String) options.get(XMLInjector.WITH_KEY);
		if (with == null || (options != null && options.size() > 2)) {
			for (Object key : options.keySet()) {
				String target = "" + key;
				String replacementval = "" + options.get(key);
				if(replacementval.equals("${with}")){
					replacementval = with;
				}
				else{
					replacementval = getTemplateInjector().inject(replacementval,""+bindings.get(FEATURE_KEY), gbinds);
				}
				
				String[] regexs = getTargetRegex(target, replacementval);
				
				if(regexs == null)
					continue;
				
				String replaceex = regexs[0];
				replacementval = regexs[1];
				
				if(replaceex == null){
					continue;
				}

				if(replacementval.indexOf("${") > -1){
					throw new InvalidInjectionException("The injection value for the key "+key+" contains unbound variables. The injection value is \""+replacementval+"\"");
				}
				
				template = template.replaceAll(replaceex, replacementval);
			}
		}
		else{
			template = with;
		}
		return template;
	}
	
	public String[] getTargetRegex(String target, String replacement){
		return new String[]{"\\$\\{"+target+"\\}",replacement};
	}

}
