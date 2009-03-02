package org.gems.ajax.client.connection;
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
import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.geometry.Point;

import com.google.gwt.user.client.Element;

public abstract class Connection {
	private ConnectionAnchor source_;
	private ConnectionAnchor target_;
	private List<ConnectionDecoration> connectionDecorations_ = new ArrayList<ConnectionDecoration>(1);
	private List<ConnectionListener> listeners_ = new ArrayList<ConnectionListener>();
	
	public Connection(ConnectionAnchor source,
			ConnectionAnchor target) {
		super();
		source_ = source;
		target_ = target;
	}

	public void update() {
		source_.update();
		target_.update();
		for(ConnectionListener l : listeners_)
			l.routingChanged(this);
	}

	public ConnectionAnchor getSource() {
		return source_;
	}

	public void setSource(ConnectionAnchor source) {
		source_ = source;
		for(ConnectionListener l : listeners_)
			l.sourceChanged(this);
	}

	public ConnectionAnchor getTarget() {
		return target_;
	}
	
	public ConnectionAnchor getOtherEnd(ConnectionAnchor ca){
		if(ca != getSource())
			return getSource();
		else
			return getTarget();
	}

	public void setTarget(ConnectionAnchor target) {
		target_ = target;
		for(ConnectionListener l : listeners_)
			l.targetChanged(this);
	}
	
	public void translateToAbsolute(Point p){
	}
	
	public void translateToRelative(Point p){
	}
	
	public void focus(){
	}	
	
	public void addDecoration(ConnectionDecoration e) {
		connectionDecorations_.add(e);
		update();
		for(ConnectionListener l : listeners_)
			l.decorationsChanged(this);
	}

	public void removeDecoration(ConnectionDecoration o) {
		o.dispose();
		connectionDecorations_.remove(o);
		update();
		for(ConnectionListener l : listeners_)
			l.decorationsChanged(this);
	}

	public List<ConnectionDecoration> getConnectionDecorations() {
		return connectionDecorations_;
	}

	public void setConnectionDecorations(
			List<ConnectionDecoration> connectionDecorations) {
		connectionDecorations_ = connectionDecorations;
		for(ConnectionListener l : listeners_)
			l.decorationsChanged(this);
	}

	public void addConnectionListener(ConnectionListener e) {
		listeners_.add(e);
	}

	public void removeConnectionListener(Object o) {
		listeners_.remove(o);
	}

	public abstract void dispose();
	
	public abstract void setConnectionLayer(ConnectionLayer p);
	
	public abstract List<Element> getElements();
	
	public abstract void updateDecorations();
	
	public Element getElement(){
		return getElements().get(0);
	}
}
