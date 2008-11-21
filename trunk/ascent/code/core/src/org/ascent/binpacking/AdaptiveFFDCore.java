/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.binpacking;

import java.util.Collections;

public class AdaptiveFFDCore extends FFDCore {

	private int[] totalResources_;
	private int[] residualResources_;
	private int[] totalResourceDemands_;
	private double[] qK_;
	
	public Object nextSource(){
		return getQueue().get(getQueue().size()-1);
	}
	
	public void removeSource(Object src){
		getQueue().remove(src);
	}
	
	public void init(){
		int dim = getSourceState(getSources().get(0)).getSize().length;
		totalResourceDemands_ = new int[dim];
		totalResources_ = new int[dim];
		residualResources_ = new int[dim];
		totalResourceDemands_ = new int[dim];
		qK_ = new double[dim];
		
		updateTotalResourceDemands();
		updateTotalResources();
		updateResidualResources();
		initQK();
		
		super.init();
	}
	
	public void updateTotalResourceDemands(){
		for(Object o : getSources()){
			ItemState s = getSourceState(o);
			for(int j = 0; j < s.getSize().length; j++){
				totalResourceDemands_[j] += s.getSize()[j];
			}
		}
	}
	public void updateTotalResources(){
		for(Object o : getTargets()){
			BinState s = getTargetState(o);
			for(int j = 0; j < s.getSize().length; j++){
				totalResources_[j] += s.getSize()[j];
			}
		}
	}
	public void updateResidualResources(){
		for(Object o : getTargets()){
			BinState s = getTargetState(o);
			for(int j = 0; j < s.getSize().length; j++){
				residualResources_[j] += s.getSize()[j];
			}
		}
	}
	
	public void initQK(){
		for(int i = 0; i < qK_.length; i++){
			qK_[i] = (((double)totalResourceDemands_[i])/((double)totalResources_[i])) + 1;
		}
	}
	
	public void updateQK(){
		for(int i = 0; i < qK_.length; i++){
			qK_[i] = ((((double)totalResources_[i])/(((double)totalResources_[i]))+((double)residualResources_[i])) * qK_[i]) + 1;
		}
	}
	
	public void updateItemWeight(AbstractState st, int[] size, double weight) {
		double w = 0;
		double qk = 0;
		for (int i = 0; i <size.length; i++) {
			qk = (st instanceof BinState)? 1 : qK_[i];
			w += (size[i] * qk) * (size[i] * qk);
		}
		w += weight;
		st.setWeight(Math.sqrt(w));
	}
	
	
	@Override
	public void postIterate() {
//		updateTotalResourceDemands();
//		updateTotalResources();
		updateResidualResources();
		updateQK();
		for (Object o : getSources())
			updateWeight(getSourceState(o));
		
		Collections.sort(getQueue(), new FFDSourceComparator());

		super.postIterate();
	}

}
