package org.gems.ajax.client.util.dojo;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class DojoAnimation {

	private Object dojoAnimation_;

	public DojoAnimation(Object dojoAnimation) {
		super();
		dojoAnimation_ = dojoAnimation;
	}

	public void play() {
		playImpl(dojoAnimation_);
	}

	private native void playImpl(Object anim)/*-{
			anim.play();
		}-*/;

	public Object getDojoAnimation() {
		return dojoAnimation_;
	}

	public void setDojoAnimation(Object dojoAnimation) {
		dojoAnimation_ = dojoAnimation;
	}

	public DojoAnimation chain(DojoAnimation next) {
		return new DojoAnimation(DojoUtil.chain(dojoAnimation_, next.getDojoAnimation()));
	}
}
