package org.gems.ajax.client;

import java.util.Map;

import org.gems.ajax.client.edit.ConnectionEditPart;
import org.gems.ajax.client.edit.EditPartFactory;
import org.gems.ajax.client.edit.EditPartManager;
import org.gems.ajax.client.edit.ModelEditPart;
import org.gems.ajax.client.model.ModelHelper;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class ConnectionLoader {
	
	private ConnectionLoader(){}

	public static void loadConnections(ModelHelper helper, EditPartFactory factory, Object cmo,
			Map traversed,
			Map loaded) {

		if (traversed.get(cmo) == null) {
			traversed.put(cmo, true);

			for (Object assoc : helper.getConnections(cmo)) {
				if (loaded.get(assoc) == null) {
					loaded.put(assoc, assoc);
					loadAssociation(helper, factory, assoc);
				}
			}
			for (Object child : helper.getChildren(cmo)) {
				loadConnections(helper, factory, child, traversed, loaded);
			}
		}
	}

	public static void loadAssociation(ModelHelper helper, EditPartFactory factory, Object assoc) {
		ModelEditPart src = (ModelEditPart) EditPartManager.getEditPart(helper.getSource(assoc));
		ModelEditPart trg = (ModelEditPart) EditPartManager.getEditPart(helper.getTarget(assoc));

		ConnectionEditPart connectionEditPart = factory
				.createConnectionEditPart(src.getView(), assoc);
		connectionEditPart.setConnectionLayer(src.getModelFigure().getDiagram()
				.getConnectionLayer());

		connectionEditPart.attach(src, trg);
	}
}
