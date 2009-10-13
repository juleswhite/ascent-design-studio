package org.vanderbilt.spruce.emulab;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmulabConnectorServlet extends HttpServlet {

	private String user_;
	private String pass_;
	private String url_;
	
	private ExecutorService executor_ = Executors.newFixedThreadPool(5);
	
	public EmulabConnectorServlet(String url, String user, String pass) {
		super();
		url_ = url;
		user_ = user;
		pass_ = pass;
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eidnum="";
		try {
			eidnum = req.getParameter("exp");

			if (eidnum != null && eidnum.trim().length() > 0) {

				int eid = Integer.parseInt(eidnum);
				LauchRequest lr = new LauchRequest(url_,user_,pass_,eidnum);
				executor_.execute(lr);
			}
		} catch (Exception e) {

		}
		resp.setContentType("text/html");
		resp.getWriter().write("<html><body style='background:#000000'><img src='/spruceisislab.png'></img><div><a href=http://afrl-gift.dre.vanderbilt.edu:8090/results?exp="+eidnum+">View Experiment Results</a>,/div></body></html>");

	}

}
