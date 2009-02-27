package org.gems.ajax.client.edit;

import org.gems.ajax.client.edit.exdata.ExtendedData;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.views.View;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public abstract class AbstractEditPart implements EditPart {
	private View view_;
	private ExtendedData extendedData_;
	private Object model_;
	private ModelHelper modelHelper_;
	private EditPartFactory factory_;
	private GEMSDiagram diagram_;

	public AbstractEditPart(ModelHelper modelHelper, EditPartFactory factory,
			Object model) {
		super();
		modelHelper_ = modelHelper;
		factory_ = factory;
		model_ = model;
		EditPartManager.mapPartToModel(model_, this);
	}

	public ModelHelper getModelHelper() {
		return modelHelper_;
	}

	public void setModelHelper(ModelHelper modelHelper) {
		modelHelper_ = modelHelper;
	}

	public Object getModel() {
		return model_;
	}

	public EditPartFactory getFactory() {
		return factory_;
	}

	public void setFactory(EditPartFactory factory) {
		factory_ = factory;
	}

	public void setModel(Object model) {
		model_ = model;
	}

	public ExtendedData getExtendedData() {
		if (extendedData_ == null) {
			return new ExtendedData();
		}
		return extendedData_;
	}

	public void setExtendedData(ExtendedData extendedData) {
		extendedData_ = extendedData;
	}

	public GEMSDiagram getDiagram() {
		return diagram_;
	}

	public void setDiagram(GEMSDiagram diagram) {
		diagram_ = diagram;
	}

	public View getView() {
		return view_;
	}

	public void setView(View view) {
		view_ = view;
	}

}