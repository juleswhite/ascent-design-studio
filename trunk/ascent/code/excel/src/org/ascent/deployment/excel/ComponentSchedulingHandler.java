package org.ascent.deployment.excel;

import java.util.Map;

import jxl.Sheet;

import org.ascent.deployment.Component;
import org.ascent.deployment.DeploymentConfig;
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
public class ComponentSchedulingHandler extends AbstractWorksheetHandler {

	public static final String COMPONENTS_SCHEDULING_SHEET = "Component Scheduling";

	public String getWorksheetName() {
		return COMPONENTS_SCHEDULING_SHEET;
	}

	@Override
	public void handleSheet(DeploymentConfig problem, Sheet schedule,
			Map<String, Component> comps, Map<String, Node> nodes) {
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
		if (isRT) {
			problem.getResourceConsumptionPolicies().put(0,
					new RateMonotonicResource());
		}
	}

}
