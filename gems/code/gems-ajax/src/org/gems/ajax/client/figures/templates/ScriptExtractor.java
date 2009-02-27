package org.gems.ajax.client.figures.templates;

import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.figures.templates.TemplateParser.Element;
import org.gems.ajax.client.figures.templates.TemplateParser.Token;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ScriptExtractor {

	private static final String START_SCRIPT = "<script";
	private static final String END_SCRIPT = "</script>";

	private static TemplateParser parser_ = new TemplateParser();

	public static List<TemplateElement> extractLoadableElements(String html) {
		ArrayList<TemplateElement> els = new ArrayList<TemplateElement>();
		els.addAll(extractScripts(html));
		els.addAll(extractCSSLinks(html));
		return els;
	}

	public static List<TemplateScript> extractScripts(String html) {
		ArrayList<TemplateScript> scripts = new ArrayList<TemplateScript>();
		int start = html.indexOf(START_SCRIPT);

		while (start > -1) {
			try {
				Token token = parser_.parseAnyElement(html.substring(start));

				Element el = (Element) token.data;
				String spath = el.attributes.get("src");
				String upfunc = el.attributes.get("onupdate");
				String checkfunc = el.attributes.get("readyif");
				String script = null;
				String initf = el.attributes.get("init");

				if (!el.selfTerminating && spath==null) {
					int end = html.indexOf(END_SCRIPT, start + 1);
					script = html.substring(start, end);
				}

				scripts.add(TemplateScript.getScript(spath, script, initf, upfunc, checkfunc));
				start = html.indexOf(START_SCRIPT, start
						+ START_SCRIPT.length() + END_SCRIPT.length());
			} catch (Exception e) {
				break;
			}
		}

		return scripts;
	}

	public static List<TemplateCSS> extractCSSLinks(String html) {
		ArrayList<TemplateCSS> css = new ArrayList<TemplateCSS>();
		int start = html.indexOf("<link ");

		TemplateParser parser = new TemplateParser();

		while (start > -1) {
			try {
				Token token = parser.parseAnyElement(html.substring(start));
				Element tag = (Element) token.data;

				String type = tag.attributes.get("type");
				if ("text/css".equals(type)) {
					String href = tag.attributes.get("href");
					if (href != null) {
						TemplateCSS cssel = TemplateCSS.getCSS(href);
						css.add(cssel);
					}
				}

				start = html.indexOf("<link ", start + 6);
			} catch (Exception e) {
				break;
			}
		}

		return css;
	}
	
	public static String[] stripSection(String html, String stag, String etag){
		String sec = "";
		
		int start = html.indexOf(stag);
		if(start > -1){
			int end = html.lastIndexOf(etag);
			if(end > start){
				sec = html.substring(start+stag.length(),end);
				html = html.substring(0,start)+html.substring(end+etag.length());
			}
		}
		
		return new String[]{html,sec};
	}
	
	public static ProcessedTemplate processTemplate(String html){
		String[] parts = stripSection(html, "<head>", "</head>");
		String body = stripSection(parts[0], "<body>", "</body>")[1];
		return new ProcessedTemplate(body,extractLoadableElements(parts[1]));
	}
}
