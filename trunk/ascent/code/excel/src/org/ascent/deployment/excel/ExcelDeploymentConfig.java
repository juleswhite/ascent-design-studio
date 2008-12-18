package org.ascent.deployment.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import org.ascent.deployment.Component;
import org.ascent.deployment.DeploymentConfig;
import org.ascent.deployment.NetworkLink;
import org.ascent.deployment.Node;
import org.ascent.deployment.RateMonotonicResource;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class ExcelDeploymentConfig {

	public class Row {
		private String key_;
		private Map<String, Object> data_ = new HashMap<String, Object>();

		public String getKey() {
			return key_;
		}

		public void setKey(String key) {
			key_ = key;
		}

		public Map<String, Object> getData() {
			return data_;
		}

		public void setData(Map<String, Object> data) {
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

	public void load(File f, DeploymentConfig problem) throws Exception {
		Workbook workbook = Workbook.getWorkbook(f);

		Sheet nodes = getSheet(workbook, NODES_SHEET);
		Node[] nodelist = loadNodes(problem, nodes);

		Sheet compsres = getSheet(workbook, COMPONENTS_RESOURCES_SHEET);
		Component[] comps = loadComponentResources(problem, compsres);
		HashMap<String, Component> complookup = new HashMap<String, Component>();
		for (Component c : comps)
			complookup.put(c.getLabel(), c);

		Sheet schedule = getSheet(workbook, COMPONENTS_SCHEDULING_SHEET);
		loadComponentScheduling(complookup, schedule, problem);

		Sheet coloc = getSheet(workbook, COMPONENTS_COLOCATION_SHEET);
		loadComponentColocationRules(complookup, coloc, problem);

		HashMap<String, Node> nodelookup = new HashMap<String, Node>();
		for (Node n : nodelist)
			nodelookup.put(n.getLabel(), n);
		Sheet networks = getSheet(workbook, NETWORK_RESOURCES_SHEET);
		loadNetworks(problem, networks, nodelookup);

		Sheet ints = getSheet(workbook, COMPONENTS_INTERACTIONS_SHEET);
		loadInteractions(problem, ints, complookup);

		workbook.close();
	}

	protected Sheet getSheet(Workbook workbook, String sheetname) {
		Sheet sheet = workbook.getSheet(sheetname);
		if (sheet == null) {
			throw new ExcelDeploymentConfigException(
					"The workbook is missing the required worksheet: \""
							+ sheetname + "\".");
		}
		return sheet;
	}

	public void loadInteractions(DeploymentConfig problem, Sheet interacts,
			Map<String, Component> comps) {
		int rows = getRowCount(interacts);
		String[] headers = getHeaders(interacts);
		for (int i = 1; i < rows; i++) {
			Row row = getRow(interacts, headers, i);
			String src = ""+row.getData().get(ISOURCE);
			if (src == null) {
				throw new ExcelDeploymentConfigException(
						"Missing required column:" + ISOURCE
								+ " from worksheet:" + INTERACTIONS_SHEET,
						INTERACTIONS_SHEET, i + 1, -1);
			}
			Component sender = comps.get(src);
			if (sender == null) {
				throw new ExcelDeploymentConfigException(
						"Undeclared component ID:" + src + " from worksheet:"
								+ INTERACTIONS_SHEET + " in " + ISOURCE
								+ " column", INTERACTIONS_SHEET, i + 1, -1);
			}

			String trg = ""+row.getData().get(ITARGET);
			if (trg == null) {
				throw new ExcelDeploymentConfigException(
						"Missing required column:" + ITARGET
								+ " from worksheet:" + INTERACTIONS_SHEET,
						INTERACTIONS_SHEET, i + 1, -1);
			}
			Component receiver = comps.get(trg);
			if (receiver == null) {
				throw new ExcelDeploymentConfigException(
						"Undeclared component ID:" + trg + " from worksheet:"
								+ INTERACTIONS_SHEET + " in " + ITARGET
								+ " column", INTERACTIONS_SHEET, i + 1, -1);
			}

			double rate = 0;
			try {
				rate = (Double)row.getData().get(IRATE);
			} catch (Exception e) {
				throw new ExcelDeploymentConfigException(
						"Invalid rate specification (not a number):"
								+ row.getData().get(IRATE) + " from worksheet:"
								+ INTERACTIONS_SHEET + " in " + IRATE
								+ " column", INTERACTIONS_SHEET, i + 1, -1);
			}

			int size = 0;
			try {
				size = (int)Math.rint((Double)(row.getData().get(ISIZE)));
			} catch (Exception e) {
				throw new ExcelDeploymentConfigException(
						"Invalid size specification (not a number):"
								+ row.getData().get(ISIZE) + " from worksheet:"
								+ INTERACTIONS_SHEET + " in " + ISIZE
								+ " column", INTERACTIONS_SHEET, i + 1, -1);
			}

			problem.addInteraction(row.getKey(), new int[] { size }, rate,
					new Component[] { sender, receiver });
		}
	}

	public NetworkLink[] loadNetworks(DeploymentConfig problem, Sheet networks,
			Map<String, Node> nodelookup) {
		String[] headers = getHeaders(networks);
		int rows = getRowCount(networks);
		NetworkLink[] links = new NetworkLink[rows - 1];

		for (int i = 0; i < links.length; i++) {
			List<Node> nodes = new ArrayList<Node>();

			int[] bandwidth = null;
			try {
				bandwidth = new int[] { getInt(networks, 1, i + 1) };
			} catch (Exception e) {
				throw new ExcelDeploymentConfigException(
						"Invalid network resource specification", e,
						NETWORK_RESOURCES_SHEET, i + 2, 2);
			}
			for (int j = 2; j < headers.length; j++) {
				String val = networks.getCell(j, i + 1).getContents().trim();
				if (val.length() > 0) {
					Node node = nodelookup.get(headers[j]);
					if (node == null) {
						throw new ExcelDeploymentConfigException(
								"Undeclared node ID:" + headers[j],
								NETWORK_RESOURCES_SHEET, i + 1, j + 2);
					}
					nodes.add(node);
				}
			}

			String key = getPrimaryKey(networks, i + 1);
			if (key == null || key.length() < 1) {
				throw new ExcelDeploymentConfigException(
						"Invalid network identifier (null or missing):" + key,
						NETWORK_RESOURCES_SHEET, i + 2, 1);
			}
			links[i] = problem.addNetwork(key, nodes.toArray(new Node[0]),
					bandwidth);
		}
		return links;
	}

	public void loadComponentColocationRules(Map<String, Component> comps,
			Sheet colocation, DeploymentConfig problem) {
		String[] headers = getHeaders(colocation);
		int rows = getRowCount(colocation);
		for (int i = 1; i < rows; i++) {
			String pk = getPrimaryKey(colocation, i);
			Component comp = comps.get(pk);
			if (comp == null) {
				throw new ExcelDeploymentConfigException(
						"Undeclared component ID:" + pk,
						COMPONENTS_COLOCATION_SHEET, i + 1, 1);

			}
			for (int j = 1; j < headers.length; j++) {
				String val = colocation.getCell(j, i).getContents().trim();
				if (val.equalsIgnoreCase("r")) {
					Component other = comps.get(headers[j - 1]);
					if (other == null) {
						throw new ExcelDeploymentConfigException(
								"Undeclared component ID:" + headers[j - 1],
								COMPONENTS_COLOCATION_SHEET, i + 1, j + 1);

					}
					problem.requireColocated(comp, other);
				} else if (val.equalsIgnoreCase("x")) {
					Component other = comps.get(headers[j - 1]);
					if (other == null) {
						throw new ExcelDeploymentConfigException(
								"Undeclared component ID:" + headers[j - 1],
								COMPONENTS_COLOCATION_SHEET, i + 1, j + 1);

					}
					problem.requireNotColocated(comp, other);
				}
			}
		}
	}

	public void loadComponentScheduling(Map<String, Component> comps,
			Sheet schedule, DeploymentConfig problem) {
		String[] headers = getHeaders(schedule);
		int rows = getRowCount(schedule) - 1;
		boolean isRT = false;
		double[] rates = new double[headers.length - 1];
		for (int j = 0; j < headers.length - 1; j++) {
			try {
				rates[j] = Double.parseDouble(headers[j]);
			} catch (Exception e) {
				throw new ExcelDeploymentConfigException(
						"Invalid task periodic rate specification:"
								+ headers[j], COMPONENTS_SCHEDULING_SHEET, 1,
						j + 1);
			}
		}

		for (int i = 1; i <= rows; i++) {
			
			double tutil = 0;
			String pk = getPrimaryKey(schedule, i);
			Component comp = comps.get(pk);
			for (int j = 1; j < headers.length; j++) {
				String val = schedule.getCell(j, i).getContents().trim();
				if (val.length() > 0) {
					if (val.endsWith("%")) {
						val = val.substring(0, val.length() - 1);
					}
					if (val.startsWith("%")) {
						val = val.substring(1);
					}
					double period = rates[j - 1];
					try {
						double util = Double.parseDouble(val);
						tutil += util;
						comp.addTask(period, util);
						isRT = true;
					} catch (Exception e) {
						throw new ExcelDeploymentConfigException(
								"Invalid task periodic utilization specification:"
										+ val, COMPONENTS_SCHEDULING_SHEET,
								i + 1, j + 1);
					}
				}
			}
			comp.prependResource((int) Math.rint(tutil));
		}
		if(isRT){
			problem.getResourceConsumptionPolicies().put(0, new RateMonotonicResource());
		}
	}

	public Component[] loadComponentResources(DeploymentConfig problem,
			Sheet comps) {
		// Find the names of the resource types
		String[] res = getHeaders(comps);

		int rows = getRowCount(comps);
		int totalcomps = rows - 1;
		Component[] components = new Component[totalcomps];
		for (int i = 1; i < rows; i++) {
			try {
				int[] cres = getIntResources(comps, i);
				components[i - 1] = problem.addComponent(
						getPrimaryKey(comps, i), cres);
			} catch (Exception e) {
				throw new ExcelDeploymentConfigException(
						"Invalid resource specification (a non-number is in the row)",
						COMPONENTS_RESOURCES_SHEET, i + 1, -1);
			}
		}

		return components;
	}

	public Node[] loadNodes(DeploymentConfig problem, Sheet nodes) {

		// Find the names of the resource types
		String[] res = getHeaders(nodes);

		int rows = getRowCount(nodes);

		// Load the resources available on each node
		Node[] ns = new Node[rows - 1];
		for (int i = 1; i < rows; i++) {
			try {
				int[] nres = getIntResources(nodes, i);
				// Create the node
				ns[i - 1] = problem.addNode(getPrimaryKey(nodes, i), nres);
			} catch (Exception e) {
				throw new ExcelDeploymentConfigException(
						"Invalid resource specification (a non-number is in the row)",
						NODES_SHEET, i + 1, -1);
			}
		}
		return ns;
	}

	public String getPrimaryKey(Sheet sheet, int row) {
		return sheet.getCell(0, row).getContents();
	}

	public int[] getIntResources(Sheet sheet, int row) {
		int tc = getColumnCount(sheet);
		int[] nres = new int[tc - 1];
		for (int j = 1; j < tc; j++) {
			nres[j - 1] = Integer.parseInt(sheet.getCell(j, row).getContents());
		}
		return nres;
	}

	public float[] getFloatResources(Sheet sheet, int row) {
		int tc = getColumnCount(sheet);
		float[] nres = new float[tc - 1];
		for (int j = 1; j < tc; j++) {
			nres[j - 1] = Float.parseFloat(sheet.getCell(j, row).getContents());
		}
		return nres;
	}

	public Row getRow(Sheet sheet, String[] cols, int row) {
		String pk = getPrimaryKey(sheet, row);
		Row robj = new Row();
		robj.setKey(pk);
		for (int i = 1; i < cols.length; i++) {
			Cell cell = sheet.getCell(i, row);
			if (cell.getType() == CellType.NUMBER)
				robj.getData().put(cols[i - 1], ((NumberCell) cell).getValue());
			else {
				String val = cell.getContents().trim();

				if (val.length() > 0)
					robj.getData().put(cols[i - 1], val);
			}
		}
		return robj;
	}

	public String[] getHeaders(Sheet sheet) {
		return getHeaders(sheet, 1);
	}

	public String[] getHeaders(Sheet sheet, int off) {
		// Find the names of the headers
		int tc = getColumnCount(sheet);
		int totalres = tc - 1;
		String[] head = new String[totalres];
		for (int i = off; i < tc; i++) {
			head[i - 1] = sheet.getCell(i, 0).getContents().trim();
		}
		return head;
	}

	public int getColumnCount(Sheet sheet) {
		int max = sheet.getColumns();
		int real = 1;
		for (int i = 1; i < max; i++) {
			if (sheet.getCell(i, 0).getContents().trim().length() > 0) {
				real++;
			} else {
				break;
			}
		}
		return real;
	}

	public int getInt(Sheet sheet, int col, int row) {
		String val = sheet.getCell(col, row).getContents().trim();
		if (val.length() < 1)
			return 0;
		return Integer.parseInt(val);
	}

	public int getRowCount(Sheet sheet) {
		int max = sheet.getRows();
		int real = 1;
		for (int i = 1; i < max; i++) {
			if (sheet.getCell(0, i).getContents().trim().length() > 0) {
				real++;
			} else {
				break;
			}
		}
		return real;
	}
}
