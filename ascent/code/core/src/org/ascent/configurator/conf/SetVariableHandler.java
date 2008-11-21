package org.ascent.configurator.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ascent.injectors.annotations.InvalidInjectionException;
import org.ascent.util.ParsingUtil;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class SetVariableHandler implements ConfigDirectiveHandler {

	public class InvalidVariableDeclarationException extends RuntimeException {
		public InvalidVariableDeclarationException(String msg){
			super(msg);
		}
	}
	
	public String[] resolve(String context, String var){
		int first = var.indexOf(".");
		String[] resolved = new String[2];
		if(first > 0){
			resolved[0] = var.substring(0,first);
			resolved[1] = var.substring(first+1);
		}
		else{
			resolved[0] = context;
			resolved[1] = var;
		}
		return resolved;
	}
	
	public void handle(RefreshProblem problem, String context, String directive, String argstr) {
		argstr = argstr.trim();
		if(argstr.startsWith("{") && argstr.endsWith("}"))
			argstr = argstr.substring(1,argstr.length()-1);
		
		int start = argstr.indexOf("=");
		if(start < 1)
			throw new InvalidVariableDeclarationException("A variable declaration must use the form \"VAR = VALUE\" and the declaration \""+argstr+"\" in context \""+context+"\" does not (it is missing '=').");
		
		String var = argstr.substring(0,start);
		String val = argstr.substring(start+1,argstr.length());
		
		Map<String,Object> ivals = problem.getInjectionValues(context);
		if(ivals == null){
			ivals = new HashMap<String, Object>();
			problem.setInjectionValues(context, ivals);
		}
		Object eval = ivals.get(var);
		if(eval != null && !(eval instanceof List)){
			throw new InvalidInjectionException("The variable \""+var+"\" in context \""+context+"\" is not a list and and already has a value set.");
		}
		
		Object dval = val;
		if(val.startsWith("[")){
			dval = ParsingUtil.parseList(val);
		}
		else if(val.startsWith("{")){
			dval = ParsingUtil.parseMap(val);
		}
		if(eval != null){
			List lval = (List)eval;
			lval.add(dval);
			dval = lval;
		}
		
		String[] resolved = resolve(context, var);
		ivals.put(resolved[0]+"."+resolved[1],dval);
	}

}
