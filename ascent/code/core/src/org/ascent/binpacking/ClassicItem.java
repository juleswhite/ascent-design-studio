package org.ascent;

public class ClassicItem extends Element {
    double [] consumedResources_ ;
    
	public ClassicItem(int id, String name) {
		super(id, name);
	}
	
	public ClassicItem(int id, String name, double [] cr) {
		super(id, name);
		consumedResources_ = cr;
	}
	public double [] getConsumedResources_(){
		return consumedResources_;
	}

}
