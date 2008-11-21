package org.ascent.configurator.conf;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class ExcludesHandler implements ConfigDirectiveHandler {

	public void handle(RefreshProblem problem, String context, String directive, String argstr) {
		String[] rfeatures = argstr.split(",");
		if(rfeatures != null && rfeatures.length > 0){
			List req = new ArrayList();
			for(String rfeat : rfeatures){
				req.add(rfeat.trim());
			}
			problem.getExcludes(context.trim()).addAll(req);
		}
	}

}
