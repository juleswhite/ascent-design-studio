package org.ascent;

import java.util.ArrayList;
import java.util.List;

public class ClassicBin {
	private double [] spaceLeft_;
	private String binName_;
	private int binId_;
	private List<ClassicItem> items = new ArrayList();
	
public ClassicBin(){
		
	}
	
	public ClassicBin(int numCR, int binId){
		spaceLeft_ = new double [numCR];
		initializeSpaceLeft_();
		binId_ = binId;
		
	}
	public ClassicBin(String bn, int binId, double [] sl){
		binName_ = bn;
		//initializeSpaceLeft_();
		spaceLeft_ = sl;
		
		binId_ = binId;
		
	}
	
	private void initializeSpaceLeft_(){
		for( int i = 0; i < spaceLeft_.length; i++){
			spaceLeft_[i] = 0;
		}
	}
	public double[] getSpaceLeft_() {
		return spaceLeft_;
	}

	public void setSpaceLeft_(double[] spaceLeft) {
		this.spaceLeft_ = spaceLeft;
	}
	
	public void addItem(ClassicItem item){
		items.add(item);
	}
	public String getBinName_() {
		return binName_;
	}

	public void setBinName_(String binName_) {
		this.binName_ = binName_;
	}

	public int getBinId_() {
		return binId_;
	}

	public void setBinId_(int binId_) {
		this.binId_ = binId_;
	}

	public List<ClassicItem> getItems() {
		return items;
	}

	public void setItems(List<ClassicItem> items) {
		this.items = items;
	}

	
}
