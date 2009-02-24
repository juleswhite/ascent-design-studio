/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.gems.ajax.client.edit.cmd;

import java.util.List;

import org.gems.ajax.client.edit.ConnectionEditPart;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.EditPartFactory;
import org.gems.ajax.client.edit.ModelEditPart;
import org.gems.ajax.client.model.ModelHelper;

public class ConnectCommand extends AbstractConnectionCommand  {

	private ConnectionEditPart connectionEditPart_;
	private EditPartFactory factory_;
	private ModelHelper modelHelper_;
	private String associationType_;
	private Object associationHandle_;
	private ModelEditPart source_;
	private ModelEditPart target_;

	public ConnectCommand(ModelHelper modelHelper, EditPartFactory fact) {
		super();
		modelHelper_ = modelHelper;
		factory_ = fact;
	}

	public ConnectCommand(ModelHelper modelHelper, EditPartFactory fact,
			String associationType) {
		super();
		factory_ = fact;
		modelHelper_ = modelHelper;
		associationType_ = associationType;
	}

	public boolean canExecute() {
		return source_ != null && target_ != null
				&& getAssociationType() != null;
	}

	public boolean canUndo() {
		return true;
	}

	public void execute() {
		if (associationHandle_ == null) {
			associationHandle_ = modelHelper_
					.createAssociation(associationType_);
			connectionEditPart_ = factory_
					.createConnectionEditPart(source_.getView(),associationHandle_);
			connectionEditPart_.setConnectionLayer(source_.getModelFigure()
					.getDiagram().getConnectionLayer());
		}

		connectionEditPart_.attach(source_, target_);
	}

	public String getName() {
		return "Connect";
	}

	public void redo() {
		execute();
	}

	public void undo() {
		connectionEditPart_.detach();
	}

	public ModelEditPart getSource() {
		return source_;
	}

	public ModelEditPart getTarget() {
		return target_;
	}

	public void setSource(ModelEditPart source) {
		source_ = source;
	}

	public void setTarget(EditPart target) {
		if(target instanceof ModelEditPart){
			target_ = (ModelEditPart)target;
		}
	}

	public Object getAssociationType() {
		Object stype = null;
		if (associationType_ != null) {
			stype = associationType_;
		}
		if (stype == null && target_ != null && source_ != null) {
			List valid = source_.getModelHelper().getAssociationTypes(source_.getModel(), target_.getModel());
			if (valid != null && valid.size() > 0)
				stype = valid.get(0);
		}
		return stype;
	}
}
