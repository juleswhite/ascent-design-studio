package org.gems.ajax.client.model;

import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.util.UUID;

public class MetaType extends Type {

	private MetaType parentType_;
	
	private ModelType modelType_;

	private List<MetaType> validChildTypes_ = new ArrayList<MetaType>();

	private List<MetaAssociation> associations_ = new ArrayList<MetaAssociation>();
	
	private List<MetaProperty> properties_ = new ArrayList<MetaProperty>();

	protected MetaType() {
		super(null);
	}

	public MetaType(ModelType mt, String name) {
		this(mt,name,null);
	}

	public MetaType(String modeltype, String name) {
		this(TypeManager.getModelTypeForName(modeltype),name,null);
	}
	
	public MetaType(ModelType mt, String name, MetaType parent) {
		super(name);
		modelType_ = mt;
		parentType_ = parent;
	}
	
	public MetaType getParentType() {
		return parentType_;
	}

	public void setParentType(MetaType parentType) {
		parentType_ = parentType;
	}

	public boolean sameType(MetaType mt) {
		return mt.getName().equals(getName());
	}

	public boolean isAssignableFrom(MetaType mt) {
		if (sameType(mt))
			return true;

		while (mt.getParentType() != null) {
			mt = mt.getParentType();
			if (sameType(mt))
				return true;
		}

		return false;
	}

	public boolean isValidChildType(MetaType mt) {
		for (MetaType t : validChildTypes_) {
			if (t.isAssignableFrom(mt))
				return true;
		}
		if (getParentType() != null)
			return getParentType().isValidChildType(mt);

		return false;
	}

	public List<MetaType> getValidChildTypes() {
		return validChildTypes_;
	}

	public void setValidChildTypes(List<MetaType> validChildTypes) {
		validChildTypes_ = validChildTypes;
	}

	public List<MetaAssociation> getAssociations() {
		return associations_;
	}

	public void setAssociations(List<MetaAssociation> associations) {
		associations_ = associations;
	}
	
	

	public List<MetaProperty> getProperties() {
		return properties_;
	}
	
	public List<MetaProperty> getAllProperties(){
		List<MetaProperty> props = new ArrayList<MetaProperty>();
		collectProperties(props);
		return props;
	}
	
	protected void collectProperties(List<MetaProperty> props){
		props.addAll(getProperties());
		if(parentType_ != null){props.addAll(parentType_.getAllProperties());};
	}

	public void setProperties(List<MetaProperty> properties) {
		properties_ = properties;
	}

	public boolean instanceOf(ClientModelObject o) {
		for (MetaType mt : o.getTypes()) {
			if (isAssignableFrom(mt))
				return true;
		}
		return false;
	}
	
	public ClientModelObject newInstance(){
		ClientModelObject o = new ClientModelObject();
		o.getTypes().add(this);
		for(MetaProperty p : getAllProperties()){
			Property prop = p.newInstance();
			o.getProperties().put(prop.getName(),prop);
		}
		return o;
	}

	public ModelType getModelType() {
		return modelType_;
	}

	public void setModelType(ModelType modelType) {
		modelType_ = modelType;
	}
	
	
}
