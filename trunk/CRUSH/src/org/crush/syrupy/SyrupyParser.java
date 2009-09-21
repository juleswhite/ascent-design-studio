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
	    
	    processTop();
	}
	public void parseInput(String st){
		System.out.println("in parseInput");
		splitContent_ = new ArrayList();
     //  System.out.println ("Parsing st = " + st);
        String [] splitstr = st.split("\n");
        for( int i = 0 ; i < splitstr.length; i ++) {
        	String str = splitstr[i];
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
       
	    
	    processTop();
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
	
	public Map<String,String> processTop (){
		
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
		
		
		
		//
		
	}
	
}
