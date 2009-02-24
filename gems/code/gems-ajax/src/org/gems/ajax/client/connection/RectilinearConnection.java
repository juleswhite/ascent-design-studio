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

import org.gems.ajax.client.edit.EditPartManager;
import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.figures.DiagramElementListener;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.geometry.Dimension;
import org.gems.ajax.client.geometry.Point;
import org.gems.ajax.client.geometry.Rectangle;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class RectilinearConnection extends BendpointConnection implements
		DiagramElement, GraphicsConstants {

	private int hSegmentHeight_ = -1;
	private int vSegmentWidth_ = -1;

	private List<Segment> segments_;
	private ConnectionLayer connectionLayer_;
	private GEMSDiagram diagram_;

	public RectilinearConnection(GEMSDiagram diagram, ConnectionAnchor source,
			ConnectionAnchor target, ConnectionRouter router) {
		super(source, target, router);
		diagram_ = diagram;
	}

	public RectilinearConnection(GEMSDiagram diagram, ConnectionAnchor source,
			ConnectionAnchor target) {
		super(source, target, new RectilinearRouter());
		diagram_ = diagram;
	}

	public GEMSDiagram getDiagram() {
		return diagram_;
	}

	public void repaint() {
		if (connectionLayer_ != null) {
			if (segments_ == null) {
				segments_ = new ArrayList<Segment>();
			}
			updateSegments();
		}
	}

	public void dispose() {
		for (int i = 0; i < segments_.size(); i++) {
			((Segment) segments_.get(i)).dispose();
		}
		for (ConnectionDecoration cd : getConnectionDecorations()) {
			cd.dispose();
		}
	}

	public void onSelect() {
		for (int i = 0; i < segments_.size(); i++) {
			((Segment) segments_.get(i)).onSelect();
		}
	}

	public void onDeSelect() {
		for (int i = 0; i < segments_.size(); i++) {
			((Segment) segments_.get(i)).onDeSelect();
		}
	}

	public Rectangle getBounds() {
		Point[] bends = getBendPoints();
		Rectangle bounds = Util.getBoundingBox(bends);
		bounds.width += 10;
		bounds.height += 10;
		return bounds;
	}

	public void updateSegments() {

		Point[] bends = getBendPoints();

		int extra = segments_.size();
		for (int i = 0; i < bends.length - 1; i++) {
			Point a = bends[i];
			Point b = bends[i + 1];

			String style = "";
			// String style = CONNECTION_PART_STYLE;
			// if(i == 0)
			// style = CONNECTION_START_STYLE;
			// else if(i == bends.length-1)
			// style = CONNECTION_END_STYLE;
			// else if(i == 3)
			// style = CONNECTION_MIDDLE_STYLE;
			// style = style + "-";

			if (!updateSegment(a, b, i, style)) {
				Segment seg = new Segment(diagram_, this);
				EditPartManager.mapPartToElement(seg.getElement(),
						EditPartManager.getEditPart(this));
				connectionLayer_.getConnectionPanel().add(seg);
				configSegment(a, b, seg, style);
				segments_.add(seg);
			} else {
				extra--;
			}
		}

		// Need to fix this so
		// that it removes segments from segments_
		for (int i = segments_.size() - 1; i > segments_.size() - extra; i--) {
			Segment s = segments_.get(i);
			EditPartManager.unMap(s.getElement());
			s.dispose();
		}
	}

	public void configSegment(Point a, Point b, Segment seg, String style) {
		boolean h = Util.horizontalLine(a, b);
		seg.setHorizontal(h);

		int length = Math.abs(b.y - a.y) + hSegmentHeight_;
		if (h) {
			length = Math.abs(b.x - a.x) + vSegmentWidth_;
		}

		if (h && hSegmentHeight_ == -1)
			hSegmentHeight_ = seg.getOffsetHeight();
		else if (!h && vSegmentWidth_ == -1)
			vSegmentWidth_ = seg.getOffsetWidth();

		length = Math.max(0, length);
		
		seg.setLength(length);
		if (h) {
			if (b.x < a.x)
				seg.setLocation(b);
			else
				seg.setLocation(a);
		} else {
			if (b.y < a.y)
				seg.setLocation(b);
			else
				seg.setLocation(a);
		}

		seg.setStyleBaseName(style);
	}

	public boolean updateSegment(Point a, Point b, int index, String style) {
		if (index >= 0 && index < segments_.size()) {
			Segment seg = (Segment) segments_.get(index);
			DOM.removeElementAttribute(seg.getElement(), "width");
			DOM.removeElementAttribute(seg.getElement(), "height");
			DOM.removeElementAttribute(seg.getElement(), "style");
			configSegment(a, b, seg, style);
			return true;
		}
		return false;
	}

	public void clearSegments() {
		segments_.clear();
	}

	public List<Segment> getSegments() {
		return segments_;
	}

	public void setSegments(List<Segment> segments) {
		segments_ = segments;
	}

	public void translateToAbsolute(Point p) {

	}

	public void translateToRelative(Point p) {
	}

	public ConnectionLayer getConnectionLayer() {
		return connectionLayer_;
	}

	public void setConnectionLayer(ConnectionLayer connectionLayer) {
		connectionLayer_ = connectionLayer;
		if (connectionLayer_ != null)
			setRouter(connectionLayer_.getConnectionRouter());
	}

	public List<Element> getElements() {
		int sz = (segments_ != null) ? segments_.size() : 0;
		List<Element> els = new ArrayList<Element>(sz);
		if (segments_ != null) {
			for (Segment s : segments_)
				els.add(s.getElement());
		}
		return els;
	}

	public void addStyleDependentName(String styleSuffix) {
		if (segments_ != null) {
			for (Segment s : segments_)
				s.addStyleDependentName(styleSuffix);
		}
	}

	public void removeStyleDependentName(String styleSuffix) {
		if (segments_ != null) {
			for (Segment s : segments_)
				s.addStyleDependentName(styleSuffix);
		}
	}

	public void updateDecorations() {
		if (segments_ != null) {
			for (ConnectionDecoration d : getConnectionDecorations()) {
				if (d.getLocation() instanceof BasicConnectionLocation) {
					BasicConnectionLocation bcl = (BasicConnectionLocation) d
							.getLocation();

					int direction = LEFT;
					Segment s = null;
					Segment s2 = null;

					if (bcl.getLocation() == START) {
						s = segments_.get(0);
						s2 = segments_.get(segments_.size() - 1);
					} else if (bcl.getLocation() == END) {
						s = segments_.get(segments_.size() - 1);
						s2 = segments_.get(0);
					} else if (bcl.getLocation() == MIDDLE) {
						s = segments_.get(2);
						s2 = segments_.get(0);
					}

					if (s.isHorizontal()) {
						direction = (s.getAbsoluteLeft() - s2.getAbsoluteLeft() < 0) ? LEFT
								: RIGHT;
					} else {
						direction = (s.getAbsoluteTop() - s2.getAbsoluteTop() < 0) ? UP
								: DOWN;
					}

					d.setDirection(direction);

					Widget w = d.getWidget();

					int x = 0;
					int y = 0;
					Point p = Util.getDiagramLocation(s);
					if (direction == UP) {
						x = Util.half(Util.getOffsetWidth(s)) + p.x
								- Util.half(w.getOffsetWidth());
						y = p.y - 3;
					} else if (direction == DOWN) {
						x = Util.half(Util.getOffsetWidth(s)) + p.x
								- Util.half(w.getOffsetWidth());
						y = 3 + p.y + s.getLength() - w.getOffsetHeight();
					} else if (direction == LEFT) {
						y = Util.half(Util.getOffsetHeight(s)) + p.y
								- Util.half(w.getOffsetHeight());
						x = p.x - 3;
					} else if (direction == RIGHT) {
						y = Util.half(Util.getOffsetHeight(s)) + p.y
								- Util.half(w.getOffsetHeight());
						x = 3 + p.x + s.getLength() - w.getOffsetWidth();
					}

					x = adjustXOffset(bcl, s, w, x, direction);
					y = adjustYOffset(bcl, s, w, y, direction);

					AbsolutePanel cp = getConnectionLayer()
							.getConnectionPanel();
					if (w.getParent() != cp) {
						w.removeFromParent();
						cp.add(w, x, y);
					} else {
						cp.setWidgetPosition(w, x, y);
					}
					
					d.update();
				}
			}
		}
	}

	protected int adjustXOffset(BasicConnectionLocation loc, Segment s,
			Widget w, int x, int dir) {
		double xoff = (s.isHorizontal()) ? loc
				.getConnectionLengthRelativeOffset()
				* s.getLength() : loc.getConnectionWidthRelativeOffset()
				* s.getOffsetWidth();
		xoff += (s.isHorizontal()) ? loc.getDecorationLengthRelativeOffset()
				* w.getOffsetWidth() : loc.getDecorationWidthRelativeOffset()
				* w.getOffsetHeight();

		if (dir == LEFT)
			x += (int) Math.rint(xoff);
		else if (dir == RIGHT)
			x -= (int) Math.rint(xoff);
		else
			x += (int) Math.rint(xoff);

		return x;
	}

	protected int adjustYOffset(BasicConnectionLocation loc, Segment s,
			Widget w, int y, int dir) {
		double yoff = (s.isHorizontal()) ? loc
				.getConnectionWidthRelativeOffset()
				* s.getOffsetWidth() : loc.getConnectionLengthRelativeOffset()
				* s.getLength();
		yoff += (s.isHorizontal()) ? loc.getDecorationWidthRelativeOffset()
				* w.getOffsetHeight() : loc.getDecorationLengthRelativeOffset()
				* s.getOffsetWidth();
		if (dir == UP)
			y += (int) Math.rint(yoff);
		else if (dir == DOWN)
			y -= (int) Math.rint(yoff);
		else
			y += (int) Math.rint(yoff);

		return y;
	}

	public List<ConnectionDecoration> getSegmentDecorations(int seg) {
		List<ConnectionDecoration> decs = new ArrayList<ConnectionDecoration>();
		if (seg == 0) {
			for (ConnectionDecoration d : getConnectionDecorations()) {
				if (d.getLocation() instanceof BasicConnectionLocation) {
					if (((BasicConnectionLocation) d.getLocation())
							.getLocation() == START) {
						decs.add(d);
					}
				}
			}
		} else if (seg == segments_.size() - 1) {
			for (ConnectionDecoration d : getConnectionDecorations()) {
				if (d.getLocation() instanceof BasicConnectionLocation) {
					if (((BasicConnectionLocation) d.getLocation())
							.getLocation() == END) {
						decs.add(d);
					}
				}
			}
		}
		return decs;
	}

	public Dimension getSegmentBoundingBox(int seg) {		
		Segment segment = segments_.get(seg);
		Dimension d = new Dimension(Util.getOffsetWidth(segment), Util
				.getOffsetHeight(segment));

		List<ConnectionDecoration> decs = getSegmentDecorations(seg);
		for (ConnectionDecoration dec : decs) {
			d.union(new Dimension(Util.getOffsetWidth(dec.getWidget()), Util
					.getOffsetHeight(dec.getWidget())));
		}
		return d;
	}

	public Widget getDiagramWidget() {
		return getSegments().get(0);
	}
	
	public String getId() {
		return getSegments().get(0).getId();
	}

	public void onMove() {
	}

	public void addDiagramElementListener(DiagramElementListener l) {
	}

	public void removeDiagramElementListener(DiagramElementListener l) {
	}
	
	
}
