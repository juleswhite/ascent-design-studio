package org.gems.ajax.client.dnd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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

public class DnDManager {

	private static DnDManager instance_;
	private List<DropTarget> dropTargets_ = new ArrayList<DropTarget>();
	
	public static DnDManager getInstance(){
		if(instance_ == null)
			instance_ = new DnDManager();
		return instance_;
	}
	
	private boolean inDrag_ = true;
	
	private DnDManager(){}
	
	public void addDropTarget(DropTarget t){
		dropTargets_.add(t);
	}
	
	public void removeDropTarget(DropTarget t){
		dropTargets_.remove(t);
	}
	
	public DropTarget getDropTarget(int x, int y){
		ArrayList<DropTarget> matches = new ArrayList<DropTarget>(5);
		for(DropTarget t : dropTargets_){
			if(t.intersects(x, y)){
				matches.add(t);
			}
		}
		if(matches.size() > 0){
			Collections.sort(matches);
			return matches.get(0);
		}
		return null;
	}
	
}
