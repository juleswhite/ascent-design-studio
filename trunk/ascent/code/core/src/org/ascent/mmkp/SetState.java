package org.ascent.mmkp;

import java.util.List;

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
public class SetState {
	private int index_;
	private Object currentItem_;
	private List set_;
	
	public SetState(List set) {
		super();
		set_ = set;
		if(set_.size() > 0)
			currentItem_ = set_.get(0);
	}
	
	public SetState(List set, int index) {
		super();
		set_ = set;
		index_ = index;
		if(set_.size() > 0)
			currentItem_ = set_.get(0);
	}

	public SetState(Object currentItem, List set) {
		super();
		currentItem_ = currentItem;
		set_ = set;
	}

	public Object getCurrentItem() {
		return currentItem_;
	}

	public void setCurrentItem(Object currentItem) {
		currentItem_ = currentItem;
	}

	public List getSet() {
		return set_;
	}

	public void setSet(List set) {
		set_ = set;
	}

	public int getIndex() {
		return index_;
	}

	public void setIndex(int index) {
		index_ = index;
	}

	
}
