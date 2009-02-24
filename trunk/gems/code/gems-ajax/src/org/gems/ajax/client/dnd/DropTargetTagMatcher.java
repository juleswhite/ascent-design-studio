package org.gems.ajax.client.dnd;

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

public class DropTargetTagMatcher implements DropTargetMatcher {

	private String tag_;
	
	public DropTargetTagMatcher(String tag) {
		super();
		tag_ = tag;
	}

	public boolean matches(DropTarget t) {
		return t.getTags().contains(tag_);
	}

}
