package org.gems.ajax.client;

/******************************************************************************
 * Copyright (c) 2007 Jules White.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jules White - initial API and implementation 
 ****************************************************************************/
import org.gems.ajax.client.edit.EditDomain;
import org.gems.ajax.client.edit.EditManager;
import org.gems.ajax.client.edit.tools.ConnectionTool;
import org.gems.ajax.client.event.UIEventDispatcher;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.model.BasicModelHelper;
import org.gems.ajax.client.model.ClientModelObject;
import org.gems.ajax.client.model.ModelLoader;
import org.gems.ajax.client.model.ModelLoaderAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Designer implements EntryPoint {

	private ModelLoaderAsync modelLoaderService_;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		modelLoaderService_ = (ModelLoaderAsync) GWT
				.create(ModelLoader.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) modelLoaderService_;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "modelLoader";
		endpoint.setServiceEntryPoint(moduleRelativeURL);
		modelLoaderService_.loadModel("foo", new AsyncCallback<ClientModelObject>() {

            public void onFailure(Throwable caught) {
              Window.alert("Unable to load the specified model!");
            }

            public void onSuccess(ClientModelObject result) {
            	
            	EditManager emanager = new EditManager();
        		ToolEntry te = new ToolEntry("Select", "Select",
        				"A tool to select an element.", SelectionManager.getInstance()
        						.getSelectionTool());
        		ToolEntry ce = new ToolEntry("Connect", "Connect",
        				"A tool to create connectsion between elements.",
        				ConnectionTool.getInstance());
        		emanager.setCurrentTool(SelectionManager.getInstance()
        				.getSelectionTool());
        		UIEventDispatcher.getInstance().addUIListener(emanager);
        		UIEventDispatcher.getInstance().addKeyListener(emanager);

        		GEMSEditor editor = new GEMSEditor(new EditDomain(emanager));
        		// editor.getViews().getDeckPanel().setAnimationEnabled(true);

        		// GEMSEditor editor = new GEMSEditor(new EditDomain(emanager));
        		// editor.getPaletteManager().addToolEntry(te);
        		// editor.getPaletteManager().addToolEntry(ce);
        		//
        		
        		GEMSDiagram dig = editor.open(new BasicModelHelper(), result);// new

        		// GEMSDiagram();
        		// editor.open(new MetaModelHelper(), new new MetaClass(), null,
        		// "Components and Nodes");
        		// ConnectionLayer clayer = new ConnectionLayer();
        		// dig.setConnectionLayer(clayer);
        		// dig.setSize("800px", "600px");
        		// clayer.setSize("800px", "600px");

        		// GEMSPanel src = new GEMSPanel(dig);
        		// GEMSPanel trg = new GEMSPanel(dig);
        		// dig.add(src,30,30);
        		// dig.add(trg,100,100);

        		// RootPanel.get("slot1").add(editor);
        		// RootPanel.get("slot1").add(clayer);

        		// src.connectTo(trg);

        		editor.addView(dig, "View 1");

        		editor.getViews().selectTab(0);
        		RootPanel.get("slot1").add(editor);
        		dig.setSize("2001px", "2001px");
            }
        });

		

	}

	// private TabPanel centerPanel;
	//   
	// public void onModuleLoad() {
	//
	// //create the main panel and assign it a BorderLayout
	// Panel mainPanel = new Panel();
	// mainPanel.setLayout(new BorderLayout());
	//
	// BorderLayoutData northLayoutData = new
	// BorderLayoutData(RegionPosition.NORTH);
	// northLayoutData.setSplit(false);
	//
	// BorderLayoutData centerLayoutData = new
	// BorderLayoutData(RegionPosition.CENTER);
	// centerLayoutData.setMargins(new Margins(5, 0, 5, 5));
	//
	// Panel centerPanelWrappper = new Panel();
	// centerPanelWrappper.setLayout(new FitLayout());
	// centerPanelWrappper.setBorder(false);
	// centerPanelWrappper.setBodyBorder(false);
	//
	// centerPanel = new TabPanel();
	// centerPanel.setBodyBorder(false);
	// centerPanel.setEnableTabScroll(true);
	// centerPanel.setAutoScroll(true);
	// centerPanel.setAutoDestroy(false);
	// centerPanel.setActiveTab(0);
	//
	//
	// centerPanel.setLayoutOnTabChange(true);
	// centerPanel.setTitle("Main Content");
	//
	//       
	// //setup the west regions layout properties
	// BorderLayoutData westLayoutData = new
	// BorderLayoutData(RegionPosition.WEST);
	// westLayoutData.setMargins(new Margins(5, 5, 0, 5));
	// westLayoutData.setCMargins(new Margins(5, 5, 5, 5));
	// westLayoutData.setMinSize(155);
	// westLayoutData.setMaxSize(350);
	// westLayoutData.setSplit(true);
	//
	// //create the west panel and add it to the main panel applying the west
	// region layout properties
	// Panel westPanel = createWestPanel();
	// mainPanel.add(westPanel, westLayoutData);
	//
	// final Panel introPanel = new Panel();
	// introPanel.setTitle("GWT-Ext 2.0 Showcase");
	// introPanel.setPaddings(10);
	// introPanel.setLayout(new FitLayout());
	// //introPanel.add(new ComplexSample());
	//
	// centerPanel.add(introPanel, centerLayoutData);
	// centerPanelWrappper.add(centerPanel);
	// mainPanel.add(centerPanelWrappper, centerLayoutData);
	//
	// final String initToken = History.getToken();
	//        
	//
	// Viewport viewport = new Viewport(mainPanel);
	//
	// // Add history listener
	//        
	// }
	//
	// private Panel createWestPanel() {
	// Panel westPanel = new Panel();
	// westPanel.setId("side-nav");
	// westPanel.setTitle("Showcase Explorer");
	// westPanel.setLayout(new FitLayout());
	// westPanel.setWidth(210);
	// westPanel.setCollapsible(true);
	//
	// Toolbar toolbar = new Toolbar();
	// toolbar.addFill();
	// toolbar.addItem(new ToolbarTextItem("Select Theme "));
	// toolbar.addSpacer();
	// //toolbar.addField(new ThemeChanger());
	// westPanel.setTopToolbar(toolbar);
	//        
	// TabPanel tabPanel = new TabPanel();
	// tabPanel.setActiveTab(0);
	// tabPanel.setDeferredRender(true);
	// tabPanel.setTabPosition(Position.BOTTOM);
	// // TreePanel treePanel = screenManager.getTreeNav();
	//
	// //tabPanel.add(treePanel);
	// //tabPanel.add(screenManager.getAccordionNav());
	// westPanel.add(tabPanel);
	//
	// return westPanel;
	// }

}
