package org.gems.ajax.client.model.event;

import org.gems.ajax.client.model.ModelElement;

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
public class ProposedPropertyEvent extends PropertyEvent implements ProposedEvent{

	public ProposedPropertyEvent(ModelElement source, String propertyname,
			String oldvalue, String newvalue) {
		super(source, propertyname, oldvalue, newvalue);
	}
	
	public void dispatchImpl(ModelListener l) {
		if(l instanceof ProposedChangeListener){
			ProposedChangeListener pcl = (ProposedChangeListener)l;
			pcl.aboutToChangeProperty(this);
		}
	}
}
