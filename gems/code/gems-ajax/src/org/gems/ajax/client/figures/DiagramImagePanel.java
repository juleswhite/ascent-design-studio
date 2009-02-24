package org.gems.ajax.client.figures;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Widget;

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

public class DiagramImagePanel extends GPanel {

	public DiagramImagePanel(GEMSDiagram diagram, boolean withheader,
			boolean moveable, boolean resizeable) {
		super(diagram, withheader, moveable, resizeable, false);
		setAutoSize(false);
	}

	public DiagramImagePanel(GEMSDiagram diagram, boolean withheader) {
		this(diagram, withheader, true, false);
	}

	public DiagramImagePanel(GEMSDiagram diagram) {
		this(diagram, false, true, false);
	}

	protected Widget createBodyPanel() {
		return new DeckPanel();
	}

	public DeckPanel getDeckPanel(){
		return (DeckPanel)getBodyPanel();
	}
}
