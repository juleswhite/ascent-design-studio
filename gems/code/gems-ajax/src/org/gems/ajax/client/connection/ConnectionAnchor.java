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

import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.geometry.Dimension;
import org.gems.ajax.client.geometry.Point;
import org.gems.ajax.client.geometry.Rectangle;
import org.gems.ajax.client.util.GraphicsConstants;


public abstract class ConnectionAnchor implements GraphicsConstants{

	private ConnectableDiagramElement owner_;
	
	private int direction_ = RIGHT;

	private ArrayList<Connection> attachedConnections_ = new ArrayList<Connection>();
	
	protected Point location_ = new Point();

	public ConnectionAnchor(ConnectableDiagramElement owner) {
		super();
		owner_ = owner;
		owner_.getAnchorManager().add(this);
	}

	public void dispose(){
		owner_.getAnchorManager().remove(this);
		attachedConnections_.clear();
	}
	
	public Point getLocation(Point otherend) {
		return location_;
	}

	protected void updateLocation(int x, int y) {
		location_.x = x;
		location_.y = y;
	}

	public ConnectableDiagramElement getOwner() {
		return owner_;
	}

	public void setOwner(ConnectableDiagramElement owner) {
		owner_ = owner;
	}
	
	public void translate(int dx, int dy){
		location_.x += dx;
		location_.y += dy;
	}

	public int getDirection() {
		return direction_;
	}

	public void setDirection(int direction) {
		if(direction != direction_){
			direction_ = direction;
			getOwner().getAnchorManager().directionChanged(this);
		}
	}

	public void update(){
		getOwner().getAnchorManager().update(this);
	}

	public void attach(Connection c){
		attachedConnections_.add(c);
		getOwner().getAnchorManager().connectionsChanged(this);
	}
	
	public void detach(Connection c){
		attachedConnections_.remove(c);
		getOwner().getAnchorManager().connectionsChanged(this);
	}
	
	public void forceConnectionUpdate(){
		for(Connection c : attachedConnections_){
			c.update();
		}
	}
	
	public Rectangle getBoundingBox(){
		Rectangle bounds = new Rectangle(location_,new Dimension(1,1));
		for(Connection c : attachedConnections_){
			if(c instanceof RectilinearConnection){
				RectilinearConnection rc = (RectilinearConnection)c;
				if(this == c.getSource()){
					bounds.union(rc.getSegmentBoundingBox(0));
				}
				else {
					bounds.union(rc.getSegmentBoundingBox(rc.getSegments().size()-1));
				}
			}
		}
		return bounds;
	}
}
