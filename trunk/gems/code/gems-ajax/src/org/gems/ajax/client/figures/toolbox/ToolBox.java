package org.gems.ajax.client.figures.toolbox;

import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.edit.CommandStack;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.EditPartManager;
import org.gems.ajax.client.figures.AbstractDiagramElement;
import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.figures.DiagramElementListener;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.ui.HorizontalPanel;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class ToolBox extends HorizontalPanel implements GraphicsConstants,
		DiagramElementListener {

	private GEMSDiagram diagram_;
	private List<ToolBoxTool> tools_ = new ArrayList<ToolBoxTool>();
	private CommandStack commandStack_;

	public ToolBox(CommandStack stk) {
		setStyleName(TOOLBOX_STYLE);
		commandStack_ = stk;
	}

	public void attach(DiagramElement parent) {
		if (diagram_ != null) {
			diagram_.remove(this);
			diagram_ = null;
		}

		updateSize(parent);
		diagram_ = parent.getDiagram();
		diagram_.add(this, Util.getDiagramX(parent.getDiagramWidget()), Util.getDiagramY(parent.getDiagramWidget())
				- getTopInset());
		parent.addDiagramElementListener(this);

		EditPart ctx = EditPartManager.getEditPart(parent);
		for (ToolBoxTool t : tools_) {
			t.setCommandStack(commandStack_);
			t.attach(this, ctx);
		}

		updateLocation(parent);
	}

	public void detach() {
		if (diagram_ != null) {
			diagram_.remove(this);
			diagram_ = null;
		}

		for (ToolBoxTool t : tools_) {
			t.detach();
		}
	}

	public void updateSize(DiagramElement parent) {
		setSize(parent.getDiagramWidget().getOffsetWidth() + "px", getTopInset() + "px");
	}

	public void updateLocation(DiagramElement parent) {
		if (diagram_ != null) {
			diagram_.setWidgetPosition(this, Util.getDiagramX(parent.getDiagramWidget()), Util
					.getDiagramY(parent.getDiagramWidget())
					- getOffsetHeight());
		}
	}

	public int getTopInset() {
		return 27;
	}

	public int getLeftInset() {
		return 20;
	}

	public void onCollapse(AbstractDiagramElement p) {
		updateSize(p);
	}

	public void onExpand(AbstractDiagramElement p) {
		updateSize(p);
	}

	public void onMove(DiagramElement p) {
		updateLocation(p);
	}

	public void onResize(AbstractDiagramElement p, String w, String h) {
		updateSize(p);
	}

	public void onChildAdded(AbstractDiagramElement p) {
	}

	public void onChildRemoved(AbstractDiagramElement p) {
	}

	public void addTool(ToolBoxTool tool) {
		tools_.add(tool);
	}

	public void removeTool(ToolBoxTool tool) {
		tools_.remove(tool);
	}

	public CommandStack getCommandStack() {
		return commandStack_;
	}

	public void setCommandStack(CommandStack commandStack) {
		commandStack_ = commandStack;
	}

}
