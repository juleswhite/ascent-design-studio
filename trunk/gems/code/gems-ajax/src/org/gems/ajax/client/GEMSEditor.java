package org.gems.ajax.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gems.ajax.client.edit.EditDomain;
import org.gems.ajax.client.edit.EditManager;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.EditPartFactory;
import org.gems.ajax.client.edit.EditPartManager;
import org.gems.ajax.client.edit.ModelEditPart;
import org.gems.ajax.client.edit.RootEditPart;
import org.gems.ajax.client.edit.TemplateEditPartFactory;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.model.LockingModelHelper;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.views.DefaultView;
import org.gems.ajax.client.views.View;

import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class GEMSEditor extends DockPanel {

	private DecoratedTabPanel views_;
	private HorizontalPanel southPanel_;
	private HorizontalPanel northPanel_;
	private VerticalPanel toolPanel_;

	// private Map views_ = new HashMap<String,RootEditPart>();
	// private Panel toolPalette_;
	// private TabPanel propertiesTabPanel_;
	// private TabPanel modelTabPanel_;
	// private Panel centerPanel_;
	private Map<Object, Integer> viewIndexByRoot_ = new HashMap<Object, Integer>();
	private EditManager editManager_;
	private PaletteManager paletteManager_;
	private EditDomain editDomain_;

	public GEMSEditor(EditDomain d) {
		d.setEditor(this);
		editManager_ = d.getEditManager();
		editDomain_ = d;

		views_ = new DecoratedTabPanel();
		add(views_, CENTER);

		toolPanel_ = new VerticalPanel();
		add(toolPanel_, WEST);
	}

	public GEMSDiagram open(ModelHelper helper, Object rootobj) {
		LockingModelHelper locker = new LockingModelHelper(helper);
		return open(locker, new TemplateEditPartFactory(locker), rootobj,
				new DefaultView(rootobj));
	}
	

	
	

	public GEMSDiagram open(ModelHelper helper, EditPartFactory figfact,
			Object rootobj, View view) {

		LockingModelHelper locker = null;
		if(!(helper instanceof LockingModelHelper)){
			locker = new LockingModelHelper(helper);
			helper = locker;
		}
		else{
			locker = (LockingModelHelper)helper;
		}
		
		GEMSDiagram diagram = new GEMSDiagram();
		RootEditPart rootep = new RootEditPart(helper, figfact, diagram,
				rootobj);
		rootep.setEditDomain(getEditDomain());
		EditPartManager.mapPart(rootobj, diagram, rootep);

		/*
		 * InlineFilePropertyEditor te = new
		 * InlineFilePropertyEditor("c:/foo/bar.txt"); diagram.add(te,100,100);
		 * 
		 * InlineTextEditor te2 = new InlineTextEditor("foo\nbar");
		 * diagram.add(te2,200,200);
		 * 
		 * InlineNumberEditor ne = new InlineNumberEditor(34356);
		 * diagram.add(ne,300,300);
		 * 
		 * ExpandoFigure ee = new ExpandoFigure(diagram);
		 * diagram.add(ee,300,320);
		 */

	
		
		// DiagramImagePanel dp = new DiagramImagePanel(diagram);
		// dp.addGlobalStyleDependentName("ip");
		// diagram.add(dp,100,100);
		// DetailFigure df = new DetailFigure(new HTML("<b>foooo</b>"));
		// df.attach(diagram, 400,400);
		// ListBox lb = new ListBox(false);
		// lb.addItem("foo");
		// lb.addItem("bar");
		// diagram.add(lb,350,350);

		locker.setLocked(true);
		
		List children = helper.getChildren(rootobj);
		for (Object o : children) {
			ModelEditPart ep = load(diagram, o, figfact, helper, view);
			// diagram.add(ep.getModelFigure(), 0, 0);
			if(ep != null)
				rootep.addChild(ep);
		}
		
		ConnectionLoader.loadConnections(helper, figfact, rootobj, new HashMap(), new HashMap());
		
		
		// p.add(diagram);
		diagram.setSize("2001px", "2001px");

		int cv = getViews().getWidgetCount();
		viewIndexByRoot_.put(rootobj, cv);
		addView(diagram, view.getId());
		getViews().selectTab(cv);
		
		locker.setLocked(false);
		
		
		return diagram;
	}
	
	public ModelEditPart load(EditPart ref, Object root){
		return load(ref.getDiagram(),root,ref.getFactory(),ref.getModelHelper(),ref.getView());
	}

	public ModelEditPart load(GEMSDiagram d, Object root, EditPartFactory f,
			ModelHelper mh, View v) {

		ModelEditPart ep = null;
		if (v.isVisible(root)) {

			ep = (ModelEditPart) f.createEditPart(v, root);
			ep.setEditDomain(getEditDomain());

			ep.setDiagram(d);
			ep.getFigure();

			for (Object o : mh.getChildren(root)) {
				ModelEditPart cep = load(d, o, f, mh, v);
				ep.addChild(cep);
			}
		}

		return ep;
	}

	public PaletteManager getPaletteManager() {
		return paletteManager_;
	}

	public EditManager getEditManager() {
		return editManager_;
	}

	public void setEditManager(EditManager editManager) {
		editManager_ = editManager;
	}

	public void setPaletteManager(PaletteManager paletteManager) {
		paletteManager_ = paletteManager;
	}

	public EditDomain getEditDomain() {
		return editDomain_;
	}

	public void setEditDomain(EditDomain editDomain) {
		editDomain_ = editDomain;
	}

	public void addView(Widget w, String text) {
		views_.add(w, text);
	}

	public DecoratedTabPanel getViews() {
		return views_;
	}

	public HorizontalPanel getSouthPanel() {
		return southPanel_;
	}

	public void setSouthPanel(HorizontalPanel southPanel) {
		southPanel_ = southPanel;
	}

	public HorizontalPanel getNorthPanel() {
		return northPanel_;
	}

	public void setNorthPanel(HorizontalPanel northPanel) {
		northPanel_ = northPanel;
	}

	public VerticalPanel getToolPanel() {
		return toolPanel_;
	}

	public void setToolPanel(VerticalPanel toolPanel) {
		toolPanel_ = toolPanel;
	}

	public boolean hasOpenView(Object root) {
		return viewIndexByRoot_.get(root) != null;
	}

	public boolean switchView(Object rootobj) {
		if (hasOpenView(rootobj)) {
			int v = viewIndexByRoot_.get(rootobj);
			getViews().selectTab(v);
			return true;
		}
		return false;
	}
}
