package org.gems.ajax.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gems.ajax.client.model.ClientAssociation;
import org.gems.ajax.client.model.ClientModelObject;
import org.gems.ajax.client.model.Property;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public abstract class AbstractAssociationClassPostProcessor implements
		ModelPostprocessor {
	
	private class Mod {
		
		public void exec(){}
	}

	private class AssocMod extends Mod{
		private ClientAssociation mod_;

		public AssocMod(ClientAssociation toRemove) {
			super();
			mod_ = toRemove;
		}

		public void exec() {
			exec(mod_);
		}

		public void exec(ClientAssociation mod) {
		}
	}

	private class Addition extends AssocMod {

		public Addition(ClientAssociation toRemove) {
			super(toRemove);
		}

		@Override
		public void exec(ClientAssociation mod) {
			mod.add();
		}

	}

	private class Removal extends AssocMod {

		public Removal(ClientAssociation toRemove) {
			super(toRemove);
		}

		public void exec(ClientAssociation mod) {
			mod.remove();
		}
	}
	
	private class Delete extends Mod {
		
		private ClientModelObject toDelete_;
		
		public Delete(ClientModelObject td){
			toDelete_ = td;
		}
		public void exec(){
			toDelete_.getParent().removeChild(toDelete_);
		}
	}

	public Model process(Model cmo) {
		List<Mod> mods = new ArrayList<Mod>();

		traverse(cmo.getRoot(), new HashMap<ClientModelObject, Boolean>(), mods);
		try {
			for (Mod r : mods)
				r.exec();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cmo;
	}

	public void traverse(ClientModelObject cmo,
			Map<ClientModelObject, Boolean> traversed, List<Mod> mods) {
		if (traversed.get(cmo) == null) {
			traversed.put(cmo, true);

			if (isAssociationClass(cmo)) {
				convertToAssociationClass(cmo, mods);
			} else {
				for (ClientModelObject child : cmo.getChildren())
					traverse(child, traversed, mods);
				for (ClientAssociation assoc : cmo.getAssociations()) {
					if (assoc.getSource() == cmo)
						traverse(assoc.getTarget(), traversed, mods);
				}
			}
		}
	}

	public void convertToAssociationClass(ClientModelObject cmo,
			List<Mod> mods) {
		ClientModelObject src = getSource(cmo);
		ClientModelObject trg = getTarget(cmo);

		for(ClientAssociation a : src.getAssociations()){
			if(a.getSource() == cmo || a.getTarget() == cmo)
				mods.add(new Removal(a));
		}
		for(ClientAssociation a : trg.getAssociations()){
			if(a.getSource() == cmo || a.getTarget() == cmo)
				mods.add(new Removal(a));
		}
		
		for(ClientAssociation a : cmo.getAssociations())
			mods.add(new Removal(a));

		ClientAssociation assoc = new ClientAssociation(cmo.getId(), src, trg);
		// src.getAssociations().add(assoc);
		mods.add(new Addition(assoc));

		mods.add(new Delete(cmo));

		for (Property p : cmo.getProperties().values())
			assoc.getProperties().add(p);

	}

	public abstract ClientModelObject getSource(ClientModelObject assoc);

	public abstract ClientModelObject getTarget(ClientModelObject assoc);

	public abstract boolean isAssociationClass(ClientModelObject cmo);

}
