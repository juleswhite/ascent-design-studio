package org.gems.ajax.client.figures.toolbox;

import org.gems.ajax.client.dnd.DragSource;
import org.gems.ajax.client.dnd.DragSourceListener;
import org.gems.ajax.client.dnd.DropTarget;
import org.gems.ajax.client.edit.CommandStack;
import org.gems.ajax.client.edit.EditConstants;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.EditPartManager;
import org.gems.ajax.client.edit.ModelEditPart;
import org.gems.ajax.client.edit.cmd.ConnectCommand;
import org.gems.ajax.client.figures.ImageButton;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class ConnectionHandle implements ToolBoxTool, GraphicsConstants, EditConstants {
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
		
		dragSource_ = new DragSource(widget_,connectionFloater_,CONNECTION_TARGET);
		dragSource_.addListener(new DragSourceListener() {
			
			public void onExitTarget(DragSource ds, DropTarget t) {
			}
		
			public void onEnterTarget(DragSource ds, DropTarget t) {
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
	
	protected SimplePanel createFloater(){
		SimplePanel s = new SimplePanel();
		s.setStyleName(CONNECTION_HANDLE_FLOAT_STYLE);
		return s;
	}
	
	protected ImageButton createHandle(){
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
	
	public ConnectCommand getConnectionCommand(EditPart context){
		return (ConnectCommand)context.getCommand(ADD_CONNECTION_REQUEST);
	}

	public void detach() {
		context_ = null;
		if(toolBox_ != null){
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

}
