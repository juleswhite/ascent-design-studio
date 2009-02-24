package org.gems.ajax.client.edit;

import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.Property;
import org.gems.ajax.client.views.View;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class GEMSEditPartFactory implements EditPartFactory {

	private ModelHelper modelHelper_;

	public GEMSEditPartFactory(ModelHelper modelHelper) {
		super();
		modelHelper_ = modelHelper;
	}

	public ConnectionEditPart createConnectionEditPart(View v, Object con) {
		ConnectionEditPart cep = new RectilinearConnectionEditPart(modelHelper_, this, con);
		cep.setView(v);
		return cep;
	}

	public EditPart createEditPart(View v, Object model) {
		EditPart ep = new DiagramPanelEditPart(modelHelper_, this, model);
	
		ep.setView(v);
		return ep;
	}

	public org.gems.ajax.client.figures.properties.PropertyEditor createPropertyEditor(
			View view, Object model, Property prop) {
		// TODO Auto-generated method stub
		return null;
	}



	
}
