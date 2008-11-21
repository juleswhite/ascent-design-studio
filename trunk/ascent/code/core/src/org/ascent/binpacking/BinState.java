package org.ascent.binpacking;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class BinState extends AbstractState {
	private List<ItemState> sources_ = new ArrayList<ItemState>(1);

	public List<ItemState> getSources() {
		return sources_;
	}

	public void setSources(List<ItemState> sources) {
		sources_ = sources;
	}

}
