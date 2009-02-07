package org.ascent.realtime.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.ascent.deployment.RealTimeTask;
import org.ascent.realtime.ResponseTimeAnalysis;

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
public class ResponseTimeAnalysisTest extends TestCase{

	public void testSimpleReponseTimeAnalysis(){
		RealTimeTask t1 = new RealTimeTask(10,.5);
		RealTimeTask t2 = new RealTimeTask(5,.5);
		
		List<RealTimeTask> tasks = new ArrayList<RealTimeTask>();
		tasks.add(t2);
		tasks.add(t1);
		
		
		ResponseTimeAnalysis rta = new ResponseTimeAnalysis();
		
		assertTrue(rta.schedulable(tasks));
	}
	
	public void testSimpleInvalidReponseTimeAnalysis(){
		RealTimeTask t1 = new RealTimeTask(10,.76);
		RealTimeTask t2 = new RealTimeTask(5,.5);
		
		List<RealTimeTask> tasks = new ArrayList<RealTimeTask>();
		tasks.add(t2);
		tasks.add(t1);
		
		
		ResponseTimeAnalysis rta = new ResponseTimeAnalysis();
		
		assertTrue(!rta.schedulable(tasks));
	}
	
	public void testComplexReponseTimeAnalysis(){
		RealTimeTask t1 = new RealTimeTask(10,.1);
		RealTimeTask t2 = new RealTimeTask(5,.5);
		
		List<RealTimeTask> tasks = new ArrayList<RealTimeTask>();
		tasks.add(t2);
		tasks.add(t1);
		
		
		ResponseTimeAnalysis rta = new ResponseTimeAnalysis();
		
		assertTrue(rta.schedulable(tasks,t1));
	}
}
