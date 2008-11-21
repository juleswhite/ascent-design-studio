package org.ascent.binpacking;
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
public abstract class AbstractState {
	protected Object item_;
	protected int[] size_;
	protected double weight_ = 0;
	
	public double getWeight() {
		return weight_;
	}

	public void setWeight(double weight) {
		weight_ = weight;
	}

	public int[] getSize() {
		return size_;
	}

	public void setSize(int[] size) {
		size_ = size;
	}
	
	public String toString() {
		String w = "[";
		for (int i = 0; i < size_.length; i++) {
			w += size_[i];
			if (i != size_.length - 1)
				w += ",";
		}
		w += "]";
		return w;
	}

	public Object getItem() {
		return item_;
	}

	public void setItem(Object item) {
		item_ = item;
	}
	
}
