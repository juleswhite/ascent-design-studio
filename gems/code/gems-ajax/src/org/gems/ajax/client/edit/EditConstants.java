package org.gems.ajax.client.edit;

import java.util.Comparator;

import org.gems.ajax.client.edit.cmd.AbstractConnectionCommand;
import org.gems.ajax.client.edit.cmd.DeleteCommand;

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

public interface EditConstants {
	public static final String ASSOCIATION_TYPE = "association-type";
	
	public static final Request DELETE_REQUEST = new Request("Delete");
	public static final Request ADD_CHILD_REQUEST = new Request("Add Child");
	public static final Request ADD_CONNECTION_REQUEST = new ConnectRequest("Add Connection",null);
	public static final Request REMOVE_CONNECTION_REQUEST = new Request("Remove Connection");
	
	public static final String CONTAINMENT_TARGET = "containment";
	public static final String CONNECTION_TARGET = "connection";
	
	public static Comparator<Command> COMMAND_COMPARATOR = new Comparator<Command>() {
		public int compare(Command o1, Command o2) {
			if(o1 instanceof DeleteCommand && o2 instanceof AbstractConnectionCommand){
				return -1;
			}
			else if(o2 instanceof DeleteCommand && o1 instanceof AbstractConnectionCommand){
				return -1;
			}
			else {
				return 0;
			}
		}
	};
}
