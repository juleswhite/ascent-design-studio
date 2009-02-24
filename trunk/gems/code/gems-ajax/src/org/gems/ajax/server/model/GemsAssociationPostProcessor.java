package org.gems.ajax.server.model;

import org.gems.ajax.client.model.ClientAssociation;
import org.gems.ajax.client.model.ClientModelObject;

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
public class GemsAssociationPostProcessor extends
		AbstractAssociationClassPostProcessor {

	@Override
	public ClientModelObject getSource(ClientModelObject assoc) {
		for(ClientAssociation p : assoc.getAssociations())
			if(p.getType() != null && p.getType().getName() != null)
				if(p.getType().getName().endsWith("Source"))
					if(p.getSource() == assoc)
						return p.getTarget();
					else
						return p.getSource();
		
		return null;
	}

	@Override
	public ClientModelObject getTarget(ClientModelObject assoc) {
		for(ClientAssociation p : assoc.getAssociations())
			if(p.getType() != null && p.getType().getName() != null)
				if(p.getType().getName().endsWith("Target"))
					if(p.getSource() == assoc)
						return p.getTarget();
					else
						return p.getSource();
		
		return null;
	}

	@Override
	public boolean isAssociationClass(ClientModelObject cmo) {
		return cmo.getTypes().get(0).getName().endsWith("Connection") && !cmo.getTypes().get(0).getName().equals("Connection");
	}

}
