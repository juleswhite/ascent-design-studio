package org.gems.ajax.client.model;


public class MetaAssociation extends MetaType {

	public static final String META_ASSOCIATION_ID = "MetaAssociation";
	private MetaType sourceType_;
	private MetaType targetType_;
	private String name_;

	protected MetaAssociation(){}
	
	public MetaAssociation(ModelType mt, String name, MetaType sourceType, MetaType targetType) {
		super(mt,name);
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
	
	protected ClientModelObject createBlankTypeInstance(){
		return new ClientAssociation();
	}
	
	public String getFullName(){
		return META_ASSOCIATION_ID+NAME_PART_SEPARATOR+getModelType()+NAME_PART_SEPARATOR+getName()+NAME_PART_SEPARATOR+getSourceType().getFullName()+NAME_PART_SEPARATOR+getTargetType().getFullName();
	}
}
