package org.gems.ajax.client.edit.exdata;

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

public interface DataType<T> {

	/**
	 * Converts the data to a string.
	 * @param data
	 * @return
	 */
	public String toString(T data);
	
	/**
	 * Converts from a string to
	 * an instance of the DataType.
	 * This method should throw
	 * a RuntimeException if there
	 * is a problem with the string
	 * passed in.
	 * @param data
	 * @return
	 */
	public T fromString(String data);
}
