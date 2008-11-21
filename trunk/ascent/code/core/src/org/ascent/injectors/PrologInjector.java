package org.ascent.injectors;

import org.ascent.injectors.annotations.ForEachAnnotation;
import org.ascent.injectors.annotations.InsertAnnotation;
import org.ascent.injectors.annotations.ReplaceAnnotation;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class PrologInjector extends CommentBasedInjector {

	public static PrologInjector createDefaultInjector() {
		PrologInjector injector = new PrologInjector();
		injector.addAnnotationHandler(new ForEachAnnotation());
		injector.addAnnotationHandler(new InsertAnnotation());
		injector.addAnnotationHandler(new ReplaceAnnotation());
		return injector;
	}

	public static final String COMMENT_START = "%";
	

	public PrologInjector() {
		super(COMMENT_START,COMMENT_START);
	}

}
