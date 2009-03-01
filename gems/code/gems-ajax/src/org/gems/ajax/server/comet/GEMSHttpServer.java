package org.gems.ajax.server.comet;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.cometd.Bayeux;
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
		(new GEMSHttpServer()).start(8080);
	}

	public void start(int port) {
		if (running_)
			return;

		running_ = true;

		server_ = new Server(port);

		Context context = new Context(server_, "/", Context.SESSIONS);
		ContinuationCometdServlet cometd = new ContinuationCometdServlet();
		context.addServlet(new ServletHolder(cometd), "/cometd/*");
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
		
		try {
			context.setBaseResource(new ResourceCollection(
					new Resource[] { Resource.newResource("www/"),
							Resource.newResource("src/"), }));
		} catch (Exception e) {
			e.printStackTrace();
		}

		GEMSImpl gems = new GEMSImpl();
		ServletHolder gemsh = new ServletHolder(gems);
		context.addServlet(gemsh, "/org.gems.ajax.Designer/gems");
		context.setContextPath("/");

		
		TemplateManagerImpl loader = new TemplateManagerImpl();
		ServletHolder loaderh = new ServletHolder(loader);
		context.addServlet(loaderh, "/org.gems.ajax.Designer/templateManager");

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setResourceBase("www/");
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, context });
		server_.setHandler(handlers);
		try {
			server_.start();
		} catch (Exception e) {
		}

		
	}
}
