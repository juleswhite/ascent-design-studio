package org.ascent.configurator.conf;

import java.util.HashMap;
import java.util.Map;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class DefineHandler implements ConfigDirectiveHandler {

	public class InvalidVariableDeclarationException extends RuntimeException {
		public InvalidVariableDeclarationException(String msg) {
			super(msg);
		}
	}

	public void handle(RefreshProblem problem, String context,
			String directive, String argstr) {
		String[] parts = argstr.split("=");
		if (parts.length != 2)
			throw new InvalidVariableDeclarationException(
					"A variable declaration must use the form \"VAR = VALUE\" and the declaration \""
							+ argstr
							+ "\" in context \""
							+ context
							+ "\" does not.");
		String var = parts[0];
		String val = parts[1];

		///int ival = ParsingUtil.toInt(val);

		Map<Object, Object> defines = problem.getSourceVariableValuesTable()
				.get(context);
		if (defines == null) {
			defines = new HashMap<Object, Object>();
			problem.getSourceVariableValuesTable().put(context, defines);
		}
		defines.put(var.trim(), val);
	}

}
