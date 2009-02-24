package org.gems.ajax.client.model;

public class LockingModelHelper extends DelegatingModelHelper {
	private boolean locked_ = false;

	public LockingModelHelper(ModelHelper delegate) {
		super(delegate);
	}

	protected boolean doPreDelegation() {
		return !locked_;
	}
	
	public void setLocked(boolean locked){
		locked_ = locked;
	}
	
	public boolean isLocked(){
		return locked_;
	}
}
