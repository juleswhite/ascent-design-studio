package org.vanderbilt.spruce.emulab;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SpruceEmulabResultsServlet extends HttpServlet {

	public SpruceEmulabResultsServlet(){
	
	}
	
 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

        String results = "";
		try {
			String eidnum = req.getParameter("exp");
		
			if (eidnum != null && eidnum.trim().length() > 0) {
		        String fileName = eidnum+"/results.html";
		        results  = readFile(fileName);
			}
		} catch (Exception e) {
		
		}
		resp.setContentType("text/html");
		resp.getWriter().write(results);
	}
 	
    public void doPost(HttpServletRequest request,
         HttpServletResponse response)throws ServletException, IOException {
    	    String content = request.getParameter("content");
    	    String eid = request.getParameter("exp");
    	   // System.out.println("content received from post parmaeter is " + content);
    	    String fileContent = readFile(eid+"/results.html");
    	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	        Date date = new Date();
    	    content  = fileContent +"<div>"+dateFormat.format(date)+"</div>"+content;
    	    content = content.replaceAll("<html>", "");
    	    content = content.replaceAll("</html>", "");
    	    content = content.replaceAll("<body>", "");
    	    content = content.replaceAll("</body>", "");
    	    content = "<html><body>"+content+"</body></html>";
    	    writeFile("results.html",content,eid);//(//eid +"/results.html"),content);
    	    System.out.println(" about to see if there is params.txt post param");
    	    
    	    if(request.getParameter("params.txt") != null){
    	    	System.out.println(" found params.txt");
        	    writeFile("params.txt",request.getParameter("params.txt"),"/home/briand/"+eid);
    	    }
    	    System.out.println("About to write the "+eid+"parametersForm.txt file with data:");// +request.getParameter("parameterForm"));
    	    writeFile(eid+"parametersForm.txt",request.getParameter("parameterForm"),eid );
    	    response.getWriter().write(content);
    	    
	 		doGet(request, response);
 		}

	private void writeFile (String name, String content, String strDirectory){
		 FileOutputStream Output;
	     PrintStream file;
	     new File(strDirectory).mkdir();
	          //must use a try/catch statement for it to work
	     try
	     {     
	          Output = new FileOutputStream(strDirectory+"/"+name,false);
	          file = new PrintStream(Output);
	          file.print(content);
	        
	     }
	     catch(Exception e)
         {
	          
	     }
	}
	
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
	
	  
}

	


