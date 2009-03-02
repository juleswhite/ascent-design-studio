package org.gems.ajax.client.model;


public class ClientAssociation extends ClientModelObject {

	private ClientModelObject source_;

	private ClientModelObject target_;
	
	private String associationId_;
	
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
		return (MetaAssociation)getTypes().get(0);
	}

	public void setType(MetaAssociation type) {
		getTypes().clear();
		getTypes().add(type);
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
