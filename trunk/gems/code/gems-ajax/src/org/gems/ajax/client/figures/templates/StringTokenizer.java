package org.gems.ajax.client.figures.templates;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class StringTokenizer {
	private int pos_ = 0;
	private String toTokenize_;
	private String delimiters_;
	private boolean returnDelimiters_;

	public StringTokenizer(String toTokenize, String delimiters,
			boolean returnDelimiters) {
		super();
		toTokenize_ = toTokenize;
		delimiters_ = delimiters;
		returnDelimiters_ = returnDelimiters;
	}
	
	public boolean hasMoreTokens(){
		return pos_ < toTokenize_.length();
	}
	
	public String nextToken(){
		int mark = pos_;
		
		for(int i = pos_; i < toTokenize_.length(); i++){
			char c = toTokenize_.charAt(i);
			if(delimiters_.contains(""+c)){
				if(pos_ == i && returnDelimiters_){
					mark = i+1;
					break;
				}
				else if(pos_ == i && !returnDelimiters_){
					pos_ = i+1;
				}
				else {
					mark = i;
					break;
				}
			}
		}
		
		
		String token = toTokenize_.substring(pos_,mark);
		pos_ = mark;
		
		return token;
	}
}
