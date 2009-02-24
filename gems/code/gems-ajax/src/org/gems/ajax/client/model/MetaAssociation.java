package org.gems.ajax.client.model;

import java.io.Serializable;

public class MetaAssociation implements Serializable {

	private MetaType sourceType_;
	private MetaType targetType_;
	private String name_;

	protected MetaAssociation(){}
	
	public MetaAssociation(String name, MetaType sourceType, MetaType targetType) {
		super();
		name_ = name;
		sourceType_ = sourceType;
		targetType_ = targetType;
	}

	public String getName() {
		return name_;
	}

	public void setName(String name) {
		name_ = name;
	}

	public MetaType getSourceType() {
		return sourceType_;
	}

	public void setSourceType(MetaType sourceType) {
		sourceType_ = sourceType;
	}

	public MetaType getTargetType() {
		return targetType_;
	}

	public void setTargetType(MetaType targetType) {
		targetType_ = targetType;
	}

	public String toString(){
		return "MetaAssociation("+name_+","+sourceType_.getName()+"->"+targetType_.getName()+")";
	}
}
