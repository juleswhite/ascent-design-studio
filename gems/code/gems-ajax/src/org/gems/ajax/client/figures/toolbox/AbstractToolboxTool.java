package org.gems.ajax.client.figures.toolbox;

import org.gems.ajax.client.edit.Command;
import org.gems.ajax.client.edit.CommandStack;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.util.GraphicsConstants;

import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public abstract class AbstractToolboxTool implements ToolBoxTool, GraphicsConstants {

	private EditPart context_;
	private ToolBox toolBox_;
	private Widget widget_;
	private Command command_;
	private CommandStack commandStack_;
	
	public AbstractToolboxTool() {
		super();
	}
	
	protected abstract Command createCommand();
	protected abstract Widget createWidget(GEMSDiagram d);

	public void attach(ToolBox toolbox, EditPart context) {
		context_ = context;
		toolBox_ = toolbox;
		getCommand().setTarget(context);
		toolBox_.add(getWidget());
	}

	public void detach() {
		if(context_ != null && widget_ != null)
			toolBox_.remove(getWidget());
	}

	public Widget getWidget() {
		if(widget_ == null && context_ != null){
			widget_ = createWidget(context_.getFigure().getDiagram());
		}
		return widget_;
	}

	public void setCommandStack(CommandStack stk) {
		commandStack_ = stk;
	}

	public ToolBox getToolBox() {
		return toolBox_;
	}

	public void setToolBox(ToolBox toolBox) {
		toolBox_ = toolBox;
	}

	public Command getCommand() {
		if(command_ == null){
			command_ = createCommand();
		}
		return command_;
	}

	public void setCommand(Command command) {
		command_ = command;
	}

	public CommandStack getCommandStack() {
		return commandStack_;
	}

	public void setWidget(Widget widget) {
		widget_ = widget;
	}

	public void execute(Command c){
		commandStack_.execute(c);
	}
}
