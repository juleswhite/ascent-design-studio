package org.ascent.binpacking.test;
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
public class ProblemConfiguration {

	public int dims = 3;
	public int srcs = 1000;
	public int trgs = 50;
	public int srmin = 1;
	public int srmax = 100;
	public int trmin = 100;
	public int trmax = 10000;
	public double reqcthreshold = 50;
	public double reqethreshold = 49;
	public int reqmax = 5;
	public int reqmin = 0;
}
