package org.ascent.deployment.excel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.ascent.deployment.Component;
import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.HardwareNode;
import org.ascent.deployment.NetworkBandwidthMinimizingPlanner;

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
public class ExcelDeploymentConfig {

	public static final String NODES_SHEET = "Nodes";
	public static final String COMPONENTS_RESOURCES_SHEET = "Component Resources";
	public static final String COMPONENTS_SCHEDULING_SHEET = "Component Scheduling";
	public static final String COMPONENTS_COLOCATION_SHEET = "Component Co-location";
	public static final String COMPONENTS_INTERACTIONS_SHEET = "Component Interactions";
	
	public void load(File f) throws Exception{
		NetworkBandwidthMinimizingPlanner problem = new NetworkBandwidthMinimizingPlanner();
		
		Workbook workbook = Workbook.getWorkbook(f);
		
		Sheet nodes = workbook.getSheet(NODES_SHEET);
		loadNodes(problem, nodes);
		
		Sheet compsres = workbook.getSheet(COMPONENTS_RESOURCES_SHEET);
		Component[] comps = loadComponentResources(problem, compsres);
		HashMap<String, Component> complookup = new HashMap<String, Component>();
		for(Component c : comps)
			complookup.put(c.getLabel(), c);
		
		Sheet schedule = workbook.getSheet(COMPONENTS_SCHEDULING_SHEET);
		loadComponentScheduling(complookup, schedule);
		
		Sheet coloc = workbook.getSheet(COMPONENTS_COLOCATION_SHEET);
		loadComponentColocationRules(complookup, coloc, problem);
	}
	
	public void loadComponentColocationRules(Map<String,Component> comps, Sheet colocation, DeploymentConfig problem){
		String[] headers = getHeaders(colocation);
		int rows = getRowCount(colocation);
		for(int i = 1; i < rows; i++){
			String pk = getPrimaryKey(colocation, i);
			Component comp = comps.get(pk);
			for(int j = 1; j < headers.length; j++){
				String val = colocation.getCell(j,i).getContents().trim();
				if(val.equalsIgnoreCase("r")){
					Component other = comps.get(headers[j-1]);
					problem.requireColocated(comp, other);
				}
				else if(val.equalsIgnoreCase("x")){
					Component other = comps.get(headers[j-1]);
					problem.requireNotColocated(comp, other);
				}
			}
		}
	}
	
	public void loadComponentScheduling(Map<String,Component> comps, Sheet schedule){
		String[] headers = getHeaders(schedule);
		int rows = getRowCount(schedule)-1;
		
		double[] rates = new double[headers.length-1];
		for(int j = 0; j < headers.length-1; j++){
			rates[j] = Double.parseDouble(headers[j]);
		}
		
		for(int i = 1; i < rows; i++){
			String pk = getPrimaryKey(schedule, i);
			Component comp = comps.get(pk);
			for(int j = 1; j < headers.length; j++){
				String val = schedule.getCell(j, i).getContents().trim();
				if(val.length() > 0){
					if(val.endsWith("%")){val = val.substring(0,val.length()-1);}
					if(val.startsWith("%")){val = val.substring(1);}
					double period = rates[j-1];
					double util = Double.parseDouble(val);
					comp.addTask(period, util);
				}
			}
		}
	}
	
	public Component[] loadComponentResources(DeploymentConfig problem, Sheet comps){
		//Find the names of the resource types
		String[] res = getHeaders(comps);
		
		int rows = getRowCount(comps);
		int totalcomps = rows-1;
		Component[] components = new Component[totalcomps];
		for(int i = 1; i < rows; i++){
			int[] cres = getIntResources(comps, i);
			components[i-1] = problem.addComponent(getPrimaryKey(comps, i), cres);
		}
		
		return components;
	}
	
	public void loadNodes(DeploymentConfig problem, Sheet nodes){
		
		//Find the names of the resource types
		String[] res = getHeaders(nodes);
		
		int rows = getRowCount(nodes);
		
		//Load the resources available on each node
		for(int i = 1; i < rows; i++){
			int[] nres = getIntResources(nodes, i);
			//Create the node
			problem.addNode(getPrimaryKey(nodes, i), nres);
		}
	}
	
	public String getPrimaryKey(Sheet sheet, int row){
		return sheet.getCell(0,row).getContents();
	}
	
	public int[] getIntResources(Sheet sheet, int row){
		int tc = getColumnCount(sheet);
		int[] nres = new int[tc-1];
		for(int j = 1; j < tc; j++){
			nres[j-1] = Integer.parseInt(sheet.getCell(j,row).getContents());
		}
		return nres;
	}
	
	public float[] getFloatResources(Sheet sheet, int row){
		int tc = getColumnCount(sheet);
		float[] nres = new float[tc-1];
		for(int j = 1; j < tc; j++){
			nres[j-1] = Float.parseFloat(sheet.getCell(j,row).getContents());
		}
		return nres;
	}
	
	public String[] getHeaders(Sheet sheet){
		//Find the names of the headers
		int tc = getColumnCount(sheet);
		int totalres = tc-1;
		String[] head = new String[totalres];
		for(int i = 1; i < tc; i++){
			head[i-1] = sheet.getCell(i,0).getContents().trim();
		}
		return head;
	}
	
	public int getColumnCount(Sheet sheet){
		int max = sheet.getColumns();
		int real = 1;
		for(int i = 1; i < max; i++){
			if(sheet.getCell(i,0).getContents().trim().length() > 0){
				real++;
			}
			else {
				break;
			}
		}
		return real;
	}
	
	public int getRowCount(Sheet sheet){
		int max = sheet.getRows();
		int real = 1;
		for(int i = 1; i < max; i++){
			if(sheet.getCell(0,i).getContents().trim().length() > 0){
				real++;
			}
			else {
				break;
			}
		}
		return real;
	}
}
