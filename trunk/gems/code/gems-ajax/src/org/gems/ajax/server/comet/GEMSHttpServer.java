package org.gems.ajax.server.comet;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.cometd.Bayeux;
import org.gems.ajax.client.model.event.ModelEventRemoting;
import org.gems.ajax.server.GEMSImpl;
import org.gems.ajax.server.figures.templates.TemplateManagerImpl;
import org.mortbay.cometd.continuation.ContinuationCometdServlet;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.resource.Resource;
import org.mortbay.resource.ResourceCollection;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class GEMSHttpServer {

	public static final String TEMPLATE_MANAGER_RPC = "/templateManager";

	public static final String GEMS_RPC = "/gems";

	public static final String GEMS_WEBAPP_PATH = "/org.gems.ajax.Designer";

	private static final Logger logger_ = Logger.getLogger(GEMSHttpServer.class
			.getName());

	private boolean running_ = false;
	private Server server_;

	public void stop() {
		try {
			server_.stop();
		} catch (Exception e) {
		}
		running_ = false;
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		if(args.length > 0){
			try{port = Integer.parseInt(args[0]);}catch (Exception e) {
				//Need to modify this to print the correct usage...
				System.out.println("Invalid port number:"+args[0]);
				return;
			}
		}
		
		(new GEMSHttpServer()).start(port);
	}

	public void start(int port) {
		if (running_)
			return;

		running_ = true;

		server_ = new Server(port);

		Context context = new Context(server_, "/", Context.SESSIONS);
		ContinuationCometdServlet cometd = new ContinuationCometdServlet();
		context.addServlet(new ServletHolder(cometd), GEMS_WEBAPP_PATH
				+ ModelEventRemoting.COMETD_PATH+"/*");
		context.addEventListener(new ServletContextAttributeListener() {

			public void attributeReplaced(ServletContextAttributeEvent arg0) {
			}

			public void attributeRemoved(ServletContextAttributeEvent arg0) {
			}

			public void attributeAdded(ServletContextAttributeEvent event) {
				if (Bayeux.DOJOX_COMETD_BAYEUX.equals(event.getName()))
					ModelRPC.launch((Bayeux) event.getValue());
			}
		});

		GEMSImpl gems = new GEMSImpl();
		ServletHolder gemsh = new ServletHolder(gems);
		context.addServlet(gemsh, GEMS_WEBAPP_PATH + GEMS_RPC);
		context.setContextPath("/");

		try {
			context.setBaseResource(new ResourceCollection(
					new Resource[] { Resource.newResource("www/"),
							Resource.newResource("src/"), }));
		} catch (Exception e) {
			logger_
					.log(
							Level.SEVERE,
							"Unable to start the GEMS server. There was an " +
							"error setting up the resource base of the web context.",
							e);
		}

		TemplateManagerImpl loader = new TemplateManagerImpl();
		ServletHolder loaderh = new ServletHolder(loader);
		context.addServlet(loaderh, GEMS_WEBAPP_PATH + TEMPLATE_MANAGER_RPC);

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setResourceBase("www/");
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, context });
		server_.setHandler(handlers);

		try {
			server_.start();
		} catch (Exception e) {
			logger_.log(Level.SEVERE, "Unable to start the GEMS server.", e);
		}

	}
}
