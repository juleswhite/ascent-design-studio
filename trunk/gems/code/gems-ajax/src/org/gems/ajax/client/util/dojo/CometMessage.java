package org.gems.ajax.client.util.dojo;

import java.util.HashMap;

import com.google.gwt.core.client.JavaScriptObject;

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
public class CometMessage extends JavaScriptObject {
	
	protected CometMessage() {}
	
	public final native String getString(String key) /*-{ return this[key]; }-*/;
	
	public final native String getInt(String key) /*-{ return this[key]; }-*/;
	
	public final native String getBoolean(String key) /*-{ return this[key]; }-*/;
	
	public final native String getDouble(String key) /*-{ return this[key]; }-*/;
	
	public final native int size() /*-{ return this.length; }-*/;
	 
	public final native void put(String key, String data)/*-{ this[key] = data; }-*/;
	
	public final HashMap<String, String> asMap(){
		HashMap<String, String> map = new HashMap<String, String>();
		copyTo(map);
		return map;
	}
	
	public final native void copyTo(HashMap map)/*-{
	    for(key in this){
		  map.@java.util.Map::put(Ljava/lang/Object;Ljava/lang/Object;)(key,this[key]);
		}
	}-*/;
}
