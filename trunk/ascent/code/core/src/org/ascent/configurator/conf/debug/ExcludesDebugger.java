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
public class ExcludesDebugger extends SourceConstraintDebugger{

	private List selectSet_;
	
	public ExcludesDebugger(RefreshCore core, RefreshProblem problem, Object src, List rset) {
		super(core, problem, src);
		selectSet_ = rset;
	}

	public ExcludesDebugger(RefreshCore core, RefreshProblem problem, Object src, Object rset) {
		super(core, problem, src);
		selectSet_ = new ArrayList(1);
		selectSet_.add(rset);
	}
	
	public String getError() {
		List targets = getSourceTargets(getSource());

		String msg = "";
		for (Object trg : targets) {
			msg += "\n"+getSource()+" could not be mapped to "+trg+" b/c it excludes the following items which were mapped to the same target: [";
			for (Object o : selectSet_) {
				List otrgs = getSourceTargets(o);
				msg += o;
				if(otrgs.contains(trg)){
					msg += "(mapped";
					if(getProblem().getSelectedItems().contains(o)){
						msg += ",required by user";
					}
					if(getProblem().getSourceMappedInstancesCountMap().get(o) != null && getProblem().getSourceMappedInstancesCountMap().get(o).getMin() > 0){
						msg += ",required by problem definition";
					}
				}
				msg += "),";
			}
			msg += "]";
		}
		return msg;
	}

}
