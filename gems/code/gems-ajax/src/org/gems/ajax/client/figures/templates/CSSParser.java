package org.gems.ajax.client.figures.templates;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * Copyright (c) 2005, 2006 Jules White.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jules White - initial API and implementation 
 ****************************************************************************/
public class CSSParser {
	
	public static List<AttributeSet> parseAttributeSets(String styles){
		return parseAttributeSets(styles,false,true);
	}
	
	
	public static List<AttributeSet> parseAttributeSets(String styles, boolean keepescapes, boolean overwritedups){
		styles = styles.trim();
		styles = styles.replaceAll("/\\*[^/]*?\\*/", "");
		ArrayList<AttributeSet> sets = new ArrayList<AttributeSet>();
		StringTokenizer tk = new StringTokenizer(styles,"{}:;",true);
		while(tk.hasMoreTokens()){
			sets.add(parseAttributes(tk,keepescapes,overwritedups));
		}
		return sets;
	}
	
	public static AttributeSet parseAttributes(StringTokenizer style){
		return parseAttributes(style, false, true);
	}
	
	public static AttributeSet parseAttributes(StringTokenizer style, boolean preserveescpaes, boolean overwritedups){
		String selector = style.nextToken().trim();
	
		//remove the first {
		style.nextToken();
		
		boolean inval = false;
		String token = null;
		String key = null;
		String val = "";
		AttributeSet props = new AttributeSet(selector);
		while(style.hasMoreTokens()){
			
			token = style.nextToken();
			
			token = token.trim();
			if(token.length() < 1)
				continue;
			
			if(token.equals("}")){
				break;
			}
			if(token.equals(":"))
				inval = true;
			if(token.equals(";")){
				props.put(key,val.trim());
//				setProperty(key.trim(), val.trim(), props, overwritedups);
				
				key = null;
				inval = false;
				val = "";
				continue;
			}
			if(token.equals("{") && inval){
				token = parseEscapedValue(style);
				if(preserveescpaes){
					token = "{"+token+"}";
				}
			}
			if(!token.equals(";")
					&& !token.equals(":")){
				if(key == null){
					key = token.trim();
				}
				else{
					val += token;
					
				}
			}
			
		}
		
		return props;
	}
	
	
	public static String parseEscapedValue(StringTokenizer tk){
		int lhs = 1;
		
		String val = "";
		while(lhs > 0 && tk.hasMoreTokens()){
			String token = tk.nextToken();
			if(token.equals("{"))
				lhs++;
			else if(token.equals("}"))
				lhs--;
			else
				val += token;
		}
		return val;
	}
	
	public static List<AttributeSet> parseAttributes(String reqcss, boolean preserveescapes, boolean overwritedups){
		
		List<AttributeSet> attrs = parseAttributeSets(reqcss,preserveescapes,overwritedups);
		return attrs;
	}

	public static List<AttributeSet> parseAttributes(String reqcss){
		return parseAttributes(reqcss,false,true);
	}
	

}

