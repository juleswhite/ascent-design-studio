package org.gems.ajax.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gems.ajax.client.util.UUID;

public class ClientModelObject implements Serializable{

	private String id_;
	
	private String label_ = "[New Element]";
	
	private List<String> tags_ = new ArrayList<String>();

	private ClientModelObject parent_;
	
	private List<MetaType> types_ = new ArrayList<MetaType>();

	private List<ModelListener> listeners_ = new ArrayList<ModelListener>();

	private List<ClientModelObject> children_ = new ArrayList<ClientModelObject>();

	private List<ClientAssociation> associations_ = new ArrayList<ClientAssociation>();

	private Map<String,Property> properties_ = new HashMap<String,Property>();
	
	public ClientModelObject(){
		id_ = UUID.get();
		ModelRegistry.getInstance().add(this);
	}

	public ClientModelObject(String id, String label) {
		id_ = id;
		label_ = label;
		ModelRegistry.getInstance().add(this);
	}
	
	public ClientModelObject(String id, String label, MetaType mt) {
		this(id,label);
		getTypes().add(mt);
	}
	
	public ClientModelObject(String label, MetaType mt) {
		this(UUID.get(),label);
		getTypes().add(mt);
	}

	public String getId() {
		return id_;
	}

	public void setId(String id) {
		ModelRegistry.getInstance().remove(this);
		id_ = id;
		ModelRegistry.getInstance().add(this);
	}

	public List<ClientModelObject> getChildren() {
		return children_;
	}

	public List<ClientAssociation> getAssociations() {
		return associations_;
	}

	public void addChild(ClientModelObject child) {
		getChildren().add(child);
		child.setParent(this);

	}

	public void removeChild(ClientModelObject child) {
		getChildren().remove(child);
		child.setParent(null);

	}

	public void addAssociation(ClientAssociation a) {
		getAssociations().add(a);
	}

	public void removeAssociation(ClientAssociation a) {
		getAssociations().remove(a);
	}

	public ClientModelObject getParent() {
		return parent_;
	}

	public void setParent(ClientModelObject parent) {
		parent_ = parent;
	}

	public List<ModelListener> getListeners() {
		return listeners_;
	}

	public void setListeners(List<ModelListener> listeners) {
		listeners_ = listeners;
	}

	public ClientModelObject clone() {
		return null;
	}

	public String toString() {
		return getLabel();
	}

	public List<String> getTags() {
		return tags_;
	}

	public void addTag(String tag){
		if(!getTags().contains(tag))
			getTags().add(tag);
	}
	
	public void removeTag(String tag){
		getTags().remove(tag);
	}

	public List<MetaType> getTypes() {
		return types_;
	}

	public void setTypes(List<MetaType> types) {
		types_ = types;
	}

	public Map<String, Property> getProperties() {
		return properties_;
	}

	public void setProperties(Map<String, Property> properties) {
		properties_ = properties;
	}

	public String getLabel() {
		return label_;
	}

	public void setLabel(String label) {
		label_ = label;
	}
	
}
