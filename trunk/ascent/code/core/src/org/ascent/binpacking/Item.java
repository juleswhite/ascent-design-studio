 /**************************************************************************
 * Copyright 2008 Jules White                                              *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/

package org.ascent.binpacking;

import java.util.ArrayList;
import java.util.List;

public class Item {
	private int value_;
	private int[] size_;
	private String name_;
	private List<Item> dependencies_ = new ArrayList<Item>();
	private List<Item> exclusions_ = new ArrayList<Item>();
	
	public Item(String name, int[] size){
		name_ = name;
		size_ = size;
	}
	
	public int getValue() {
		return value_;
	}
	public void setValue(int value) {
		value_ = value;
	}
	public int[] getSize() {
		return size_;
	}
	public void setSize(int[] size) {
		size_ = size;
	}
	public String getName() {
		return name_;
	}
	public void setName(String name) {
		name_ = name;
	}
	public List<Item> getDependencies() {
		return dependencies_;
	}
	public void setDependencies(List<Item> dependencies) {
		dependencies_ = dependencies;
	}
	public List<Item> getExclusions() {
		return exclusions_;
	}
	public void setExclusions(List<Item> exclusions) {
		exclusions_ = exclusions;
	}
	
	
}
