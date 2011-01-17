//https://www.isislab.vanderbilt.edu/showuser.php3?user=10053
package org.vanderbilt.spruce.emulab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SpruceConfigServlet extends HttpServlet {

	private String user_;
	private String pass_;
	private String url_;
	private String projectDirectory_ = "/proj/";
	private String projectName_ = "LMATLProject";
	private ExecutorService executor_ = Executors.newFixedThreadPool(5);
	
	public SpruceConfigServlet(String url) {
		super();
		url_ = url;
	}
	public void doPost(HttpServletRequest request,
	         HttpServletResponse response)throws ServletException, IOException {
		System.out.println("in do post of experiments servlet");
		doGet(request,response);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String paramsFile = req.getParameter("paramsFile");
		System.out.println("paramsFile = " +paramsFile);
		String paramForm = "";
		String lineOfFile = null;
		ParamParser p = new ParamParser(paramsFile);
		paramForm = p.prepareForm();
		ArrayList<HashMap<String, String>> myArgs = p.getMyArgs();
		ArrayList<String> argTypes = new ArrayList();
		for( HashMap<String, String> arg: myArgs){
			
			argTypes.add(arg.get("type"));
		}
		String eid = req.getParameter("exp");
		/*try{
			 FileReader input = new FileReader(paramsFile);
			 BufferedReader bufRead = new BufferedReader(input);
			   // String that holds current file line
			 int count = 0;  // Line number of count 
			 lineOfFile= bufRead.readLine();
			 paramForm += lineOfFile;
			 count++;
			 while (lineOfFile != null){
				 System.out.println(count+": "+paramForm);
				 lineOfFile = bufRead.readLine();
				 paramForm+= lineOfFile;
			 	 count++;
			 }
			 bufRead.close();
			
		}catch(Exception e){
			System.out.println("Caught exception: " + e);
		}*/
		System.out.println("paramFrom = " + paramForm);
		String formTop = "<form method=\"POST\" action=\"http://afrl-gift.dre.vanderbilt.edu:8090/writeLaunch?exp="+eid;
		int argCount=0;
		for( String type: argTypes){
			argCount++;
			formTop  = formTop + "&arg"+argCount+"type"+"="+type;
		}
		formTop = formTop + "\">";
		paramForm = formTop + paramForm;
		
		resp.setContentType("text/html");
		resp.getWriter().write("<html><body>"+paramForm+"</body></html>");
	}

}
