package org.gems.ajax.client.connection;

import org.gems.ajax.client.util.GraphicsConstants;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class BasicConnectionLocation implements ConnectionLocation,
		GraphicsConstants {

	private int location_ = START;

	private double connectionLengthRelativeOffset_ = 0;

	private double connectionWidthRelativeOffset_ = 0;

	private double decorationLengthRelativeOffset_ = 0;

	private double decorationWidthRelativeOffset_ = 0;

	public BasicConnectionLocation(int location) {
		super();
		location_ = location;
	}

	public int getLocation() {
		return location_;
	}

	public void setLocation(int location) {
		location_ = location;
	}

	public double getConnectionLengthRelativeOffset() {
		return connectionLengthRelativeOffset_;
	}

	public void setConnectionLengthRelativeOffset(
			double connectionLengthRelativeOffset) {
		connectionLengthRelativeOffset_ = connectionLengthRelativeOffset;
	}

	public double getConnectionWidthRelativeOffset() {
		return connectionWidthRelativeOffset_;
	}

	public void setConnectionWidthRelativeOffset(
			double connectionWidthRelativeOffset) {
		connectionWidthRelativeOffset_ = connectionWidthRelativeOffset;
	}

	public double getDecorationLengthRelativeOffset() {
		return decorationLengthRelativeOffset_;
	}

	public void setDecorationLengthRelativeOffset(
			double decorationLengthRelativeOffset) {
		decorationLengthRelativeOffset_ = decorationLengthRelativeOffset;
	}

	public double getDecorationWidthRelativeOffset() {
		return decorationWidthRelativeOffset_;
	}

	public void setDecorationWidthRelativeOffset(
			double decorationWidthRelativeOffset) {
		decorationWidthRelativeOffset_ = decorationWidthRelativeOffset;
	}

	
}
