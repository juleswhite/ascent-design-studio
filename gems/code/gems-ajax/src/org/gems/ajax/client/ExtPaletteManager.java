/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.gems.ajax.client;


public class ExtPaletteManager {/*extends PaletteManager {

	private class ToolEntryListener implements ClickListener, ToolListener {
		private Tool tool_;
		private EditManager editManager_;
		private Widget widget_;
		private String activatedStyle_;
		private String deActivatedStyle_;

		public ToolEntryListener(Widget w, Tool tool, EditManager editManager, String activatedstyle, String deactivatedstyle) {
			super();
			tool_ = tool;
			tool_.setToolListener(this);
			editManager_ = editManager;
			activatedStyle_ = activatedstyle;
			deActivatedStyle_ = deactivatedstyle;
			widget_ = w;
		}

		public void onClick(Widget sender) {
			editManager_.setCurrentTool(tool_);
		}

		public void activated(Tool t) {
			widget_.setStyleName(activatedStyle_);
		}

		public void deActivated(Tool t) {
			widget_.setStyleName(deActivatedStyle_);
		}	
	}

	private Panel palettePanel_;
	
	public ExtPaletteManager(EditManager editManager, Panel palettePanel) {
		super(editManager);
		palettePanel_ = palettePanel;
	}

	@Override
	protected void addToolWidget(Widget w) {
		palettePanel_.add(w);
		palettePanel_.doLayout();
	}

	@Override
	protected Widget createToolWidget(ToolEntry e) {
		Label l = new Label(e.getLabel());
		l.setStyleName(e.getDeActivatedStyle());
		l.addClickListener(new ToolEntryListener(l,e.getTool(),getEditManager(),e.getActivatedStyle(),e.getDeActivatedStyle()));
		return l;
	}

	@Override
	protected void removeToolWidget(Widget w) {
		palettePanel_.remove(w);
	}
*/
}
