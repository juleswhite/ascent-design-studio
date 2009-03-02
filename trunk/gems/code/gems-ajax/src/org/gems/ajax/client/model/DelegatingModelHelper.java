package org.gems.ajax.client.model;

import java.util.List;

import org.gems.ajax.client.model.event.ModelListener;
import org.gems.ajax.client.model.resources.ModelResource;

public class DelegatingModelHelper implements ModelHelper {

	private ModelHelper delegate_;

	public DelegatingModelHelper(ModelHelper delegate) {
		super();
		delegate_ = delegate;
	}

	public ModelHelper getDelegate() {
		return delegate_;
	}

	public void setDelegate(ModelHelper delegate) {
		delegate_ = delegate;
	}

	public void addChild(Object parent, Object child) {
		if (doPreDelegation()) {
			delegate_.addChild(parent, child);
			doPostDelegation();
		}
	}

	public Type getModelType(Type objecttype) {
		return delegate_.getModelType(objecttype);
	}

	public void addListener(Object o, ModelListener l) {
		if (doPreDelegation()) {
			delegate_.addListener(o, l);
			doPostDelegation();
		}
	}

	public Type getTypeForFullName(String fullname) {
		return delegate_.getTypeForFullName(fullname);
	}

	public boolean canAddChild(Object parent, Object child) {
		return delegate_.canAddChild(parent, child);
	}

	public boolean canConnect(Object src, Object trg, Object assoctype) {
		return delegate_.canConnect(src, trg, assoctype);
	}

	public boolean canRemoveChild(Object parent, Object child) {
		return delegate_.canRemoveChild(parent, child);
	}

	public Object clone(Object o) {
		Object co = null;
		if (doPreDelegation()) {
			co = delegate_.clone(o);
			doPostDelegation();
		}
		return co;
	}

	public void connect(Object src, Object trg, Object assoc) {
		if (doPreDelegation()) {
			delegate_.connect(src, trg, assoc);
			doPostDelegation();
		}
	}

	public Object createAssociation(ModelResource res, Type assoctype) {
		Object a = null;
		if (doPreDelegation()) {
			a = delegate_.createAssociation(res, assoctype);
			doPostDelegation();
		}
		return a;
	}

	public Object createInstance(ModelResource res, Object type) {
		Object i = null;
		if (doPreDelegation()) {
			i = delegate_.createInstance(res,type);
			doPostDelegation();
		}
		return i;
	}
	
	public Type getModelType(Object o) {
		return delegate_.getModelType(o);
	}

	public Object createInstance(ModelResource res, Object type, String id) {
		Object i = null;
		if (doPreDelegation()) {
			i = delegate_.createInstance(res,type, id);
			doPostDelegation();
		}
		return i;
	}

	public void disconnect(Object src, Object trg, Object assochandle) {
		if (doPreDelegation()) {
			delegate_.disconnect(src, trg, assochandle);
			doPostDelegation();
		}
	}

	public List<Type> getAssociationTypes(Object src, Object trg) {
		return delegate_.getAssociationTypes(src, trg);
	}

	public List getChildren(Object model) {
		return delegate_.getChildren(model);
	}

	public List getConnections(Object model) {
		return delegate_.getConnections(model);
	}

	public String getLabel(Object model) {
		return delegate_.getLabel(model);
	}

	public Object getParent(Object child) {
		return delegate_.getParent(child);
	}

	public Object getProperty(Object model, String property) {
		return delegate_.getProperty(model, property);
	}



	public Object getSource(Object assoc) {
		return delegate_.getSource(assoc);
	}

	public Object getTarget(Object assoc) {
		return delegate_.getTarget(assoc);
	}

	public List<Property> getProperties(Object model) {
		return delegate_.getProperties(model);
	}

	public List<String> getTags(Object o) {
		return delegate_.getTags(o);
	}

	public Type getTypeForName(String mtype, String name) {
		return delegate_.getTypeForName(mtype, name);
	}

	public Type[] getTypes(Object o) {
		return delegate_.getTypes(o);
	}

	public void removeChild(Object parent, Object child) {
		if (doPreDelegation()) {
			delegate_.removeChild(parent, child);
			doPostDelegation();
		}
	}

	public void removeListener(Object o, ModelListener l) {
		if (doPreDelegation()) {
			delegate_.removeListener(o, l);
			doPostDelegation();
		}
	}

	public void setProperty(Object model, String property, Object value) {
		if (doPreDelegation()) {
			delegate_.setProperty(model, property, value);
			doPostDelegation();
		}
	}

	public String validatePropertyValue(Object model, String property,
			Object value) {
		return delegate_.validatePropertyValue(model, property, value);
	}

	public List getAllowedChildTypes(Object type) {
		return delegate_.getAllowedChildTypes(type);
	}

	protected boolean doPreDelegation() {
		return true;
	}

	protected boolean doPostDelegation() {
		return true;
	}

	public String getId(Object o) {
		return delegate_.getId(o);
	}
	
	
	public ModelResource getContainingResource(Object o){
		return delegate_.getContainingResource(o);
	}
	
	public void attachToResource(Object o, ModelResource res){
		delegate_.attachToResource(o, res);
	}
}
