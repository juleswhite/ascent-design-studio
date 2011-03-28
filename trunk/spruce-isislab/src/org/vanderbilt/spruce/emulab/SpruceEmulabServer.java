package org.vanderbilt.spruce.emulab;

/*******************************************************************************
 * Copyright 2009 Jules White,Brian Dougherty
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * This class is the entry point for the Spruce Testbed Integration (STI) server. The class spawns an
 * HTTP server and registers the core STI servlets.
 * 
 * @author jules,brian
 * 
 */
public class SpruceEmulabServer {

	private static final Logger logger_ = Logger
			.getLogger(SpruceEmulabServer.class.getName());

	private boolean running_ = false;

	private static Server server_;

	/**
	 * Stops the SpruceEmulabServer server.
	 */
	public void stop() {
		try {
			server_.stop();
		} catch (Exception e) {
		}
		running_ = false;
	}

	/**
	 * Entry Point
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int port = 8090; //opens up server on port 8090
		Properties props = new Properties();
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
				
				
				props.load(new FileInputStream(".spruce")); // loads the .spruce properties file. contains the properties "uo"
				//the operating user and "op" is the password
				System.out.println(" The properties are "+props);
			} catch (Exception e) {
				// Need to modify this to print the correct usage...
				logger_.log(Level.SEVERE, "Probably an invalid port number:"
						+ args[0], e);
				return;
			}
		}

		(new SpruceEmulabServer()).start(port,args[1],props.getProperty("uo"),props.getProperty("op")); //call to actually load the server
	}

	/**
	 * Starts the STI server on the specified port.
	 * 
	 * @param port
	 */
	public void start(int port, String url, String user, String pass) {
		if (running_)
			return;

		running_ = true;

		server_ = new Server(port);
		Context context = new Context(server_, "/", 1);
		System.out.println(" url = " + url);
		HttpServlet servlet = new EmulabConnectorServlet(url, user, pass);
		HttpServlet wlservlet = new SpruceWriteLaunchServlet(url, user, pass);
		HttpServlet pVservlet = new ParamValidator(url, user, pass);
		HttpServlet ilservlet = new IntermediateLaunchServlet(url, user, pass);
		HttpServlet mkexservlet = new SpruceMakeExpServlet(url, user, pass);
		context.addServlet(new ServletHolder(wlservlet), "/writeLaunch");
		context.addServlet(new ServletHolder(pVservlet), "/pValidate");
		context.addServlet(new ServletHolder(servlet), "/emulab/*");
		context.addServlet(new ServletHolder(new SpruceEmulabResultsServlet()), "/results/*");
		context.addServlet(new ServletHolder(new IsisLabLogin()), "/login");
		context.addServlet(new ServletHolder(mkexservlet), "/makeExp");
		context.addServlet(new ServletHolder(ilservlet), "/inter");
		context.addServlet(new ServletHolder(new SpruceParamsServlet(url)), "/validate");
		context.addServlet(new ServletHolder(new SpruceConfigServlet(url)), "/config");
		context.setResourceBase(new File(".").getAbsolutePath());
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setBaseResource(context.getBaseResource());
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, context });
		server_.setHandler(handlers);
		

		try {
			server_.start();
		} catch (Exception e) {
			logger_
					.log(
							Level.SEVERE,
							"Something went horribly wrong...and we have no idea why....",
							e);
		}
	}

}
