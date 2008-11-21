package org.ascent.configurator.conf.debug;

import java.util.List;

import org.ascent.configurator.RefreshCore;
import org.ascent.configurator.conf.RefreshProblem;
import org.ascent.expr.Cardinality;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class SelectDebugger extends SourceConstraintDebugger{

	private List selectSet_;
	
	private Cardinality cardinality_;

	public SelectDebugger(RefreshCore core, RefreshProblem problem, Object src, List sset, Cardinality card) {
		super(core, problem, src);
		selectSet_ = sset;
		cardinality_ = card;
	}

	
	public String getError() {
		List targets = getSourceTargets(getSource());

		String msg = "";
		for (Object trg : targets) {
			msg += "\n"+getSource()+" could not be mapped to "+trg+" b/c the cardinality constraint "+cardinality_+" does not hold for the set: [";
			for (Object o : selectSet_) {
				List otrgs = getSourceTargets(o);
				msg += o;
				if(otrgs.contains(trg)){
					msg += "(enabled";
					if(getProblem().getSelectedItems().contains(o)){
						msg += ",required by user";
					}
					if(getProblem().getSourceMappedInstancesCountMap().get(o) != null && getProblem().getSourceMappedInstancesCountMap().get(o).getMin() > 0){
						msg += ",required by problem definition";
					}
				}
				else{
					msg += "(disabled";
					if(getProblem().getDisabledItems().contains(o)){
						msg += ",disabled by user";
					}
					if(getProblem().getSourceMappedInstancesCountMap().get(o) != null && getProblem().getSourceMappedInstancesCountMap().get(o).getMax() == 0){
						msg += ",disabled by problem definition";
					}
				}
				msg += "),";
			}
			msg += "]";
		}
		return msg;
	}

}
