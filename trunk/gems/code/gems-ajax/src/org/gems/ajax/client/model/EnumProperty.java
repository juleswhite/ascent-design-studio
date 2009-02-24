package org.gems.ajax.client.model;

import java.util.List;

public class EnumProperty extends Property {

	private List<String> allowedValues_;
	
	public EnumProperty(){
	}
	
	public EnumProperty(String name, Object value, List<String> avals) {
		super(name, ENUM, value);
		allowedValues_ = avals;
	}

	public List<String> getAllowedValues() {
		return allowedValues_;
	}

	public void setAllowedValues(List<String> allowedValues) {
		allowedValues_ = allowedValues;
	}
	
	
}
