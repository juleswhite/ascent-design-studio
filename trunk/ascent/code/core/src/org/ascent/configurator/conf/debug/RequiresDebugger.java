package org.ascent.configurator.conf.debug;

import java.util.ArrayList;
import java.util.List;

import org.ascent.configurator.RefreshCore;
import org.ascent.configurator.conf.RefreshProblem;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class RequiresDebugger extends SourceConstraintDebugger{

	private List selectSet_;
	
	public RequiresDebugger(RefreshCore core, RefreshProblem problem, Object src, List rset) {
		super(core, problem, src);
		selectSet_ = rset;
	}

	
	public RequiresDebugger(RefreshCore core, RefreshProblem problem, Object src, Object rset) {
		super(core, problem, src);
		selectSet_ = new ArrayList(1);
		selectSet_.add(rset);
	}
	
	public String getError() {
		List targets = getSourceTargets(getSource());

		String msg = "";
		for (Object trg : targets) {
			msg += "\n"+getSource()+" could not be mapped to "+trg+" b/c the following required items could not be mapped: [";
			for (Object o : selectSet_) {
				List otrgs = getSourceTargets(o);
				msg += o;
				if(!otrgs.contains(trg)){
					msg += "(not mapped";
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
