package org.gems.ajax.server.model.emf;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.gems.ajax.client.model.ClientAssociation;
import org.gems.ajax.client.model.ClientModelObject;
import org.gems.ajax.client.model.MetaAssociation;
import org.gems.ajax.client.model.MetaProperty;
import org.gems.ajax.client.model.MetaType;
import org.gems.ajax.client.model.ModelLoader;
import org.gems.ajax.client.model.Property;
import org.gems.ajax.server.model.Model;
import org.gems.ajax.server.model.ModelReader;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class EMFModelLoader implements ModelReader {

	private boolean autoMapName_ = true;
	private boolean autoMapId_ = true;

	public Model loadModel(String id) {
		File ecore = new File("test/data/emf/Metamodel.ecore");
		File modelf = new File("test/data/emf/featuremodeling.gemsmeta2");
		URI ecoreuri = URI.createFileURI(ecore.getAbsolutePath());
		URI modeluri = URI.createFileURI(modelf.getAbsolutePath());

		try {
			EPackage epkg = loadEcore(ecoreuri);
			EObject eobj = loadEMFModel(modeluri, epkg);
			ClientModelObject cmo = toClientObject(eobj,
					new HashMap<Object, ClientModelObject>(),
					new HashMap<Object, MetaType>());

			Model model = new Model();
			model.setRoot(cmo);
			model.setId(id);
			
			return model;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static EObject loadEMFModel(URI uri, EPackage epkg)
			throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(epkg.getNsURI(), epkg);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(uri.fileExtension(), new XMIResourceFactoryImpl());
		Resource resource = resourceSet.createResource(uri);
		resource.load(null);
		return resource.getContents().get(0);
	}

	public static EPackage loadEcore(URI uri) throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("ecore", new EcoreResourceFactoryImpl());
		Resource resource = resourceSet.createResource(uri);
		resource.load(new HashMap());
		return (EPackage) resource.getContents().get(0);
	}

	public ClientModelObject toClientObject(EObject eobj,
			Map<Object, ClientModelObject> lookup,
			Map<Object, MetaType> metalookup) {
		ClientModelObject cmo = lookup.get(eobj);
		if (cmo == null) {
			cmo = new ClientModelObject();
			lookup.put(eobj, cmo);

			// This should eventually be changed to create a
			// MetaType for each super type of the EClass that
			// the eobj inherits from!!!
			EClass eclass = eobj.eClass();
			MetaType mt = toClientMetaType(eclass, metalookup);
			cmo.getTypes().add(mt);

			for (EAttribute attr : eclass.getEAllAttributes()) {
				Property prop = new Property(attr.getName(),
						getPropertyType(attr.getEType()), eobj.eGet(attr));
				if (autoMapName_ && prop.getName().equalsIgnoreCase("name")) {
					cmo.setLabel("" + prop.getValue());
				}
				if (autoMapId_ && prop.getName().equalsIgnoreCase("id")) {
					cmo.setId("" + prop.getValue());
				}
				cmo.getProperties().put(prop.getName(), prop);
			}
			for (EReference ref : eclass.getEReferences()) {
				if (!ref.isContainment()) {
					String typename = ref.getName();
					ClientModelObject src = cmo;
					Object val = eobj.eGet(ref);
					if (val instanceof EList) {
						EList list = (EList) val;
						for (Object etrg : list) {
							addAssociation(typename, cmo, (EObject) etrg, lookup,
									metalookup);
						}
					} else {
						addAssociation(typename, cmo, (EObject) val, lookup, metalookup);
					}
				}
			}
			// Should this be calling getEAllContainments rather
			// than querying for a list of contained types that is
			// scoped to the specific EClass that is being mirrored?
			for (EReference contain : eclass.getEAllContainments()) {
				Object val = eobj.eGet(contain);
				
				if (val instanceof EObject) {
					ClientModelObject child = toClientObject((EObject) eobj
							.eGet(contain), lookup, metalookup);
					cmo.addChild(child);
				} else if (val instanceof EList) {
					EList list = (EList) val;
					for (Object echild : list) {
						ClientModelObject child = toClientObject(
								(EObject) echild, lookup, metalookup);
						cmo.addChild(child);
					}
				}
			}
		}
		return cmo;
	}

	public void addAssociation(String name, ClientModelObject cmo, EObject etrg,
			Map<Object, ClientModelObject> lookup,
			Map<Object, MetaType> metalookup) {
		ClientModelObject trg = toClientObject(etrg, lookup, metalookup);
		MetaAssociation type = new MetaAssociation(name, cmo.getTypes().get(0), trg.getTypes().get(0));
		ClientAssociation assoc = new ClientAssociation(UUID.randomUUID()
				.toString(), cmo, trg);
		assoc.setType(type);
		cmo.addAssociation(assoc);
	}

	public MetaType toClientMetaType(EClass eclass, Map<Object, MetaType> lookup) {
		MetaType mt = lookup.get(eclass);
		if (mt == null) {
			mt = new MetaType(eclass.getEPackage().getName(), eclass.getName());
			lookup.put(eclass, mt);

			for (EClass sup : eclass.getEAllSuperTypes()) {
				mt.setParentType(toClientMetaType(sup, lookup));
			}
			for (EAttribute attr : eclass.getEAttributes()) {
				MetaProperty prop = new MetaProperty(attr.getName(),
						getPropertyType(attr.getEType()));
				mt.getProperties().add(prop);
			}
			for (EReference ref : eclass.getEReferences()) {
				if (!ref.isContainment()) {
					String name = ref.getName();
					MetaType src = mt;
					MetaType trg = toClientMetaType(ref.getEReferenceType(),
							lookup);
					MetaAssociation assoc = new MetaAssociation(name, src, trg);
					src.getAssociations().add(assoc);
				}
			}
			// Should this be calling getEAllContainments rather
			// than querying for a list of contained types that is
			// scoped to the specific EClass that is being mirrored?
			for (EReference contain : eclass.getEAllContainments()) {
				MetaType childtype = toClientMetaType(contain
						.getEReferenceType(), lookup);
				mt.getValidChildTypes().add(childtype);
			}
		}
		return mt;
	}

	public String getPropertyType(EClassifier c) {
		if (c.getName().contains("Int"))
			return MetaProperty.INT;
		else if (c.getName().contains("String"))
			return MetaProperty.STRING;
		else if (c.getName().contains("Double"))
			return MetaProperty.DECIMAL;
		else if (c.getName().contains("Boolean"))
			return MetaProperty.BOOLEAN;

		return MetaProperty.STRING;
	}

	public boolean isAutoMapName() {
		return autoMapName_;
	}

	public void setAutoMapName(boolean autoMapName) {
		autoMapName_ = autoMapName;
	}

	public boolean isAutoMapId() {
		return autoMapId_;
	}

	public void setAutoMapId(boolean autoMapId) {
		autoMapId_ = autoMapId;
	}

	// public static EClass findRootType(EPackage pkg){
	// EClass root = null;
	//		
	// List<EClass> potentialRoots = new ArrayList<EClass>();
	// for(EClassifier ec : pkg.getEClassifiers()){
	// if(ec instanceof EClass){
	// EClass eclass = (EClass)ec;
	// potentialRoots.add(eclass);
	// }
	// }
	//		
	// return ;
	// }
}
