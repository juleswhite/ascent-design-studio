package org.ascent.deployment.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

import org.ascent.deployment.Component;
import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.NetworkBandwidthMinimizingPlanner;
import org.ascent.deployment.NetworkLink;
import org.ascent.deployment.Node;

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
	
	public class Row {
		private String key_;
		private Map<String,String> data_ = new HashMap<String, String>();
		
		public String getKey() {
			return key_;
		}
		public void setKey(String key) {
			key_ = key;
		}
		public Map<String, String> getData() {
			return data_;
		}
		public void setData(Map<String, String> data) {
			data_ = data;
		}
	}

	public static final String NODES_SHEET = "Nodes";
	public static final String COMPONENTS_RESOURCES_SHEET = "Component Resources";
	public static final String COMPONENTS_SCHEDULING_SHEET = "Component Scheduling";
	public static final String COMPONENTS_COLOCATION_SHEET = "Component Co-location";
	public static final String COMPONENTS_INTERACTIONS_SHEET = "Component Interactions";
	public static final String NETWORK_RESOURCES_SHEET = "Networks";
	public static final String INTERACTIONS_SHEET = "Component Interactions";
	
	public static final String ISOURCE = "Sender";
	public static final String ITARGET = "Receiver";
	public static final String IRATE = "Transmit Rate";
	public static final String ISIZE = "Length";
	
	public void load(File f) throws Exception{
		NetworkBandwidthMinimizingPlanner problem = new NetworkBandwidthMinimizingPlanner();
		
		Workbook workbook = Workbook.getWorkbook(f);
		
		Sheet nodes = workbook.getSheet(NODES_SHEET);
		Node[] nodelist = loadNodes(problem, nodes);
		
		Sheet compsres = workbook.getSheet(COMPONENTS_RESOURCES_SHEET);
		Component[] comps = loadComponentResources(problem, compsres);
		HashMap<String, Component> complookup = new HashMap<String, Component>();
		for(Component c : comps)
			complookup.put(c.getLabel(), c);
		
		Sheet schedule = workbook.getSheet(COMPONENTS_SCHEDULING_SHEET);
		loadComponentScheduling(complookup, schedule);
		
		Sheet coloc = workbook.getSheet(COMPONENTS_COLOCATION_SHEET);
		loadComponentColocationRules(complookup, coloc, problem);
		
		HashMap<String, Node> nodelookup = new HashMap<String, Node>();
		for(Node n : nodelist)
			nodelookup.put(n.getLabel(), n);
		Sheet networks = workbook.getSheet(NETWORK_RESOURCES_SHEET);
		loadNetworks(problem,networks,nodelookup);
		
		Sheet ints = workbook.getSheet(COMPONENTS_INTERACTIONS_SHEET);
		loadInteractions(problem, ints, complookup);
		
		workbook.close();
	}
	
	public void loadInteractions(DeploymentConfig problem, Sheet interacts, Map<String,Component> comps){
		int rows = getRowCount(interacts);
		String[] headers = getHeaders(interacts);
		for(int i = 1; i < rows; i++){
			Row row = getRow(interacts, headers, i);
			String src = row.getData().get(ISOURCE);
			Component sender = comps.get(src);
			String trg = row.getData().get(ITARGET);
			Component receiver = comps.get(trg);
			double rate = Double.parseDouble(row.getData().get(IRATE));
			int size = Integer.parseInt(row.getData().get(ISIZE));
			problem.addInteraction(row.getKey(), new int[]{size}, rate, new Component[]{sender,receiver});
		}
	}
	
	public NetworkLink[] loadNetworks(DeploymentConfig problem, Sheet networks, Map<String,Node> nodelookup){
		String[] headers = getHeaders(networks);
		int rows = getRowCount(networks);
		NetworkLink[] links = new NetworkLink[rows-1];
		
		for(int i = 0; i < links.length; i++){
			List<Node> nodes = new ArrayList<Node>();
			int[] bandwidth = new int[]{getInt(networks,1,i+1)};
			for(int j = 2; j < headers.length; j++){
				String val = networks.getCell(j,i+1).getContents().trim();
				if(val.length() > 0){
					nodes.add(nodelookup.get(headers[j]));
				}
			}		
			links[i] = problem.addNetwork(getPrimaryKey(networks, i+1), nodes.toArray(new Node[0]), bandwidth);
		}
		return links;
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
	
	public Node[] loadNodes(DeploymentConfig problem, Sheet nodes){
		
		//Find the names of the resource types
		String[] res = getHeaders(nodes);
		
		int rows = getRowCount(nodes);
		
		//Load the resources available on each node
		Node[] ns = new Node[rows-1];
		for(int i = 1; i < rows; i++){
			int[] nres = getIntResources(nodes, i);
			//Create the node
			ns[i-1] = problem.addNode(getPrimaryKey(nodes, i), nres);
		}
		return ns;
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
	
	public Row getRow(Sheet sheet, String[] cols, int row){
		String pk = getPrimaryKey(sheet, row);
		Row robj = new Row();
		robj.setKey(pk);
		for(int i = 1; i < cols.length; i++){
			String val = sheet.getCell(i, row).getContents().trim();
			if(val.length()>0)
				robj.getData().put(cols[i-1],val);
		}
		return robj;
	}
	
	public String[] getHeaders(Sheet sheet){
		return getHeaders(sheet,1);
	}
	
	public String[] getHeaders(Sheet sheet, int off){
		//Find the names of the headers
		int tc = getColumnCount(sheet);
		int totalres = tc-1;
		String[] head = new String[totalres];
		for(int i = off; i < tc; i++){
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
	
	public int getInt(Sheet sheet, int col, int row){
		String val = sheet.getCell(col,row).getContents().trim();
		if(val.length() < 1)
			return 0;
		return Integer.parseInt(val);
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
