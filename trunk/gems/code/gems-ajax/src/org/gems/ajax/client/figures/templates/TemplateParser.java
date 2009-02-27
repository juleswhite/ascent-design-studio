package org.gems.ajax.client.figures.templates;

import java.util.HashMap;
import java.util.Map;

import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.Type;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class TemplateParser {
	
	public class Element {
		public Map<String, String> attributes = new HashMap<String, String>(3);
		public String name;
		public boolean selfTerminating = false;
	}

	public class Token {
		public String remainder;
		public Object data;
	}

	public Template parse(ModelHelper mh, String tstr) {
		Token t = parseAnyElement(tstr);

		Template template = new Template();
		Element e = (Element)t.data;
		
		String eptype = e.attributes.get(Template.TYPE_ATTR);
		if(eptype == null)
			throw new TemplateParseException("The root element of each template must specify a value for the attribute \""+Template.TYPE_ATTR+"\"");
		
		template.setEditPartId(eptype);
		String cmap = e.attributes.get(Template.CHILD_MAPPINGS_ATTR);
		if(cmap != null){
			template.setContainerIds(new HashMap<Type, String>());
			Map<String,String> childmap = parseMap(cmap);
//			for(String key : childmap.keySet()){
//				Type type = mh.getTypeForName(key.trim());
//				if(type != null){
//					template.getContainerIds().put(type, childmap.get(key));
//				}
//			}
		}
		template.setHtml(t.remainder);
		template.setStylePrimaryName(e.attributes.get(Template.STYLE_PRIMARY_NAME));
		template.setResizable("true".equalsIgnoreCase(e.attributes.get(Template.RESIZABLE)));
		template.setMoveable("true".equalsIgnoreCase(e.attributes.get(Template.MOVEABLE)));
		template.setRootAttributes(e.attributes);
		return template;
	}

	public Token parseAnyElement(String t) {
		int start = t.indexOf("<");
		if (start < 0)
			throw new TemplateParseException("Expected template text:{" + t
					+ "} to begin with an element (missing '<').");

		int end = t.indexOf(">", start + 1);
		if (end < 0)
			throw new TemplateParseException("Expected template text:{" + t
					+ "} to begin with an element (missing '>').");

		String remainder = t.substring(end + 1);
		String stag = t.substring(start + 1, end).trim();
		boolean sterm = stag.endsWith("/");
		if (sterm)
			stag = stag.substring(0, stag.length() - 1);
		String ename = null;
		if (stag.indexOf(" ") > 0) {
			int nstop = stag.indexOf(" ");
			ename = stag.substring(0, nstop);
			stag = stag.substring(nstop);
		} else {
			ename = stag;
			stag = "";
		}

		stag = stag.trim();

		if (!sterm) {
			int endstart = remainder.lastIndexOf("<");
			int endend = remainder.lastIndexOf(">");
			if (endstart > endend || endstart < 0 || endend < 0) {
				throw new TemplateParseException(
						"Missing end tag for element <" + ename
								+ "> in template text:{" + t + "}.");
			}
			remainder = remainder.substring(0, endstart);
		}

		Element e = new Element();
		e.name = ename;
		e.selfTerminating = sterm;
		e.attributes = parseAttributes(stag);

		Token tk = new Token();
		tk.remainder = remainder;
		tk.data = e;

		return tk;
	}

	public Map<String, String> parseAttributes(String e) {
		if (e == null || e.length() < 1)
			return new HashMap<String, String>();

		Map<String,String> attrs = new HashMap<String, String>();
		int nameend = 0;
		int vbegin = 0;
		int vend = 0;

		while (e.length() > 0) {
			nameend = e.indexOf("=");

			if (nameend < 0)
				throw new TemplateParseException("Invalid attribute spec:{" + e
						+ "}");

			vbegin = e.indexOf("\"", nameend + 1);

			if (vbegin < 0)
				throw new TemplateParseException("Invalid attribute spec:{" + e
						+ "}");

			vend = e.indexOf("\"", vbegin + 1);

			if (vend < 0)
				throw new TemplateParseException("Invalid attribute spec:{" + e
						+ "}");
			
			String name = e.substring(0,nameend).trim();
			String value = e.substring(vbegin+1,vend);
			attrs.put(name,value);
			e = e.substring(vend+1).trim();
		}

		return attrs;
	}
	
	public Map<String, String> parseMap(String e) {
		if (e == null || e.length() < 1)
			return new HashMap<String, String>();

		Map<String,String> attrs = new HashMap<String, String>();
		int nameend = 0;
		int vbegin = 0;
		int vend = 0;

		while (e.length() > 0) {
			nameend = e.indexOf(":");

			if (nameend < 0)
				throw new TemplateParseException("Invalid mapping spec:{" + e
						+ "}");

			vbegin = nameend + 1;

			if (vbegin > e.length()-1)
				throw new TemplateParseException("Invalid mapping spec:{" + e
						+ "}");

			vend = e.indexOf(",", vbegin + 1);
			if(vend < 0 && vbegin < e.length()-1)
				vend = e.length();

			if (vend < 0)
				throw new TemplateParseException("Invalid mapping spec:{" + e
						+ "}");
			
			String name = e.substring(0,nameend).trim();
			String value = e.substring(vbegin,vend);
			attrs.put(name,value);
			if(e.length() > vend+1)
				e = e.substring(vend+1).trim();
			else 
				e = "";
		}

		return attrs;
	}
}
