package org.ascent.binpacking.test;

import junit.framework.Test;
import junit.framework.TestSuite;

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
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.ascent.binpacking.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(WikiExampleADwMRTSC.class);
		suite.addTestSuite(FFDCoreTest.class);
		//$JUnit-END$
		return suite;
	}

}
