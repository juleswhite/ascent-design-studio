package org.ascent.mmkp;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ascent.configurator.AbstractRefreshChocoCore;
import org.ascent.configurator.RefreshCore;

/******************************************************************************
 * Copyright (c) 2007 Jules White.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jules White - initial API and implementation 
 ****************************************************************************/
public class NULLFeature extends Feature {

	public NULLFeature(){
		super("NULL",-1);
	}

	@Override
	public void collectFeatures(List<Feature> list) {
		
	}

	@Override
	public List<Feature> getCrossTreeDependants() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Feature> getCrossTreeExcludes() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Feature> getCrossTreeRequires() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Feature> getOptionalChildren() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Feature> getRequiredChildren() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Feature> getXorChildren() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public void insert(RefreshCore core) {
		
	}

	@Override
	public void insertAndClear(RefreshCore core) {
		
	}

	@Override
	public boolean isDescendant(Feature f) {
		return false;
	}

	@Override
	public boolean isTransparent() {
		return false;
	}

	@Override
	public void observe(AbstractRefreshChocoCore core, boolean o) {
		
	}

	@Override
	public void observe(AbstractRefreshChocoCore core, int errorrate, boolean o) {
		
	}

	@Override
	public void propagateConstraints() {
		
	}

	@Override
	public void propagateConsumedResources(Feature f) {
		
	}

	@Override
	public void setCrossTreeDependants(List<Feature> crossTreeDependants) {
		
	}

	@Override
	public void setCrossTreeExcludes(List<Feature> crossTreeExcludes) {
		
	}

	@Override
	public void setCrossTreeRequires(List<Feature> crossTreeRequires) {
		
	}

	@Override
	public void setId(int id) {
		
	}

	@Override
	public void setName(String name) {
		
	}

	@Override
	public void setOptionalChildren(List<Feature> optionalChildren) {
		
	}

	@Override
	public void setRequiredChildren(List<Feature> requiredChildren) {
		
	}

	@Override
	public void setTransparent(boolean transparent) {
		
	}

	@Override
	public void setXorChildren(List<Feature> xorChildren) {
		
	}

	@Override
	public void updateCrossTreeExcludes(List<Feature> crossTreeExcludes) {
		
	}

	@Override
	public void updateCrossTreeRequires(List<Feature> crossTreeRequires) {
		
	}

	@Override
	public void validate(Map<Object, List> sol) {
		
	}
	
	
}
