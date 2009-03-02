package org.gems.ajax.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gems.ajax.client.model.event.ConnectionEvent;
import org.gems.ajax.client.model.event.ContainmentEvent;
import org.gems.ajax.client.model.event.EventDispatcher;
import org.gems.ajax.client.model.event.ModelEvent;
import org.gems.ajax.client.model.event.ModelListener;
import org.gems.ajax.client.model.event.PropertyEvent;
import org.gems.ajax.client.model.event.ProposedChangeListener;
import org.gems.ajax.client.model.event.ProposedConnectionEvent;
import org.gems.ajax.client.model.event.ProposedContainmentEvent;
import org.gems.ajax.client.model.event.ProposedEvent;
import org.gems.ajax.client.model.resources.ModelResource;
import org.gems.ajax.client.util.UUID;

public class ClientModelObject implements Serializable, ModelElement {

	private String id_;

	private String label_ = "[New Element]";

	private List<String> tags_ = new ArrayList<String>(1);

	private ClientModelObject parent_;

	private List<MetaType> types_ = new ArrayList<MetaType>(1);

	private List<ModelListener> listeners_ = new ArrayList<ModelListener>(1);

	private List<ClientModelObject> children_ = new ArrayList<ClientModelObject>(
			1);

	private List<ClientAssociation> associations_ = new ArrayList<ClientAssociation>(
			1);

	private Map<String, Property> properties_ = new HashMap<String, Property>(3);

	private ModelResource modelResource_;

	public ClientModelObject() {
		id_ = UUID.get();
		register(true);
	}

	public ClientModelObject(String id, String label) {
		id_ = id;
		label_ = label;
		register(true);
	}

	public ClientModelObject(String id, String label, MetaType mt) {
		this(id, label);
		getTypes().add(mt);
	}

	public ClientModelObject(String label, MetaType mt) {
		this(UUID.get(), label);
		getTypes().add(mt);
	}

	public String getId() {
		return id_;
	}

	public void setId(String id) {
		unRegister(false);
		id_ = id;
		register(false);
	}

	public List<ClientModelObject> getChildren() {
		return children_;
	}

	public List<ClientAssociation> getAssociations() {
		return associations_;
	}

	public void addChild(ClientModelObject child) {
		if (dispatch(new ProposedContainmentEvent(this, child, true))) {
			getChildren().add(child);
			child.setParent(this);
			dispatch(new ContainmentEvent(this, child, true));
		}
	}

	public void removeChild(ClientModelObject child) {
		if (dispatch(new ProposedContainmentEvent(this, child, false))) {
			getChildren().remove(child);
			child.setParent(null);
			dispatch(new ContainmentEvent(this, child, false));
		}
	}

	public void addAssociation(ClientAssociation a) {
		if (dispatch(new ProposedConnectionEvent(a.getSource(), a.getTarget(),a,
				true))) {
			getAssociations().add(a);
			dispatch(new ConnectionEvent(a.getSource(), a.getTarget(), a, true));
		}
	}

	public void removeAssociation(ClientAssociation a) {
		if (dispatch(new ProposedConnectionEvent(a.getSource(), a.getTarget(), a,
				false))) {
			getAssociations().remove(a);
			dispatch(new ConnectionEvent(a.getSource(), a.getTarget(), a, false));
		}
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

	public void addModelListener(ModelListener l) {
		listeners_.add(l);
	}

	public void removeModelListener(ModelListener l) {
		listeners_.remove(l);
	}

	public void addProposedChangeListener(ProposedChangeListener l) {
		listeners_.add(l);
	}

	public void removeProposedChangeListener(ProposedChangeListener l) {
		listeners_.remove(l);
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

	public void addTag(String tag) {
		if (!getTags().contains(tag))
			getTags().add(tag);
	}

	public void removeTag(String tag) {
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

	public void attachProperty(Property prop) {
		properties_.put(prop.getName(), prop);
		prop.setOwner(this);
	}

	public void detatchProperty(Property prop) {
		properties_.remove(prop.getName());
		prop.setOwner(null);
	}

	public String getLabel() {
		return label_;
	}

	public void setLabel(String label) {
		label_ = label;
	}

	public ModelResource getModelResource() {
		return modelResource_;
	}

	public void attachToModelResource(ModelResource modelResource) {
		modelResource_ = modelResource;
		for (ClientModelObject child : children_)
			child.attachToModelResource(modelResource);
	}

	protected boolean propertyChange(PropertyEvent pe) {
		return dispatch(pe);
	}

	protected boolean dispatch(ModelEvent evt) {
		EventDispatcher.get().dispatch(this, evt, listeners_);

		return !(evt instanceof ProposedEvent)
				|| !((ProposedEvent) evt).vetoed();
	}

	public void register(boolean childrentoo) {
		ModelRegistry.getInstance().add(this);

		if (childrentoo) {
			for (ClientModelObject child : getChildren()) {
				child.register(true);
			}
		}
	}

	public void unRegister(boolean childrentoo) {
		ModelRegistry.getInstance().add(this);
		if (childrentoo) {
			for (ClientModelObject child : getChildren()) {
				child.unRegister(true);
			}
		}
	}
}
