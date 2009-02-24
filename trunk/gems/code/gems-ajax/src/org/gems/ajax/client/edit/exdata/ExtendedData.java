package org.gems.ajax.client.edit.exdata;

import java.util.HashMap;
import java.util.Map;

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

public class ExtendedData {

	private Map<String,String> data_;
	
	public String get(String key){
		if(data_ == null){
			return null;
		}
		return data_.get(key);
	}
	
	public <T> T get(String key, DataType<T> conv){
		String d = get(key);
		return conv.fromString(d);
	}
	
	public void put(String key, String data){
		if(data_ == null)
			data_ = new HashMap<String, String>();
		
		data_.put(key, data);
	}
	
	public <T> void put(String key, T data, DataType<T> conv){
		put(key,conv.toString(data));
	}
}
