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
public class ScriptExtractor {

	private static final String START_SCRIPT = "<script";
	private static final String END_SCRIPT = "</script>";

	public static List<String> extractScripts(String html) {
		ArrayList<String> scripts = new ArrayList<String>();
		int start = html.indexOf(START_SCRIPT);

		while (start > -1) {
			int srcstart = html.indexOf("src=", start);
			start = html.indexOf(">", start);
			if (start > -1) {
				start = start + 1;
				int end = html.indexOf(END_SCRIPT);

				String script = null;

				if (srcstart > -1 && srcstart < start) {
					
					int spstart = html.indexOf("\"", srcstart);
					if (spstart > -1) {
						int spend = html.indexOf("\"", spstart + 1);
						if (spend > -1) {
							
							String spath = html.substring(spstart+1,spend);

							script = " "
									+ "   var old = document.getElementById('"
									+ spath
									+ "');\r\n"
									+ "   if (old == null) {\n" 
									+ "   var head = document.getElementsByTagName(\"head\")[0];\r\n"
									+ "   var script = document.createElement('script');\r\n"
									+ "   script.id = '" + spath + "';\r\n"
									+ "   script.type = 'text/javascript';\r\n"
									+ "   script.src = \"" + spath + "\";\r\n"
									+ "   head.appendChild(script);" +
									  "  }";
							scripts.add(script);
						}
					}
				} else {
					script = html.substring(start, end);
				}

				scripts.add(script);
				start = html.indexOf(START_SCRIPT, end + END_SCRIPT.length());
			} else {
				break;
			}
		}

		return scripts;
	}
}
