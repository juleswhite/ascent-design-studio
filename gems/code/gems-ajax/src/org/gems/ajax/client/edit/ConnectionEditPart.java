package org.gems.ajax.client.edit;

import org.gems.ajax.client.connection.ConnectionLayer;
import org.gems.ajax.client.figures.DiagramElement;

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

public interface ConnectionEditPart extends EditPart {

	public boolean isAttached();
	
	public ModelEditPart getSource();
	
	public ModelEditPart getTarget();
	
	public void setConnectionLayer(ConnectionLayer cl);
	
	public void attach(ModelEditPart src, ModelEditPart trg);
	
	public void detach();
}
