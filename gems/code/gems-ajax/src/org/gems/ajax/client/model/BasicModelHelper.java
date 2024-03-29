package org.gems.ajax.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.gems.ajax.client.model.event.EventDispatcher;
import org.gems.ajax.client.model.event.InstantiationEvent;
import org.gems.ajax.client.model.event.ModelListener;
import org.gems.ajax.client.model.resources.ModelResource;
import org.gems.ajax.client.util.UUID;

public class BasicModelHelper implements ModelHelper {

	private ModelType modelType_;
	
	public ModelType getModelType() {
		return modelType_;
	}

	public void setModelType(ModelType modelType) {
		modelType_ = modelType;
	}

	public void addChild(Object parent, Object child) {
		((ClientModelObject) parent).addChild((ClientModelObject) child);
	}

	public void addListener(Object o, ModelListener l) {
		((ClientModelObject) o).getListeners().add(l);
	}

	public Type getModelType(Object o) {
		return ((MetaType)getTypes(o)[0]).getModelType();
	}

	public Object clone(Object o) {
		return ((ClientModelObject) o).clone();
	}

	public String getId(Object o) {
		if(o instanceof ClientModelObject)
			return ((ClientModelObject)o).getId();
		else if(o instanceof ClientAssociation){
			return ((ClientAssociation)o).getAssociationId();
		}
		
		return null;
	}

	public Type getTypeForFullName(String fullname) {
		return TypeManager.getOrCreateTypeFromFullName(fullname);
	}

	public Type getModelType(Type objecttype) {
		return ((MetaType)objecttype).getModelType();
	}

	public void connect(Object src, Object trg, Object a) {
		((ClientAssociation) a).setSource((ClientModelObject) src);
		((ClientAssociation) a).setTarget((ClientModelObject) src);
		((ClientModelObject) src).addAssociation((ClientAssociation) a);
		((ClientModelObject) trg).addAssociation((ClientAssociation) a);
	}

	public Object createAssociation(ModelResource res, Type assoctype) {
		ClientAssociation assoc = (ClientAssociation)createInstance(res, assoctype, UUID.get());
		assoc.setType((MetaAssociation) assoctype);
		return assoc;
	}

	public void disconnect(Object src, Object trg, Object a) {
		((ClientModelObject) src).removeAssociation((ClientAssociation) a);
		((ClientModelObject) trg).removeAssociation((ClientAssociation) a);
	}

	public List getChildren(Object model) {
		return ((ClientModelObject) model).getChildren();
	}

	public List getConnections(Object model) {
		return ((ClientModelObject) model).getAssociations();
	}

	public String getLabel(Object model) {
		return "" + model;
	}

	public Object getParent(Object child) {
		return ((ClientModelObject) child).getParent();
	}

	public void removeChild(Object parent, Object child) {
		((ClientModelObject) parent).removeChild((ClientModelObject) child);
	}

	public void removeListener(Object o, ModelListener l) {
		((ClientModelObject) o).getListeners().remove(l);
	}

	public List<String> getTags(Object o) {
		return ((ClientModelObject) o).getTags();
	}

	public Type[] getTypes(Object o) {
		return ((ClientModelObject) o).getTypes().toArray(new Type[0]);
	}

	public Type getTypeForName(String modeltype, String name) {
		return TypeManager.getTypeForName(modelType_.getName(),name);
	}

	public Object getProperty(Object model, String property) {
		return ((ClientModelObject) model).getProperties().get(property);
	}

	public void setProperty(Object model, String property, Object value) {
		((ClientModelObject) model).getProperties().get(property).setValue(
				value);
	}

	public List getPropertyNames(Object model) {
		Set<String> props = ((ClientModelObject) model).getProperties()
				.keySet();
		return new ArrayList<String>(props);
	}

	public boolean canAddChild(Object parent, Object child) {
		
		List<MetaType> pts = ((ClientModelObject) parent).getTypes();
		for (MetaType t : pts) {
			if (t.instanceOf((ClientModelObject) child)) {
				return true;
			}
		}
		return false;
	}

	public Object createInstance(ModelResource res, Object type, String id) {
		//It may be useful to propose InstantiationEvents
		//and see if create a new instance is possible before
		//creating the new instance. At the moment, I think
		//this is probably overkill. It may be useful
		//in the future.
		
		MetaType mtype = ((MetaType)type);
		Object inst = mtype.newInstance();
		if(id != null && inst instanceof ClientModelObject){
			((ClientModelObject)inst).setId(id);
		}
		attachToResource(inst, res);
		InstantiationEvent evt = new InstantiationEvent(mtype.getFullName(), getId(inst));
		EventDispatcher.get().dispatch((ModelElement)inst, evt, null);
		return inst;
	}

	public List getAllowedChildTypes(Object type) {
		MetaType t = (MetaType) type;
		return t.getValidChildTypes();
	}

	public List<Type> getAssociationTypes(Object src, Object trg) {
		List<Type> assoc = new ArrayList<Type>();
		ClientModelObject so = (ClientModelObject) src;
		ClientModelObject to = (ClientModelObject) trg;

		for (MetaType mt : so.getTypes()) {
			for (MetaAssociation ma : mt.getAssociations()) {
				if (ma.getTargetType().instanceOf(to)) {
					assoc.add(ma);
				}
			}
		}

		return assoc;
	}

	public Object getSource(Object assoc) {
		return ((ClientAssociation)assoc).getSource();
	}

	public Object getTarget(Object assoc) {
		return ((ClientAssociation)assoc).getTarget();
	}

	public List<Property> getProperties(Object model) {
		return new ArrayList<Property>(((ClientModelObject)model).getProperties().values());
	}

	public boolean canRemoveChild(Object parent, Object child) {
		return true;
	}

	public String validatePropertyValue(Object model, String property,
			Object value) {
		return null;
	}

	public boolean canConnect(Object src, Object trg, Object assoctype) {
		return assoctype != null && src != null && trg != null;
	}

	public ModelResource getContainingResource(Object o) {
		if(o instanceof ClientModelObject){
			return ((ClientModelObject)o).getModelResource();
		}
		return null;
	}

	public void attachToResource(Object o, ModelResource res){
		if(o instanceof ClientModelObject){
			((ClientModelObject)o).attachToModelResource(res);
			((ClientModelObject)o).register(true);
		}
	}
	
	public Object createInstance(ModelResource res, Object type){
		return createInstance(res, type, null);
	}
}
