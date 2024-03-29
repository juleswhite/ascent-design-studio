package org.gems.ajax.client.figures.toolbox;

import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.dnd.DragSource;
import org.gems.ajax.client.dnd.DragSourceListener;
import org.gems.ajax.client.dnd.DropTarget;
import org.gems.ajax.client.edit.CommandStack;
import org.gems.ajax.client.edit.EditConstants;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.cmd.ConnectCommand;
import org.gems.ajax.client.figures.ImageButton;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.Type;
import org.gems.ajax.client.util.GraphicsConstants;

import com.google.gwt.user.client.ui.SimplePanel;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class ConnectionHandle implements ToolBoxTool, GraphicsConstants,
		EditConstants {
	private CommandStack commandStack_;
	private EditPart context_;
	private ConnectCommand connectCommand_;
	private ImageButton widget_;
	private ToolBox toolBox_;
	private SimplePanel connectionFloater_;
	private boolean dragging_ = false;
	private DragSource dragSource_;

	public ConnectionHandle() {
		super();
		widget_ = createHandle();

		connectionFloater_ = createFloater();

		dragSource_ = new DragSource(widget_, connectionFloater_,
				CONNECTION_TARGET);
		dragSource_.addListener(new DragSourceListener() {

			public void onExitTarget(DragSource ds, DropTarget t) {
				getConnectCommand().updateAssociationTypes(null);
			}

			public void onEnterTarget(DragSource ds, DropTarget t) {
				EditPart trg = t.getEditPart();
				if(trg != null){
					getConnectCommand().setTarget(trg);
				}
			}

			public void onDrop(DragSource ds, DropTarget t) {
			}

			public void onDragStart(DragSource ds) {
			}

			public void onDragEnd(DragSource ds) {
				widget_.setPressed(false);
				widget_.setActive(false);
				widget_.updateStyle();
			}

		});
	}

	protected SimplePanel createFloater() {
		SimplePanel s = new SimplePanel();
		s.setStyleName(CONNECTION_HANDLE_FLOAT_STYLE);
		return s;
	}

	protected ImageButton createHandle() {
		return new ImageButton(CONNECTION_HANDLE_STYLE,
				CONNECTION_HANDLE_ACTIVE_STYLE,
				CONNECTION_HANDLE_DRAGGING_STYLE,
				CONNECTION_HANDLE_DRAGGING_STYLE);
	}

	public void attach(ToolBox toolbox, EditPart context) {
		context_ = context;
		toolBox_ = toolbox;
		toolBox_.add(widget_);
		connectCommand_ = getConnectionCommand(context);

		dragSource_.setCommand(getConnectCommand());

	}

	public ConnectCommand getConnectionCommand(EditPart context) {
		return (ConnectCommand) context.getCommand(ADD_CONNECTION_REQUEST);
	}

	public void detach() {
		context_ = null;
		if (toolBox_ != null) {
			toolBox_.remove(widget_);
			toolBox_ = null;
		}
		widget_.setPressed(false);
		widget_.updateStyle();
	}

	public CommandStack getCommandStack() {
		return commandStack_;
	}

	public void setCommandStack(CommandStack commandStack) {
		commandStack_ = commandStack;
	}

	public ImageButton getWidget() {
		return widget_;
	}

	public EditPart getContext() {
		return context_;
	}

	public void setContext(EditPart context) {
		context_ = context;
	}

	public ConnectCommand getConnectCommand() {
		return connectCommand_;
	}

	public void setConnectCommand(ConnectCommand connectCommand) {
		connectCommand_ = connectCommand;
	}

	public ToolBox getToolBox() {
		return toolBox_;
	}

	public void setToolBox(ToolBox toolBox) {
		toolBox_ = toolBox;
	}

	public SimplePanel getConnectionFloater() {
		return connectionFloater_;
	}

	public void setConnectionFloater(SimplePanel connectionFloater) {
		connectionFloater_ = connectionFloater;
	}

	public boolean isDragging() {
		return dragging_;
	}

}
