package org.ascent.injectors;

import org.ascent.injectors.annotations.AnnotationHandler;

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
public interface AnnotationBasedInjector extends Injector {

	public void addAnnotationHandler(AnnotationHandler h);
	
	public void addAnnotationHandler(String type, AnnotationHandler h) ;

	public void removeAnnotationHandler(AnnotationHandler h) ;
	
	public void removeAnnotationHandler(String type) ;
}
