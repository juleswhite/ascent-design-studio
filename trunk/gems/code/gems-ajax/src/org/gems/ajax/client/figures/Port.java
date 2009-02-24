package org.gems.ajax.client.figures;

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

public class Port extends AbstractDiagramElement {

	public Port(GEMSDiagram diagram) {
		super(diagram);
		add(new ImageButton(CONNECTION_HANDLE_STYLE,
				CONNECTION_HANDLE_ACTIVE_STYLE,
				CONNECTION_HANDLE_DRAGGING_STYLE,
				CONNECTION_HANDLE_DRAGGING_STYLE));
	}

	public void onDeSelect() {
	}

	public void onSelect() {
	}

}
