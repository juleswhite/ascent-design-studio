package org.ascent.expr;
/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class Cardinality {
	
	public static final Cardinality UNBOUNDED = new Cardinality(0,Integer.MAX_VALUE);
	
	public static Cardinality parseCardinality(String card){
		int min = 0;
		int max = Integer.MAX_VALUE;
		if(card.indexOf("..") > 0){
			int start = card.indexOf("..");
			int end = start + 2;
			String minstr = card.substring(0,start);
			String maxstr = card.substring(end);
			if(maxstr.equals("*")){
				maxstr = ""+Integer.MAX_VALUE;
			}
			min = Integer.parseInt(minstr);
			max = Integer.parseInt(maxstr);
		}
		else{
			if(!card.equals("*")){
				int val = Integer.parseInt(card);
				min = val;
				max = val;
			}
		}
		return new Cardinality(min,max);
	}
	private int min_;
	private int max_;
	public Cardinality(int min, int max){
		min_ = min;
		max_ = max;
	}
	public int getMax() {
		return max_;
	}
	public void setMax(int max) {
		max_ = max;
	}
	public int getMin() {
		return min_;
	}
	public void setMin(int min) {
		min_ = min;
	}
	
	public String toString(){
		return "["+getMin()+".."+getMax()+"]";
	}
	
}
