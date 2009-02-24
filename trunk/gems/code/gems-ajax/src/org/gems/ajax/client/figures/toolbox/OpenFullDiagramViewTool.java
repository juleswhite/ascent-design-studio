package org.gems.ajax.client.figures.toolbox;

import org.gems.ajax.client.edit.Command;
import org.gems.ajax.client.edit.cmd.OpenViewCommand;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.figures.ImageButton;

import com.google.gwt.user.client.ui.ClickListener;
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

public class OpenFullDiagramViewTool extends AbstractToolboxTool {

	protected Command createCommand() {
		return new OpenViewCommand();
	}

	protected Widget createWidget(GEMSDiagram d) {
		ImageButton b = new ImageButton(CONNECTION_HANDLE_STYLE,
				CONNECTION_HANDLE_ACTIVE_STYLE,
				CONNECTION_HANDLE_DRAGGING_STYLE,
				CONNECTION_HANDLE_DRAGGING_STYLE);
		b.addClickListener(new ClickListener() {
		
			public void onClick(Widget sender) {
				if(getCommand().canExecute()){
					execute(getCommand());
				}
			}
		
		});
		return b;
	}

}
