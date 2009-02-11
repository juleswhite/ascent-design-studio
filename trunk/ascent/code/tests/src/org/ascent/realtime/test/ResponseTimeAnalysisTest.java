package org.ascent.realtime.test;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
		RealTimeTask t1 = new RealTimeTask(10,50);
		RealTimeTask t2 = new RealTimeTask(5,50);
		
		List<RealTimeTask> tasks = new ArrayList<RealTimeTask>();
		tasks.add(t2);
		tasks.add(t1);
		
		
		ResponseTimeAnalysis rta = new ResponseTimeAnalysis();
		
		assertTrue(rta.schedulable(tasks));
	}
	
	public void testSimpleInvalidReponseTimeAnalysis(){
		RealTimeTask t1 = new RealTimeTask(10,76);
		RealTimeTask t2 = new RealTimeTask(5,50);
		
		List<RealTimeTask> tasks = new ArrayList<RealTimeTask>();
		tasks.add(t2);
		tasks.add(t1);
		
		
		ResponseTimeAnalysis rta = new ResponseTimeAnalysis();
		
		assertTrue(!rta.schedulable(tasks));
	}
	
	public void testComplexReponseTimeAnalysis(){
		RealTimeTask t1 = new RealTimeTask(10,10);
		RealTimeTask t2 = new RealTimeTask(5,50);
		
		List<RealTimeTask> tasks = new ArrayList<RealTimeTask>();
		tasks.add(t2);
		tasks.add(t1);
		
		
		ResponseTimeAnalysis rta = new ResponseTimeAnalysis();
		
		assertTrue(rta.schedulable(tasks));
	}
	
	public void testHarmonicResponseTimeAnalysis(){
		//0.46@1/0.391, 0.41@1/0.781, 60.5@1/1.562, 2.08@1/6.25, 3.22@1/12.5, 5.39@1/25.0
		RealTimeTask t1 = new RealTimeTask(0.391,.46);
		RealTimeTask t2 = new RealTimeTask(0.782,.41);
		RealTimeTask t3 = new RealTimeTask(1.564,60.5);
		RealTimeTask t4 = new RealTimeTask(6.256,2.08);
		RealTimeTask t5 = new RealTimeTask(12.512,3.22);
		RealTimeTask t6 = new RealTimeTask(25.024,5.39);
		
		List<RealTimeTask> tasks = new ArrayList<RealTimeTask>();
		tasks.add(t2);
		tasks.add(t1);
		tasks.add(t3);
		tasks.add(t4);
		tasks.add(t5);
		tasks.add(t6);
		
		ResponseTimeAnalysis rta = new ResponseTimeAnalysis();
		
		assertTrue(rta.schedulable(tasks));
	}
	
	public void testNearlyHarmonicResponseTimeAnalysis(){
		//0.46@1/0.391, 0.41@1/0.781, 60.5@1/1.562, 2.08@1/6.25, 3.22@1/12.5, 5.39@1/25.0
		RealTimeTask t1 = new RealTimeTask(0.391,0.46);
		RealTimeTask t2 = new RealTimeTask(0.781,0.41);
		RealTimeTask t3 = new RealTimeTask(1.562,60.5);
		RealTimeTask t4 = new RealTimeTask(6.25,2.08);
		RealTimeTask t5 = new RealTimeTask(12.5,3.22);
		RealTimeTask t6 = new RealTimeTask(25.0,5.39);
		
		List<RealTimeTask> tasks = new ArrayList<RealTimeTask>();
		tasks.add(t2);
		tasks.add(t1);
		tasks.add(t3);
		tasks.add(t4);
		tasks.add(t5);
		tasks.add(t6);
		
		ResponseTimeAnalysis rta = new ResponseTimeAnalysis();
		
		assertTrue(rta.schedulable(tasks));
	}
	
	public void testSamePeriodResponseTimeAnalysis(){
		//0.46@1/0.391, 0.41@1/0.781, 60.5@1/1.562, 2.08@1/6.25, 3.22@1/12.5, 5.39@1/25.0
		RealTimeTask t1 = new RealTimeTask(0.098,11);
		RealTimeTask t2 = new RealTimeTask(0.098,11);
		RealTimeTask t3 = new RealTimeTask(0.098,78);
		
		List<RealTimeTask> tasks = new ArrayList<RealTimeTask>();
		tasks.add(t2);
		tasks.add(t1);
		tasks.add(t3);
		
		
		ResponseTimeAnalysis rta = new ResponseTimeAnalysis();
		
		assertTrue(rta.schedulable(tasks));
	}
	
	//[11.0@1/0.098, 19.0@1/0.098, 31.0@1/0.098, 0.07021507013263158@1/0.391, 
	//0.0689534528293634@1/0.391, 0.16855514026526314@1/0.781, 0.13279734315872685@1/0.781, 
	//0.3281102805305263@1/1.562, 0.1425351630583705@1/1.562, 9.1655@1/3.125, 
	//0.2947205610610526@1/6.25, 3.8226354252664865@1/6.25, 0.8234411221221052@1/12.5, 
	//1.099957692638236@1/12.5, 2.3728822442442103@1/25.0, 2.181125911592261@1/25.0, 
	//21.66589488848842@1/50.0]
	public void testLotsofTasksInvalidAnalysis(){
		String tstr = "[11.0@1/0.098, 19.0@1/0.098, 31.0@1/0.098, 0.07021507013263158@1/0.391," 
		                +"0.0689534528293634@1/0.391, 0.16855514026526314@1/0.781, 0.13279734315872685@1/0.781," 
		                +"0.3281102805305263@1/1.562, 0.1425351630583705@1/1.562, 9.1655@1/3.125," 
		                +"0.2947205610610526@1/6.25, 3.8226354252664865@1/6.25, 0.8234411221221052@1/12.5," 
		                +"1.099957692638236@1/12.5, 2.3728822442442103@1/25.0, 2.181125911592261@1/25.0, "
		            	+"21.66589488848842@1/50.0]";
		List<RealTimeTask> tasks = createTasks(tstr);
		
		assertTrue(!ResponseTimeAnalysis.schedulable(tasks));
	}
	
	public void testInvalidAnalysis(){
		String tstr = "[60.0@1/0.098, 43.0@1/0.098, 27.0@1/0.098, 19.0@1/0.098, 11.0@1/0.098]";
		List<RealTimeTask> tasks = createTasks(tstr);
		
		assertTrue(!ResponseTimeAnalysis.schedulable(tasks));
		assertTrue(!ResponseTimeAnalysis.schedulable(tasks));
		assertTrue(!ResponseTimeAnalysis.schedulable(tasks));
		assertTrue(!ResponseTimeAnalysis.schedulable(tasks));
	}
	
	
	
	public List<RealTimeTask> createTasks(String tasks){
		tasks = tasks.trim();
		if(tasks.startsWith("[")){
			tasks = tasks.substring(1,tasks.length()-1);
		}
		
		List<RealTimeTask> tobjs = new ArrayList<RealTimeTask>();
		
		StringTokenizer tk = new StringTokenizer(tasks,",",false);
		while(tk.hasMoreTokens()){
			String token = tk.nextToken().trim();
			String[] parts = token.split("\\@");
			Double util = Double.parseDouble(parts[0]);
			String rstr = parts[1].split("\\/")[1];
			Double rate = Double.parseDouble(rstr);
			tobjs.add(new RealTimeTask(rate,util));
		}
		
		return tobjs;
	}
}
