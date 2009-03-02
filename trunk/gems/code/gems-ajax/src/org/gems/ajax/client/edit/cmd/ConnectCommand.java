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
import org.gems.ajax.client.model.ModelElement;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.Type;
import org.gems.ajax.client.model.event.ProposedConnectionEvent;

public class ConnectCommand extends AbstractConnectionCommand {

	private boolean fixedType_ = false;
	private ConnectionEditPart connectionEditPart_;
	private EditPartFactory factory_;
	private ModelHelper modelHelper_;
	private Type associationType_;
	private Object associationHandle_;
	private ModelEditPart source_;
	private ModelEditPart target_;

	public ConnectCommand(ModelHelper modelHelper, EditPartFactory fact) {
		super();
		modelHelper_ = modelHelper;
		factory_ = fact;
	}

	public ConnectCommand(ModelHelper modelHelper, EditPartFactory fact,
			Type associationType) {
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
		if (associationHandle_ == null && associationType_ != null) {
			if (associationType_ != null
					&& modelHelper_.getAssociationTypes(source_.getModel(), target_.getModel())
							.contains(associationType_)) {

				// This should be fixed so that the event
				// gets sent out and checked ....
				ProposedConnectionEvent pe = new ProposedConnectionEvent(
						(ModelElement) source_.getModel(),
						(ModelElement) target_.getModel(), null, true);

				if (!pe.vetoed()) {
					associationHandle_ = modelHelper_.createAssociation(
							modelHelper_.getContainingResource(source_
									.getModel()), associationType_);
					connectionEditPart_ = factory_.createConnectionEditPart(
							source_.getView(), associationHandle_);
					connectionEditPart_
							.setConnectionLayer(source_.getModelFigure()
									.getDiagram().getConnectionLayer());
				}
			}
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
		if (target instanceof ModelEditPart) {
			target_ = (ModelEditPart) target;
			
			if(target_ != null && source_ != null){
				ModelHelper mh = target_.getModelHelper();
				List<Type> tt = mh.getAssociationTypes(getSource().getModel(), getTarget().getModel());
				updateAssociationTypes(tt);
			}
		}
	}

	public Object getAssociationType() {
		Object stype = null;
		if (associationType_ != null) {
			stype = associationType_;
		}
		if (stype == null && target_ != null && source_ != null) {
			List valid = source_.getModelHelper().getAssociationTypes(
					source_.getModel(), target_.getModel());
			if (valid != null && valid.size() > 0)
				stype = valid.get(0);
		}
		return stype;
	}

	public void updateAssociationTypes(List<Type> tt) {
		if (!fixedType_) {
			if (tt != null && tt.size() > 0) {
				associationType_ = tt.get(0);
			} else {
				associationType_ = null;
			}
		}
	}

	public boolean isFixedType() {
		return fixedType_;
	}

	public void setFixedType(boolean fixedType) {
		fixedType_ = fixedType;
	}

}
