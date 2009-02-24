package org.gems.ajax.client.edit;

import org.gems.ajax.client.figures.properties.PropertyEditor;
import org.gems.ajax.client.model.Property;
import org.gems.ajax.client.views.View;

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

public interface EditPartFactory {

	public EditPart createEditPart(View view, Object model);
	
	public PropertyEditor createPropertyEditor(View view, Object model, Property prop);
	
	public ConnectionEditPart createConnectionEditPart(View view, Object con);
}
