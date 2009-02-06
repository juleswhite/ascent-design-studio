package org.ascent.deployment.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import org.ascent.deployment.Component;
import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.Node;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class ExcelDeploymentConfig extends WorksheetManipulator {

	private List<WorksheetHandler> handlers_ = new ArrayList<WorksheetHandler>();

	public ExcelDeploymentConfig() {
		handlers_.add(new NodeHandler());
		handlers_.add(new ComponentHandler());
		handlers_.add(new ComponentSchedulingHandler());
		handlers_.add(new ComponentColocationHandler());
		handlers_.add(new NetworkHandler());
		handlers_.add(new InteractionHandler());
		handlers_.add(new ValidHostsHandler());
	}

	public void load(File f, DeploymentConfig problem) throws Exception {
		Workbook workbook = Workbook.getWorkbook(f);

		try {
			HashMap<String, Component> complookup = new HashMap<String, Component>();
			HashMap<String, Node> nodelookup = new HashMap<String, Node>();

			for (WorksheetHandler handler : handlers_) {
				Sheet sheet = getSheet(workbook, handler.getWorksheetName(),
						handler.isOptionalWorksheet());
				handler.handleSheet(problem, sheet, complookup, nodelookup);
			}
			
		} finally {
			workbook.close();
		}
	}

}
