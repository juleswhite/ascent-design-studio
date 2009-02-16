package org.ascent.deployment.excel.output;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.ascent.VectorSolution;
import org.ascent.deployment.Component;
import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.DeploymentPlan;
import org.ascent.deployment.Node;
import org.ascent.deployment.excel.handlers.DeploymentPlanHandler;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class ExcelDeploymentPlan {

	public static void write(DeploymentPlan plan, File target) throws IOException {
		try {

			WritableWorkbook workbook = Workbook.createWorkbook(target);

			WritableSheet sheet = workbook.createSheet(DeploymentPlanHandler.DEPLOYMENT_PLAN_SHEET, 0);

			Component[] comps = plan.getDeploymentConfiguration()
			.getComponents();
			Node[] nodes = plan.getDeploymentConfiguration().getNodes();
			
			Map<Node,Integer> indices = new HashMap<Node, Integer>();
			for(int i = 0; i < nodes.length; i++){
				indices.put(nodes[i],i+1);
				
				Label label = new Label(i+1, 0, nodes[i].getLabel());
				sheet.addCell(label);
			}
			
			for (int i = 0; i < comps.length; i++) {

				Component c = comps[i];
				Label label = new Label(0, i+1, c.getLabel());
				sheet.addCell(label);

				Number number = new Number(indices.get(plan.getHost(c)), i+1, 1);
				sheet.addCell(number);
			}

			
			workbook.write(); 
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DeploymentPlan read(File src, DeploymentConfig conf) throws Exception {
		return read(src,DeploymentPlanHandler.DEPLOYMENT_PLAN_SHEET,conf);
	}
	
	public static DeploymentPlan read(File src, String worksheet, DeploymentConfig conf) throws Exception {
		Workbook workbook = Workbook.getWorkbook(src);
		DeploymentPlan dplan = null;

		try {
			Sheet sheet = workbook.getSheet(worksheet);
			
			dplan = new DeploymentPlan(conf,new VectorSolution(new int[conf.getComponents().length]));
			
			Map<String, Component> comps = new HashMap<String, Component>();
			Map<String, Node> nodes = new HashMap<String, Node>();
			for(Component c : conf.getComponents()){
				comps.put(c.getLabel(), c);
			}
			for(Node n : conf.getNodes()){
				nodes.put(n.getLabel(), n);
			}
			
			(new DeploymentPlanHandler()).handleSheetImpl(dplan, sheet, comps, nodes);

		} finally {
			workbook.close();
		}
		
		return dplan;
	}
}
