package org.crush.deployment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.crush.deployment.SoloMonitor;
import org.crush.syrupy.SyrupyParser;

public class SoloMonitor implements Monitor{
	/*
	 * Each map is of the form {TaskName,{ResourceName, Value}}
	 * 
	 */
    private List processList_ = new ArrayList();
	private Map upperbound_ = new HashMap<String, Map<String,Double>>(); //If average utilization passes this bound, a new node is neccessary
	private Map lowerbound_ = new HashMap<String, Map<String,Double>>();//if average utilization is below this bound, this node could be removed
	private Map average_ =new HashMap< String, Map<String,Double>>(); //average utilization of each node
	private Map highest_ =new HashMap<String, Map<String,Double>>();
	private Map lowest_ =new HashMap<String, Map<String,Double>>();
	private Map allData_=new HashMap<String, Map<String, ArrayList<String>>>();
	private Map systemData_ = new HashMap<String, Map< String, ArrayList<String>>>();//Overall System data from top	

	private static final Logger logger_ = Logger
	.getLogger(SoloMonitor.class.getName());

	public SoloMonitor(){
		
	}
	
	public void addProcess(Map <String,String> processData){
		String pid = processData.get("PID");
		System.out.println(" PID = "+pid);
		HashMap <String,Double>blankMap = new HashMap<String,Double>();
		blankMap = (HashMap) setResources(blankMap);//might need to go through an initialize all the values for that pid
		
		upperbound_.put(pid, blankMap);
		blankMap = new HashMap<String,Double>();
		blankMap = (HashMap) setResources(blankMap);//might need to go through an initialize all the values for that pid
		lowerbound_.put(pid, blankMap);
		blankMap = new HashMap<String,Double>();
		blankMap = (HashMap) setResources(blankMap);//might need to go through an initialize all the values for that pid
		highest_.put(pid, blankMap);
		blankMap = new HashMap<String,Double>();
		blankMap = (HashMap) setResources(blankMap);//might need to go through an initialize all the values for that pid
		lowest_.put(pid, blankMap);
		
		blankMap = new HashMap<String,Double>();
		blankMap = (HashMap) setResources(blankMap);//might need to go through an initialize all the values for that pid
		blankMap.put("count", 0.0);
		average_.put(pid, blankMap);
		HashMap<String,ArrayList<String>>blankMap2 = new HashMap();//(HashMap) setResources(blankMap);//might need to go through an initialize all the values for that pid
		blankMap2.put("%CPU", new ArrayList());
		
		allData_.put(pid, blankMap2);
		blankMap2 = new HashMap();
		
		System.out.println("lowest = "+lowest_);
		System.out.println("average_ = " +average_);
		processList_.add(processData.get("PID"));
	}
	
	public Map setResources(Map bp){
		HashMap <String, Double> hm = new HashMap();
		hm.put("%CPU", 2.0);
		return hm;
	}
	
	public void updateProcess(Map <String,String> processData){
		System.out.println("Process list = " + processList_);
		if(!processList_.contains(processData.get("PID"))){
			addProcess(processData);
		}
		String pid = processData.get("PID");
		System.out.println("about to update upperbound");
		updateUpperbound(processData);
		System.out.println("about to update lowerbound");
		updateLowerbound(processData);
		System.out.println("about to update average");
		updateAverage(processData);
		//updateHighest(processData);
		System.out.println("about to update lowest");
		updateLowest(processData);
		System.out.println("About to update all Data");
		updateAllData(processData);
		
		System.out.println("allData = " +allData_);
		System.out.println("lowest = " +lowest_);
		System.out.println("average = " + average_);
		
	}
	
	private void addSystemKeys(Map<String,Map<String,String>> systemData){
		Collection keys = systemData.keySet();
		Iterator it = keys.iterator();
		System.out.println("addSystemKeys = " + keys);
		while(it.hasNext()){
			String key = (String) it.next();
			HashMap outter =  (HashMap)  systemData.get(key);
			Iterator innerIt = outter.keySet().iterator();
			HashMap<String, ArrayList> inner = new HashMap<String,ArrayList>();
			while( innerIt.hasNext()){;
				String innerKey = (String) innerIt.next();
				inner.put(innerKey,new ArrayList());
				
			}
			systemData_.put(key,inner);
		}
		System.out.println("systemData = " + systemData_);
		
	}
	
	public void updateAllSystemData(Map<String,Map<String,String>> systemData ){
		if( systemData_.keySet().size()==0){
			addSystemKeys(systemData);
		}
		System.out.println("systemData_ = " + systemData_);
		System.out.println("systemData_ keySet  = " + systemData_.keySet());
		System.out.println("System Data  keySet  = " + systemData.keySet());
		for(String resource : systemData.keySet()){
			Map <String,ArrayList<String>> resourceMap = (Map<String, ArrayList<String>>) systemData_.get(resource);
			if(systemData_.containsKey(resource) ){
				Map <String, String> newValues = systemData.get(resource);
				Iterator newValuesIt = newValues.keySet().iterator();
				Map<String,ArrayList> updateList = new HashMap<String,ArrayList>();
				while( newValuesIt.hasNext()){
					String key = (String) newValuesIt.next();
					String value = newValues.get(key);
					ArrayList<String> oldValues = resourceMap.get(key);
					oldValues.add(value);
					updateList.put(key,oldValues);
				}
				systemData_.put(resource, updateList);
			}
		}
	}
	
	private void updateAllData(Map <String,String> processData){
		String pid = processData.get("PID");
		Map <String,ArrayList<String>> resourceMap = (Map<String, ArrayList<String>>) allData_.get(pid);
		for(String resource : resourceMap.keySet()){
			System.out.println("Current resource is " + resource);
			if(processData.containsKey(resource) ){
				ArrayList<String> resourceHistory =  resourceMap.get(resource);
				System.out.println("ResourceHistory = "+ resourceHistory);
				resourceHistory.add(processData.get(resource));
				resourceMap.put(resource, resourceHistory);
				allData_.put(pid,resourceMap);
			}
		}
	}
	
	public void writeAllData(String filename){
		String output = "";
	    Set pids =  allData_.keySet();
	    Iterator iterator = pids.iterator();
	    while(iterator.hasNext()){
	    	String pid = (String) iterator.next();
	    	System.out.println("pid = " + pid);
	    	if(pid != "System"){
		    	Map <String,ArrayList<String>> resourceMap = (Map<String, ArrayList<String>>) allData_.get(pid);
				for(String resource : resourceMap.keySet()){
					output += resource ;
					ArrayList<String> resourceHistory =  resourceMap.get(resource);
					for(String rs : resourceHistory){
						output+=","+rs;
					}
					output += "\n";
			}
	    	}
	    }
	    SyrupyParser.writeFile(filename, output);
		
	}
	
	private void updateLowest(Map<String, String> processData) {
		String pid = processData.get("PID");
		Map <String,Double> resourceMap = (Map<String, Double>) lowest_.get(pid);
		System.out.println("rm keyset " + resourceMap.keySet());
		for(String resource : resourceMap.keySet()){
			System.out.println("Current resource is " + resource);
			if(processData.containsKey(resource) ){
				double lowest = resourceMap.get(resource);
				double resourceValue = Double.parseDouble(processData.get(resource));
				System.out.println("Current value of " + resource+"="+resourceValue);
				if(lowest > resourceValue){
					resourceMap.put(resource, resourceValue);
					lowest_.put(pid,resourceMap);
				}
			}
		}
	}

	private void updateHighest(Map<String, String> processData) {
		String pid = processData.get("PID");
		Map <String,Double> resourceMap = (Map<String, Double>) highest_.get(pid);
		
		for(String resource : resourceMap.keySet()){
			System.out.println("Current resource is " + resource); 
			if(processData.containsKey(resource) ){
				double highest = resourceMap.get(resource);
				double resourceValue = Double.parseDouble(processData.get(resource));
				System.out.println("Current value of " + resource+"="+resourceValue);
				if(highest < resourceValue){
					resourceMap.put(resource, resourceValue);
					highest_.put(pid,resourceMap);
				}
			}
		}																												
		
	}
	

	private void updateAverage(Map<String, String> processData) {
		String pid = processData.get("PID");
		
		Map <String,Double> resourceMap = (Map<String, Double>) average_.get(pid);
		for(String resource : resourceMap.keySet()){
			System.out.println("Current resource is " + resource); 
			System.out.println("process data keys =  " + processData.keySet());
			if(processData.containsKey(resource) ){
				
				double average = resourceMap.get(resource);
				double resourceValue = Double.parseDouble(processData.get(resource));
				System.out.println("Resource value = " + resourceValue);
				double count = resourceMap.get("count");
				double newAverage = ((average* count) + resourceValue)/(count+1.0);
				System.out.println(" newaverage = " +newAverage);
				if(resource!="count"){
					resourceMap.put(resource, newAverage);
					average_.put(pid, resourceMap);
				}
			}
		
		}
		resourceMap.put("count", resourceMap.get("count")+1);
		
	}

	private void updateLowerbound(Map<String, String> processData) {
		// TODO Auto-generated method stub
		
	}

	private void updateUpperbound(Map<String, String> processData) {
		// TODO Auto-generated method stub
		
	}

	public Map getUpperbound() {
		return upperbound_;
	}

	public void setUpperbound(Map upperbound) {
		this.upperbound_ = upperbound;
	}

	public Map getLowerbound() {
		return lowerbound_;
	}

	public void setLowerbound(Map lowerbound) {
		this.lowerbound_ = lowerbound;
	}

	public Map getAverage() {
		return average_;
	}

	public void setAverage(Map average) {
		this.average_ = average;
	}

	public Map getHighest() {
		return highest_;
	}

	public void setHighest(Map highest) {
		this.highest_ = highest;
	}

	public Map getLowest() {
		return lowest_;
	}

	public void setLowest(Map lowest) {
		this.lowest_ = lowest;
	}
	public Map getAllData_() {
		return allData_;
	}

	
}
