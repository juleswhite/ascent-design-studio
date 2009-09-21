package org.crush.syrupy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyrupyParser {
	
	File outputFile_;
	String fileContent_ ="";
	List<ArrayList<String>> splitContent_ = new ArrayList();
	
	public SyrupyParser(String filePath){
		outputFile_ = new File(filePath);
	}
	
	public SyrupyParser(){
		
	}
	
	public void readFile(){
		try {
			System.out.println("in read file");
	        BufferedReader in = new BufferedReader(new FileReader(outputFile_));
	        String str;
	        int count = 0;
	        while ((str = in.readLine()) != null) {
	        	fileContent_ += count + str+"\n";
	        	count++;
	            String [] withSpaces = (str.split(" "));
	            ArrayList withoutSpaces = new ArrayList();
	            for(int i = 0; i < withSpaces.length; i++ ){
	            	if(withSpaces[i].length() >=1){
	            		withoutSpaces.add(withSpaces[i]);
	            	}
	            }
	            splitContent_.add(withoutSpaces);
	        }
	        in.close();
	        
	    } catch (IOException e) {
	    	System.out.println(e);
	    }
	    
	    //processTop();
	}
	
	public void parseInput(String st){
		System.out.println("in parseInput");
		splitContent_ = new ArrayList();
     //  System.out.println ("Parsing st = " + st);
        String [] splitstr = st.split("\n");
        for( int i = 0 ; i < splitstr.length; i ++) {
        	String str = splitstr[i];
        	System.out.println("ln+"+ i + str);
            String [] withSpaces = (str.split(" "));
            //System.out.println("str  = " +str);
            ArrayList withoutSpaces = new ArrayList();
            for(int j = 0; j < withSpaces.length; j++ ){
            	if(withSpaces[j].length() >=1){
            		//System.out.println("j = " + j + " value = " + withSpaces[j]);
            		withoutSpaces.add(withSpaces[j]);
            	}
            }
            splitContent_.add(withoutSpaces);
            
        }
       
	    
	   // processTop();
	}
	
	public static void writeFile( String filename, String content){
		try {
			System.out.println("in write file");
	        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
	        out.write(content);
	        out.close();
	        
	    } catch (IOException e) {
	    	System.out.println(e);
	    }
	    
	}
	
	public Map<String,String> processTopPID (){
		
		//System.out.println("fileContent = " +fileContent_);
		//System.out.println(" should be usage =  " + splitContent_.get(3).get(1));
		
		Map<String,String> processMap = new HashMap<String,String>();
		
		String pid = splitContent_.get(25).get(0);
		//System.out.println("row 12 = "+splitContent_.get(25));
		for(String name : splitContent_.get(24)){
			
			//System.out.print (" name ="+name);
			int currentIndex = splitContent_.get(24).indexOf(name);
			String value = splitContent_.get(25).get(currentIndex);
			//System.out.println(",value  = "+value);
			processMap.put(name, value);
		}
		
		
		//System.out.println(" processMap = " + processMap);
		//outputFile_.delete();
		
		return processMap;
		
	}
	
	public Map<String, Map<String,String>> processTopSystemInfo(){
		Map<String, String> innerMap = new HashMap<String,String>();
		Map<String, Map<String,String>> systemMap = new HashMap<String,Map<String,String>> ();
	    innerMap.put("total",splitContent_.get(13).get(1));
	    innerMap.put("running",splitContent_.get(13).get(3));
	    innerMap.put("sleeping",splitContent_.get(13).get(5));
	    innerMap.put("threads",splitContent_.get(13).get(7));
	    systemMap.put("Processes",innerMap);
	    innerMap = new HashMap<String,String>();
	    innerMap.put("date",splitContent_.get(14).get(0));
	    innerMap.put("time",splitContent_.get(14).get(1));
	    systemMap.put("Time",innerMap);
	    innerMap = new HashMap<String,String>();
	    innerMap.put("0",splitContent_.get(15).get(2));
	    innerMap.put("1",splitContent_.get(15).get(3));
	    innerMap.put("2",splitContent_.get(15).get(4));
	    systemMap.put("Load Avg",innerMap);
	    innerMap = new HashMap<String,String>();
	    innerMap.put("user",splitContent_.get(16).get(2));
	    innerMap.put("sys",splitContent_.get(16).get(4));
	    innerMap.put("idle",splitContent_.get(16).get(6));
	    systemMap.put("CPU usage",innerMap);
	    innerMap = new HashMap<String,String>();
	    innerMap.put("resident",splitContent_.get(17).get(1));
	    innerMap.put("data",splitContent_.get(17).get(3));
	    innerMap.put("linkedit",splitContent_.get(17).get(5));
	    systemMap.put("SharedLibs",innerMap);
	    innerMap = new HashMap<String,String>();
	    innerMap.put("total",splitContent_.get(18).get(1));
	    innerMap.put("resident",splitContent_.get(18).get(3));
	    innerMap.put("private",splitContent_.get(18).get(5));
	    innerMap.put("shared",splitContent_.get(18).get(7));
	    systemMap.put("MemRegions",innerMap);
	    innerMap = new HashMap<String,String>();
	    innerMap.put("wired",splitContent_.get(19).get(1));
	    innerMap.put("active",splitContent_.get(19).get(3));
	    innerMap.put("inactive",splitContent_.get(19).get(5));
	    innerMap.put("used",splitContent_.get(19).get(7));
	    innerMap.put("free",splitContent_.get(19).get(9));
	    systemMap.put("PhysMem",innerMap);
	    innerMap = new HashMap<String,String>();
	    innerMap.put("vsize",splitContent_.get(20).get(1));
	    innerMap.put("framework vsize",splitContent_.get(20).get(3));
	    innerMap.put("pageins",splitContent_.get(20).get(6));
	    innerMap.put("pageouts",splitContent_.get(20).get(8));
	    systemMap.put("VM",innerMap);
	    innerMap = new HashMap<String,String>();
	    innerMap.put("packets in",splitContent_.get(21).get(2));
	    innerMap.put("packets out",splitContent_.get(21).get(4));
	    systemMap.put("Networks",innerMap);
	    innerMap = new HashMap<String,String>();
	    innerMap.put("read",splitContent_.get(22).get(1));
	    innerMap.put("written",splitContent_.get(22).get(3));
	    systemMap.put("Disks",innerMap);
	    System.out.println(systemMap);
	    return systemMap;
	}
	
}
