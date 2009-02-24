package org.gems.ajax.client.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientAssociation implements IsSerializable{

	private ClientModelObject source_;

	private ClientModelObject target_;
	
	private MetaAssociation type_;

	private String associationId_;
	
	private List<Property> properties_ = new ArrayList<Property>(1);

	protected ClientAssociation(){}
	
	public ClientAssociation(String associationId, ClientModelObject source,
			ClientModelObject target) {
		super();
		associationId_ = associationId;
		source_ = source;
		target_ = target;
	}

	public ClientModelObject getSource() {
		return source_;
	}

	public void setSource(ClientModelObject source) {
		source_ = source;
	}

	public ClientModelObject getTarget() {
		return target_;
	}

	public void setTarget(ClientModelObject target) {
		target_ = target;
	}

	public String getAssociationId() {
		return associationId_;
	}

	public void setAssociationId(String associationId) {
		associationId_ = associationId;
	}

	public MetaAssociation getType() {
		return type_;
	}

	public void setType(MetaAssociation type) {
		type_ = type;
	}

	public List<Property> getProperties() {
		return properties_;
	}

	public void setProperties(List<Property> properties) {
		properties_ = properties;
	}

	public String toString(){
		return source_.getId()+"-->"+target_.getId();
	}
	
	public void remove(){
		if(source_ != null)
			source_.getAssociations().remove(this);
		if(target_ != null)
			target_.getAssociations().remove(this);
	}
	
	public void add(){
		if(!source_.getAssociations().contains(this))
			source_.getAssociations().add(this);
		if(!target_.getAssociations().contains(this))
			target_.getAssociations().add(this);
	}
}
