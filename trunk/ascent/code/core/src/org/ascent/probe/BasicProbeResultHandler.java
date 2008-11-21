package org.ascent.probe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ascent.configurator.conf.RefreshProblem;
import org.ascent.util.ParsingUtil;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class BasicProbeResultHandler implements ProbeResultHandler {

	public void applyResults(RefreshProblem problem, Probe probe) {
		if (probe instanceof TargetFinder) {
			TargetFinder targetinfo = (TargetFinder) probe;
			List targets = targetinfo.getTargets();
			problem.setTargetItems(targets);

			for (Object o : targets) {
				Map props = targetinfo.getTargetProperties(o);
				Map<Object, Object> vals = new HashMap<Object, Object>();
				for (Object key : props.keySet()) {
					vals.put(key, props.get(key));
				}
				problem.getTargetVariableValuesTable().put(o, vals);
			}
		} else if (probe instanceof PropertyProbe) {
			PropertyProbe pp = (PropertyProbe) probe;
			Map props = pp.getProperties();
			for (Object key : props.keySet()) {
				problem.getVariablesTable().put(key,
						ParsingUtil.toInt(props.get(key)));
			}
		} else if (probe instanceof FeatureProbe) {
			FeatureProbe fp = (FeatureProbe) probe;
			Object feature = fp.getFeature();
			int val = fp.getEnabled();

			if (val == 0)
				problem.getDisabledItems().add(feature);
		}
	}
}
