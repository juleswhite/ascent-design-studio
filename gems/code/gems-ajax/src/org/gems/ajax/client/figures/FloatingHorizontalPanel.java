package org.gems.ajax.client.figures;

import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.HorizontalPanel;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class FloatingHorizontalPanel extends HorizontalPanel implements
		GraphicsConstants, DiagramElementListener {

	private DiagramElement parent_;
	private GEMSDiagram diagram_;
	private boolean onBottom_ = false;

	public FloatingHorizontalPanel(boolean onb) {
		onBottom_ = onb;
		setStyleName(FLOATING_PANEL_STYLE);
		setHorizontalAlignment(ALIGN_CENTER);
	}

	public void attach(DiagramElement parent) {
		if (diagram_ != null) {
			diagram_.remove(this);
			diagram_ = null;
		}
		parent_ = parent;
		diagram_ = parent.getDiagram();
		diagram_.add(this, Util.getDiagramX(parent.getDiagramWidget())
				+ Util.half(parent.getDiagramWidget().getOffsetWidth()
						- getOffsetWidth()), Util.getDiagramY(parent
				.getDiagramWidget())
				- getTopInset());
		parent.addDiagramElementListener(this);

		updateLocation(parent);
	}

	protected void onAttach() {
		super.onAttach();
		if (parent_ != null) {
			DeferredCommand.addCommand(new Command() {

				public void execute() {
					updateLocation(parent_);
				}

			});
		}
	}

	public void detach() {
		if (diagram_ != null) {
			diagram_.remove(this);
			diagram_ = null;
		}
	}

	public void updateLocation(DiagramElement parent) {
		if (diagram_ != null) {
			if (!onBottom_)
				diagram_.setWidgetPosition(this, Util.getDiagramX(parent
						.getDiagramWidget())
						+ Util.half(parent.getDiagramWidget().getOffsetWidth()
								- getOffsetWidth()), Util.getDiagramY(parent
						.getDiagramWidget())
						- getOffsetHeight());
			else
				diagram_.setWidgetPosition(this, Util.getDiagramX(parent
						.getDiagramWidget())
						+ Util.half(parent.getDiagramWidget().getOffsetWidth()
								- getOffsetWidth()), Util.getDiagramY(parent
						.getDiagramWidget())
						+ parent.getDiagramWidget().getOffsetHeight());
		}
	}

	public int getTopInset() {
		return 0;
	}

	public int getLeftInset() {
		return 27;
	}

	public void onCollapse(AbstractDiagramElement p) {
		updateLocation(p);
	}

	public void onExpand(AbstractDiagramElement p) {
		updateLocation(p);
	}

	public void onMove(DiagramElement p) {
		updateLocation(p);
	}

	public void onResize(AbstractDiagramElement p, String w, String h) {
		updateLocation(p);
	}

	public void onChildAdded(AbstractDiagramElement p) {
	}

	public void onChildRemoved(AbstractDiagramElement p) {
	}

}
