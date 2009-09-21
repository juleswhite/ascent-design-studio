package org.crush.syrupy;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmdExec {

	public CmdExec(){}
	
	public String run(String cmdLine) {
	    String line;
	    String output = "";
	    try {
	        Process p = Runtime.getRuntime().exec(cmdLine);
	        BufferedReader input = new BufferedReader
	            (new InputStreamReader(p.getInputStream()));
	        while ((line = input.readLine()) != null) {
	            output += (line + '\n');
	        }
	        input.close();
	        }
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return output;
	}
}
