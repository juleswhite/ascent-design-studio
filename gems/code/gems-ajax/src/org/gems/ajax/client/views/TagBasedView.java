package org.gems.ajax.client.views;

import java.util.List;

import org.gems.ajax.client.model.ModelHelper;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class TagBasedView extends AbstractView {

	private String tag_;
	private ModelHelper modelHelper_;

	public TagBasedView(String id, String tag, ModelHelper mh, Object root) {
		super(id, root);
		modelHelper_ = mh;
		tag_ = tag;
	}

	public TagBasedView(String id, String tag, ModelHelper mh) {
		this(id, tag, mh, null);
	}

	public boolean isVisible(Object mo) {
		List<String> tags = modelHelper_.getTags(mo);
		return tags != null && tags.contains(tag_);
	}

	public View clone(Object nrootobj) {
		return new TagBasedView(getId(),tag_,modelHelper_,nrootobj);
	}
	
	
}
