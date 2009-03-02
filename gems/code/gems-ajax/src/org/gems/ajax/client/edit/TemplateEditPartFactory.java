package org.gems.ajax.client.edit;

import java.util.Map;

import org.gems.ajax.client.figures.properties.InlineBooleanEditor;
import org.gems.ajax.client.figures.properties.InlineEnumerationEditor;
import org.gems.ajax.client.figures.properties.InlineIntegerEditor;
import org.gems.ajax.client.figures.properties.InlineNumberEditor;
import org.gems.ajax.client.figures.properties.InlineTextEditor;
import org.gems.ajax.client.figures.properties.PropertyEditor;
import org.gems.ajax.client.figures.templates.ClientTemplateUpdater;
import org.gems.ajax.client.figures.templates.ServerTemplateUpdater;
import org.gems.ajax.client.figures.templates.Template;
import org.gems.ajax.client.figures.templates.TemplateManager;
import org.gems.ajax.client.figures.templates.TemplateManagerAsync;
import org.gems.ajax.client.figures.templates.TemplateParser;
import org.gems.ajax.client.figures.templates.TemplateUpdater;
import org.gems.ajax.client.figures.templates.TemplateUpdaterInfo;
import org.gems.ajax.client.model.EnumProperty;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.Property;
import org.gems.ajax.client.model.Type;
import org.gems.ajax.client.views.View;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class TemplateEditPartFactory implements EditPartFactory {

	private class TemplateUpdaterSetter implements AsyncCallback<TemplateUpdaterInfo> {
		private DiagramTemplateEditPart target_;

		public TemplateUpdaterSetter(DiagramTemplateEditPart target) {
			super();
			target_ = target;
		}

		public void onFailure(Throwable caught) {
		}

		public void onSuccess(TemplateUpdaterInfo result) {
			TemplateUpdater updater = null;
			if(result.isClientSide())
				updater = new ClientTemplateUpdater(result.getTemplate());
			else
				updater = new ServerTemplateUpdater(result.getId(),manager_);
			
			target_.setUpdater(updater);
		}		
	}
	
	private ModelHelper modelHelper_;
	
	private TemplateManagerAsync manager_;

	public TemplateEditPartFactory(ModelHelper modelHelper) {
		super();
		modelHelper_ = modelHelper;

		manager_ = (TemplateManagerAsync) GWT.create(TemplateManager.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) manager_;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "templateManager";
		endpoint.setServiceEntryPoint(moduleRelativeURL);
	}

	public ConnectionEditPart createConnectionEditPart(View view, Object con) {
		ConnectionEditPart cep = new RectilinearConnectionEditPart(
				modelHelper_, this, con);
		cep.setView(view);
		return cep;
	}

	public EditPart createEditPart(View view, Object model) {
		TemplateParser tp = new TemplateParser();

		Template t = null;
		// if(model instanceof MetaClass)
		t = tp
				.parse(
						modelHelper_,
						"<div gemstype=\"container\" childmappings=\"MetaClass:metaclass,MMethod:mmethod\" styleprimaryname=\"ttest\"  resizable=\"true\" moveable=\"true\" initwidth=\"200px\" initheight=\"200px\">\r\n"
								+ " <table width=\"100%\"><tr><td width=\"100%\" style=\"border:solid 1px rgb(0,0,0)\"> "
								+ " <div width=\"100%\">\r\n"
								+ "  Children:\r\n"
								+ "  </div>\r\n"
								+ "  <div id=\"children\" width=\"100%\">\r\n"
								+ "\r\n"
								+ "\r\n"
								+ "  </div>\r\n"
								+ "</td></tr></table></div>");
		// else
		// t = tp.parse(modelHelper_,
		// "<div gemstype=\"container\" childmappings=\"MetaClass:metaclass,MMethod:mmethod\" styleprimaryname=\"mtest\"  resizable=\"true\" moveable=\"false\">\r\n"
		// +
		// "<p>Foo</p>" +
		// "</div>");
		EditPart ep = createModelEditPart(view, t, model);
		ep.setView(view);
		return ep;
	}

	public EditPart createModelEditPart(View v, Template template, Object model) {
		
		DiagramTemplateEditPart mep = null;
		mep = new DiagramTemplateEditPart(modelHelper_, this, model, template);
		manager_.getTemplateUpdaterInfo(v.getId(), modelHelper_.getId(model), new TemplateUpdaterSetter(mep));
				Map<Type, String> cids = template.getContainerIds();
		mep.setContainerIds(cids);
		return mep;
	}

	public PropertyEditor createPropertyEditor(View view, Object model,
			Property prop) {
		PropertyEditor ed = null;
		if (Property.STRING.equals(prop.getType())) {
			ed = new InlineTextEditor("" + prop.getValue());
		} else if (Property.INT.equals(prop.getType())) {
			ed = new InlineIntegerEditor(Integer.parseInt("" + prop.getValue()));
		} else if (Property.DECIMAL.equals(prop.getType())) {
			ed = new InlineNumberEditor(Double
					.parseDouble("" + prop.getValue()));
		} else if (Property.BOOLEAN.equals(prop.getType())) {
			ed = new InlineBooleanEditor(Boolean.parseBoolean(""
					+ prop.getValue()));
		} else if (Property.ENUM.equals(prop.getType())) {
			ed = new InlineEnumerationEditor(((EnumProperty) prop)
					.getAllowedValues(), "" + prop.getValue());
		}

		return ed;
	}

}
