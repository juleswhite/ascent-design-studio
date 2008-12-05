 /**************************************************************************
 * Copyright 2008 Jules White                                              *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/

package org.ascent.pso.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


public class ExcelDataAggregator {
	

	public static void main(String[] args){
		try{
			File f = new File(args[0]);
			
			FileReader in = new FileReader(f);
			BufferedReader bin = new BufferedReader(in);
			
			String line = null;
			Map<Integer,Map<Integer,Map<Double,Integer>>> agg = new HashMap<Integer, Map<Integer,Map<Double,Integer>>>();
			while((line = bin.readLine()) != null){
				String[] tokens = line.split(",");
				int src = Integer.parseInt(tokens[3]);
				int trg = Integer.parseInt(tokens[4]);
				double rate = Double.parseDouble(tokens[1]);
				int size = Integer.parseInt(tokens[2]);
				String type = tokens[8];
				
				if(!type.startsWith("Startup")){
				
				Map<Integer,Map<Double,Integer>> pi = agg.get(src);
				if(pi == null){
					pi = new HashMap<Integer,Map<Double,Integer>>();
					agg.put(src, pi);
				}
				Map<Double,Integer> msgs = pi.get(trg);
				if(msgs == null){
					msgs = new HashMap<Double, Integer>();
					pi.put(trg, msgs);
				}
				Integer val = msgs.get(rate);
				val = (val == null) ? size : size + val;
				msgs.put(rate, val);
				
				}
			}
			
			Map<Integer,Integer> apps = new HashMap<Integer, Integer>();
			apps.put(1,13);
			apps.put(2,14);
			apps.put(21,6);
			apps.put(22,3);
			apps.put(23,11);
			apps.put(24,12);
			apps.put(25,1);
			apps.put(26,4);
			apps.put(27,2);
			apps.put(28,7);
			apps.put(29,8);
			apps.put(30,10);
			apps.put(31,5);
			apps.put(32,9);
			
			int total = 0;
			for(Integer src : agg.keySet()){
				Map<Integer,Map<Double,Integer>> pi = agg.get(src);
				for(Integer trg : pi.keySet()){
					if((src < 3 || (src > 20 && src < 33))&&(trg < 3 || (trg > 20 && trg < 33))){
						Map<Double,Integer> msgs = pi.get(trg);
						for(Double rate : msgs.keySet()){
							total++;
							Integer size = msgs.get(rate);
							System.out.println("Interaction i"+total+" = " +
									"problem.addInteraction(\"p"+src+"-->p"+trg+" "+rate+"hz" +
									"\",new int[] { "+size+" }, "+rate+", new Component[] { a"+apps.get(src)+", a"+apps.get(trg)+" });");
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
