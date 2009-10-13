package org.vanderbilt.spruce.emulab;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ServerPost {
  public ServerPost(){}
  private String readFile (String name){
		String content = "";
		try{
		    // Open the file that is the first 
		    // command line parameter
		    FileInputStream fstream = new FileInputStream(name);
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    
		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		      // Print the content on the cons
		      content += strLine;
		    }
		    //Close the input stream
		    in.close();
		    }
		catch (Exception e){//Catch exception if any
		      //System.err.println("Error: " + e.getMessage());
		    }
		return content;
	}

  public static void main(String args[]) {

    HttpClient client = new HttpClient();
    client.getParams().setParameter("http.useragent", "Test Client");
    ServerPost s = new ServerPost();
    BufferedReader br = null;
    
    PostMethod method = new PostMethod("http://afrl-gift.dre.vanderbilt.edu:8090/results");
    String expId = args[0];
    String fileContent = args[1];                     
    fileContent = s.readFile(args[1]);
    method.addParameter("exp", expId);
    method.addParameter("content",fileContent);

    try{
      int returnCode = client.executeMethod(method);

      if(returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
        System.err.println("The Post method is not implemented by this URI");
        // still consume the response body
        method.getResponseBodyAsString();
      } 
    } catch (Exception e) {
      System.err.println(e);
    } finally {
      method.releaseConnection();
      if(br != null) try { br.close(); } catch (Exception fe) {}
    }

  }
  
}