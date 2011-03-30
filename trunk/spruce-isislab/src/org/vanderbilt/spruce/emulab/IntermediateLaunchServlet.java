package org.vanderbilt.spruce.emulab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IntermediateLaunchServlet extends HttpServlet {

	private String user_;
	private String pass_;
	private String url_;
	private String projectDirectory_ = "/proj/";
	private String projectName_ = "LMATLProject/spruce";
	private ExecutorService executor_ = Executors.newFixedThreadPool(5);
	
	public  IntermediateLaunchServlet(String url, String user, String pass) {
		super();
		url_ = url;
		user_ = user;
		pass_ = pass;
	}
	

	public void doPost(HttpServletRequest request,
	         HttpServletResponse response)throws ServletException, IOException {
		doGet(request,response);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		String str = "0";
		Authenticator.setDefault(new MyAuthenticator());
		str = str.trim();
		String eidnum = req.getParameter("id");
		System.out.println("in do get of Intermediatelaunch");
	    while(!(str.equalsIgnoreCase("1"))){
	    	str = str.trim();
	    	try {
	    		System.out.println(" not done, about to sleep");
	    		try {
	    		  	 // Create a URL for the desired page
	    		  		String filename=eidnum+"/done.txt" ;
	    		  	//RLRequest request = new URLRequest("https://www.isislab.vanderbilt.edu/checkFile.php?id="+filename);
	    		  	  
	    		  	 URL url = new URL("http://www.isislab.vanderbilt.edu/checkFile.php?id="+filename);
	    		  	  
	    		  	
	    		  	 // Read all the text returned by the server
	    		  	 BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	    		  	 str = in.readLine();
	    		  	 //while (str.equalsIgnoreCase("1")) {
	    		     System.out.println("RESPONSE FROM STATUS CHECK for file" + filename+"  str = "+str);
	    		  	     // str is one line of text; readLine() strips the newline character(s)
	    		  	 //}
	    		  	 in.close();
	    		  	} 
	    		  	//System.out.println(in.)
	    		  	catch (MalformedURLException e) {
	    		  		System.out.println(e);
	    		  	} 
	    		  	catch (IOException e) {
	    		  		System.out.println(e);
	    		  	}
	    		  	System.out.println(" Found done.txt = "+str);
	    		  	str = str.trim();
	    		  	System.out.println(" sting = ***"+str+"***");
	    		  	Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	    resp.getWriter().flush();
	    resp.getWriter().write("<p><img src='"+ContentHolder.rootURL+"/complete.png'></img><div><a href="+ContentHolder.rootURL+"/results?exp="+eidnum+">View Experiment Results</a>,</p>");
	    resp.setContentType("text/html");
	    //String funHtml = "<html><body><img src=\"http://afrl-gift.dre.vanderbilt.edu:8090/complete.png\"></img></body></html>";
	    resp.getWriter().flush();
	    //resp.getWriter().write(funHtml);
	}
}
