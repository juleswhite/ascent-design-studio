/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.probe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gmt.gems.css.parser.Attributes;
import org.eclipse.gmt.gems.css.parser.CSSParser;

public class Target {

	public static final String NAME_KEY = "name";
	
	public static Target loadFrom(Attributes attrs) {
		Target node = new Target();
		node.setName(attrs.getSelector());
		node.addResources(NAME_KEY, attrs.getSelector().trim());
		for (Object key : attrs.keySet()) {
			node.addResources("" + key, attrs.getProperty("" + key));
		}
		return node;
	}
	
	public static List<Target> loadTargetsFrom(List<Attributes> attrs) {
		List<Target> targets = new ArrayList<Target>();
		for(Attributes attrset : attrs)
			targets.add(loadFrom(attrset));
		
		return targets;
	}
	
	public static List<Target> loadTargetsFrom(String attrs) {
		return loadTargetsFrom(CSSParser.parseAttributes(attrs));
	}

	public static Target loadFrom(String attrs) {
		return loadFrom(CSSParser.parseAttributes(attrs).get(0));
	}

	public static Target loadFrom(Class c, String id) throws IOException {
		String attrs = null;//IOUtils.toString(c.getResourceAsStream(id));
		return loadFrom(CSSParser.parseAttributes(attrs).get(0));
	}

	private Map resources_ = new HashMap();

	private String name_;

	private int memory_;

	private String id_;

	private boolean isLocalHost_ = false;

	public String getName() {
		return name_;
	}

	public void setName(String name) {
		name_ = name;
	}
	
	

	public Map getResources() {
		return resources_;
	}

	public void setResources(Map resources) {
		resources_ = resources;
	}

	public void addResources(String name, String val) {
		resources_.put(name, val);
	}

	public int getMemory() {
		return memory_;
	}

	public void setMemory(int memory) {
		memory_ = memory;
	}

	public String getId() {
		return id_;
	}

	public void setId(String id) {
		id_ = id;
	}

	public String toString() {
		return "Node:" + id_;
	}

	public boolean isLocalHost() {
		return isLocalHost_;
	}

	public void setLocalHost(boolean lc) {
		isLocalHost_ = lc;
	}
}
