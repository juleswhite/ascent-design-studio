package org.gems.ajax.client.figures;

import java.util.ArrayList;

import org.gems.ajax.client.geometry.Dimension;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class FloatingWrapper extends DockPanel implements GraphicsConstants,
		DiagramElementListener {

	private Dimension leftBounds_ = new Dimension(0,0);
	private Dimension rightBounds_ = new Dimension(0,0);
	private Dimension topBounds_ = new Dimension(0,0);
	private Dimension bottomBounds_ = new Dimension(0,0);
	
	private VerticalPanel left_;
	private VerticalPanel right_;
	private HorizontalPanel top_;
	private HorizontalPanel bottom_;
	
	
	private int widgetSpread_ = 2;
	private GEMSDiagram diagram_;
	private DiagramPanel parent_;
	private SimplePanel bodyPanel_;
	private ArrayList<Widget> leftWidgets_ = new ArrayList<Widget>(1);
	private ArrayList<Widget> rightWidgets_ = new ArrayList<Widget>(1);
	private ArrayList<Widget> topWidgets_ = new ArrayList<Widget>(1);
	private ArrayList<Widget> bottomWidgets_ = new ArrayList<Widget>(1);

	public FloatingWrapper() {
		setStyleName(TOOLBOX_STYLE);
		bodyPanel_ = new SimplePanel();
		super.add(bodyPanel_,CENTER);
		left_ = new VerticalPanel();
		super.add(left_,WEST);
		right_ = new VerticalPanel();
		super.add(right_,EAST);
		top_ = new HorizontalPanel();
		super.add(top_,NORTH);
		bottom_ = new HorizontalPanel();
		super.add(bottom_,SOUTH);
		
		setCellVerticalAlignment(left_, ALIGN_MIDDLE);
		setCellVerticalAlignment(right_, ALIGN_MIDDLE);
		setCellHorizontalAlignment(top_, ALIGN_CENTER);
		setCellHorizontalAlignment(bottom_, ALIGN_CENTER);
	}
	
	public void attach(DiagramPanel parent) {
		if (diagram_ != null) {
			diagram_.remove(this);
			diagram_ = null;
		}
		
		parent_ = parent;

		 DeferredCommand.addCommand(new Command() {
             public void execute() {
            	 updateSize(parent_);
         		diagram_ = parent_.getDiagram();
         		diagram_.add(FloatingWrapper.this, Util.getDiagramX(parent_) - getLeftInset(), Util
         				.getDiagramY(parent_)
         				- getTopInset());
         		parent_.addDiagramElementListener(FloatingWrapper.this);
         		updateSize(parent_);
         		updateLocation(parent_);
         		Util.moveBehind(FloatingWrapper.this,parent_);
             }
         });	
		
	}

	public void detach() {
		if (diagram_ != null) {
			diagram_.remove(this);
			diagram_ = null;
		}
	}

	public void updateSize(AbstractDiagramElement parent) {
		updateBounds();
		int w = parent.getOffsetWidth() + leftBounds_.width + rightBounds_.width;
		int h = parent.getOffsetHeight() + topBounds_.height + bottomBounds_.height;
		setSize(w + "px", h + "px");
		bodyPanel_.setSize(parent.getOffsetWidth()+"px", parent.getOffsetHeight()+"px");
	}

	public void updateLocation(DiagramElement parent) {
		if (diagram_ != null) {
			diagram_.setWidgetPosition(this, Util.getDiagramX(parent.getDiagramWidget()) - getLeftInset(), Util
					.getDiagramY(parent.getDiagramWidget())
					- getTopInset());
		}
	}

	public void add(Widget widget, DockLayoutConstant direction) {
		int ww = widget.getOffsetWidth();
		int wh = widget.getOffsetHeight();
		
		if(direction == EAST){
			int w = Math.max(ww, rightBounds_.width) - rightBounds_.width;
			rightBounds_.expand(w,wh + widgetSpread_);
			rightWidgets_.add(widget);
			right_.add(widget);
		}
		else if(direction == WEST){
			int w = Math.max(ww, leftBounds_.width) -leftBounds_.width;
			leftBounds_.expand(w,wh + widgetSpread_);
			leftWidgets_.add(widget);
			left_.add(widget);
		}
		else if(direction == NORTH){
			int h = Math.max(wh, topBounds_.height) - topBounds_.height;
			topBounds_.expand(ww + widgetSpread_, h);
			topWidgets_.add(widget);
			top_.add(widget);
		}
		else if(direction == SOUTH){
			int h = Math.max(wh, bottomBounds_.height) - bottomBounds_.height;
			bottomBounds_.expand(ww + widgetSpread_, h);
			bottomWidgets_.add(widget);
			bottom_.add(widget);
		}
		else {
			super.add(widget, direction);
		}
	}

	public boolean remove(Widget widget) {
		if(leftWidgets_.remove(widget)){
			int w = Math.max(widget.getOffsetWidth(), leftBounds_.width) -leftBounds_.width;
			leftBounds_.expand(-1 * w,-1 * (widget.getOffsetHeight() + widgetSpread_));
			return left_.remove(widget);
		}
		else if(rightWidgets_.remove(widget)){
			int w = Math.max(widget.getOffsetWidth(), rightBounds_.width) - rightBounds_.width;
			rightBounds_.expand(-1 * w,-1 * (widget.getOffsetHeight() + widgetSpread_));
			return right_.remove(widget);
		}
		else if(topWidgets_.remove(widget)){
			int h = Math.max(widget.getOffsetHeight(), topBounds_.height) - topBounds_.height;
			topBounds_.expand(-1 * (widget.getOffsetWidth() + widgetSpread_), -1 * h);
			return top_.remove(widget);
		}
		else if(bottomWidgets_.remove(widget)){
			int h = Math.max(widget.getOffsetHeight(), bottomBounds_.height) - bottomBounds_.height;
			bottomBounds_.expand(-1 * (widget.getOffsetWidth() + widgetSpread_),-1 * h);
			return bottom_.remove(widget);
		}
		else{
			return super.remove(widget);
		}
	}

	protected void updateBounds(){
		topBounds_ = calculateBounds(topWidgets_, true);
		bottomBounds_ = calculateBounds(bottomWidgets_, true);
		leftBounds_ = calculateBounds(leftWidgets_, false);
		rightBounds_ = calculateBounds(rightWidgets_, false);
	}
	
	protected Dimension calculateBounds(ArrayList<Widget> widgets, boolean horizontal){
		Dimension d = new Dimension(0,0);
		for(Widget w : widgets){
			if(horizontal){
				d.height = Math.max(d.height, w.getOffsetHeight());
				d.width += w.getOffsetWidth() + widgetSpread_;
			}
			else {
				d.width = Math.max(d.width, w.getOffsetWidth());
				d.height += w.getOffsetHeight() + widgetSpread_;
			}
		}
		return d;
	}
	
	public int getTopInset() {
		return topBounds_.height;
	}

	public int getLeftInset() {
		return leftBounds_.width;
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
}
