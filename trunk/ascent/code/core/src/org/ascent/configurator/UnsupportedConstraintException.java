package org.ascent.configurator;
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
public class UnsupportedConstraintException extends RuntimeException {

	public UnsupportedConstraintException() {
		super();
	}

	public UnsupportedConstraintException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnsupportedConstraintException(String arg0) {
		super(arg0);
	}

	public UnsupportedConstraintException(Throwable arg0) {
		super(arg0);
	}

}
