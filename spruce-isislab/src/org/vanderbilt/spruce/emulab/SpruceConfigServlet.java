package org.vanderbilt.spruce.emulab;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SpruceConfigServlet extends HttpServlet
{
  private String user_;
  private String pass_;
  private String url_;
  private String projectDirectory_ = "/proj/";
  private String projectName_ = "LMATLProject";
  private ExecutorService executor_ = Executors.newFixedThreadPool(5);

  public SpruceConfigServlet(String url)
  {
    this.url_ = url;
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("in do post of experiments servlet");
    doGet(request, response);
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    String paramsFile = req.getParameter("paramsFile");
    System.out.println("paramsFile = " + paramsFile);
    String paramForm = "";
    String lineOfFile = null;
    ParamParser p = new ParamParser(paramsFile);

    paramForm = p.prepareForm("");
    ArrayList<HashMap<String,String>> myArgs = p.getMyArgs();
    ArrayList<String> argTypes = new ArrayList();
   
    ArrayList<String>argValues = new ArrayList();
    for (HashMap arg : myArgs)
    {
      argTypes.add((String)arg.get("type"));
      System.out.println(arg.keySet());
      argValues.add((String)arg.get("value"));
    }
    String eid = req.getParameter("exp");

    String formTop = "<form method=\"POST\" action=\""+ContentHolder.rootURL+"/pValidate?exp=" + eid;

    int argCount = 0;
    for (String type : argTypes) {
      argCount++;
      formTop = formTop + "&arg" + argCount + "type" + "=" + type;
      System.out.println(" form top = " + formTop);
    }
    formTop = formTop + "&paramsFile=" + paramsFile + "\">";
    paramForm = formTop + paramForm;

    resp.setContentType("text/html");
    resp.getWriter().write("<html><body>" + paramForm + "</body></html>");
  }
}