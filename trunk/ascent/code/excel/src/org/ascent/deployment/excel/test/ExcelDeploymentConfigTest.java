package org.ascent.deployment.excel.test;

import java.io.File;

import org.ascent.deployment.excel.ExcelDeploymentConfig;

import junit.framework.TestCase;

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
public class ExcelDeploymentConfigTest extends TestCase {

	public void testWorkbookLoad(){
		ExcelDeploymentConfig config = new ExcelDeploymentConfig();
		try{
			config.load(new File("data/test.xls"));
		}catch (Exception e) {
			e.printStackTrace();
			fail("An exception was thrown loading the workbook and should not have been.");
		}
	}
}
