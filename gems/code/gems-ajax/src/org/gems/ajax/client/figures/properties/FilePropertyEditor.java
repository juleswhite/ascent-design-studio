package org.gems.ajax.client.figures.properties;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FileUpload;
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

public class FilePropertyEditor extends AbstractPropertyEditor {

	private FileUpload file_;
	
	private DockPanel widget_;
	
	public FilePropertyEditor() {
		super();
		file_ = new FileUpload();
		widget_ = new DockPanel();
		widget_.add(file_,DockPanel.CENTER);
		Button b = new Button("ok");
		b.addClickListener(new ClickListener() {
		
			public void onClick(Widget sender) {
				stopEditing();
			}
		
		});
		widget_.add(b,DockPanel.SOUTH);
		widget_.setCellHorizontalAlignment(b, DockPanel.ALIGN_CENTER);
	}

	public void edit(Object v) {
		
	}

	public Object getValue() {
		return file_.getFilename();
	}

	public Widget getWidget() {
		return widget_;
	}

}
