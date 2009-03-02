package org.gems.ajax.client.model.event;

import org.gems.ajax.client.model.ModelHelper;

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
public class ModelEventReplay {

	private ModelHelper modelHelper_;
	
	public void replay(ModelResourceEvent mre){
		ModelEvent evt = mre.getEvent();
		
		if(evt.getSource() != null){
			
			if(evt.getType() == ModelEvent.CHILD_ADDED || evt.getType() == ModelEvent.CHILD_REMOVED){
				ContainmentEvent ce = (ContainmentEvent)evt;
				if(ce.getChild() != null){
					Object parent = ce.getParent();
					Object child = ce.getChild();
					if(ce.isAdd() && !modelHelper_.getChildren(parent).contains(child)){
						modelHelper_.addChild(parent, child);
					}
					else if(!ce.isAdd() && modelHelper_.getChildren(parent).contains(child)){
						modelHelper_.removeChild(parent, child);
					}
				}
			}
			else if(evt.getType() == ModelEvent.CONNECTION_ADDED || evt.getType() == ModelEvent.CONNECTION_REMOVED){
//				ConnectionEvent ce = (ConnectionEvent)evt;
//				Object src = ce.getSource();
//				Object trg = ce.getTarget();
//				if(ce.isAdd()){
//					modelHelper_.createAssociation(assoctype)
//					modelHelper_.connect(src, trg, assoc)
//				}
//				else if(!ce.isAdd() && modelHelper_.getChildren(parent).contains(child)){
//					modelHelper_.removeChild(parent, child);
//				}
			}
			else if(evt.getType() == ModelEvent.PROPERTY_CHANGED){
				PropertyEvent pe = (PropertyEvent)evt;
				if(pe.getSource() != null){
					String pname = pe.getPropertyName();
					String nv = pe.getNewValue();
					modelHelper_.setProperty(evt.getSource(), pname, nv);
				}
			}
			else if(evt.getType() == ModelEvent.INSTANTIATION){
				InstantiationEvent ie = (InstantiationEvent)evt;
				Object type = modelHelper_.getTypeForName(ie.getModelType(),ie.getTypeName());
				Object obj = modelHelper_.createInstance(mre.getResource(), type, ie.getElementId());
				modelHelper_.attachToResource(obj, mre.getResource());
			}
		}
	}
}
