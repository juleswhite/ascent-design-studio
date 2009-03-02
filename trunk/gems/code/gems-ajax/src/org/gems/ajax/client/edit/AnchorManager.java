package org.gems.ajax.client.edit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gems.ajax.client.connection.ConnectionAnchor;
import org.gems.ajax.client.connection.WidgetSideAnchor;
import org.gems.ajax.client.geometry.Point;
import org.gems.ajax.client.geometry.Rectangle;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class AnchorManager implements GraphicsConstants {

	private class AnchorData {
		public int xOff;
		public int yOff;
		public int side;
		public int index;
		public int height;
		public int width;
		public int sourceCoord_;

		public AnchorData(int off, int off2, int side, int index) {
			super();
			xOff = off;
			yOff = off2;
			this.side = side;
			this.index = index;
		}

		public AnchorData(int side, Point offsets, Rectangle bounds, int index) {
			super();
			xOff = offsets.x;
			yOff = offsets.y;
			this.side = side;
			this.index = index;
			height = bounds.height;
			width = bounds.width;
		}

		public int getSourceCoord() {
			return sourceCoord_;
		}

		public void setSourceCoord(int sourceCoord) {
			sourceCoord_ = sourceCoord;
		}

	}

	private Comparator<ConnectionAnchor> anchorSorter_ = new Comparator<ConnectionAnchor>() {

		public int compare(ConnectionAnchor o1, ConnectionAnchor o2) {
			AnchorData o1d = anchorData_.get(o1);
			AnchorData o2d = anchorData_.get(o2);
			if (o1d.sourceCoord_ == o2d.sourceCoord_)
				return o1d.index - o2d.index;
			else
				return o1d.sourceCoord_ - o2d.sourceCoord_;
		}

	};

	private int leftHeight_;
	private int rightHeight_;
	private int topWidth_;
	private int bottomWidth_;
	private int anchorSpread_ = 3;
	private Widget owner_;
	private Map<ConnectionAnchor, AnchorData> anchorData_ = new HashMap<ConnectionAnchor, AnchorData>(
			5);
	private ArrayList<ConnectionAnchor> right_ = new ArrayList<ConnectionAnchor>(
			3);
	private ArrayList<ConnectionAnchor> left_ = new ArrayList<ConnectionAnchor>(
			3);
	private ArrayList<ConnectionAnchor> top_ = new ArrayList<ConnectionAnchor>(
			3);
	private ArrayList<ConnectionAnchor> bottom_ = new ArrayList<ConnectionAnchor>(
			3);
	private ArrayList<ConnectionAnchor> anchors_ = new ArrayList<ConnectionAnchor>();

	public AnchorManager(Widget owner) {
		super();
		owner_ = owner;
	}

	public void add(ConnectionAnchor ca) {
		anchors_.add(ca);
		int side = ca.getDirection();
		ArrayList<ConnectionAnchor> l = getSideList(side);
		AnchorData d = new AnchorData(side, calculateOffsets(ca, l), ca
				.getBoundingBox(), anchors_.size() - 1);
		anchorData_.put(ca, d);

		addToSide(ca, d, l);
	}

	public void connectionsChanged(ConnectionAnchor ca) {
		AnchorData d = anchorData_.get(ca);
		Rectangle bounds = ca.getBoundingBox();
		d.height = bounds.height;
		d.width = bounds.width;

		ArrayList<ConnectionAnchor> l = getSideList(ca.getDirection());
		removeFromSide(ca, d, l);

		addToSide(ca, d, l);
	}

	public Widget getOwner() {
		return owner_;
	}

	public void setOwner(Widget owner) {
		owner_ = owner;
	}

	public void addToSide(ConnectionAnchor ca, AnchorData d,
			ArrayList<ConnectionAnchor> l) {

		l.add(ca);
		int cdx = (ca.getDirection() == LEFT || ca.getDirection() == RIGHT) ? d.height
				: d.width;

		ConnectionAnchor oanc = ca.getOppositeAnchor();
		if (oanc != null) {
			int scoord = 0;
			Widget ow = oanc.getOwner().getDiagramWidget();

			scoord = (ca.getDirection() == LEFT || ca.getDirection() == RIGHT) ? Util
					.getDiagramY(ow)
					: Util.getDiagramX(ow);
			d.setSourceCoord(scoord);
		}

		if (l.size() > 0)
			cdx = cdx + anchorSpread_;

		Collections.sort(l, anchorSorter_);

		updateAnchorPositions(ca.getDirection(), cdx, l);
	}

	protected void updateAnchorPositions(int side, int cdx,
			ArrayList<ConnectionAnchor> l) {

		int ld = 0;
		for (int i = 0; i < l.size(); i++) {
			ConnectionAnchor anc = l.get(i);
			AnchorData ad = anchorData_.get(anc);
			if (side == LEFT || side == RIGHT) {
				ld -= ad.height;
			} else {
				ld -= ad.width;
			}
			if (i != 0)
				ld -= anchorSpread_;
		}

		ld = Util.half(ld);

		for (int i = 0; i < l.size(); i++) {
			ConnectionAnchor anc = l.get(i);
			AnchorData ad = anchorData_.get(anc);
			ad.xOff = 0;
			ad.yOff = 0;

			if (side == LEFT || side == RIGHT) {
				ad.yOff = ld;
				ld += ad.height + anchorSpread_;
			} else {
				ad.xOff = ld;
				ld += ad.width + anchorSpread_;
			}
			// anc.forceConnectionUpdate();
		}

	}

	public void remove(ConnectionAnchor ca) {

		AnchorData d = anchorData_.get(ca);
		for (int i = d.index + 1; i < anchors_.size(); i++) {
			AnchorData ad = anchorData_.get(anchors_.get(i));
			ad.index--;
		}

		ArrayList<ConnectionAnchor> l = getSideList(d.side);
		removeFromSide(ca, d, l);
		anchors_.remove(ca);
		anchorData_.remove(ca);

	}

	public void removeFromSide(ConnectionAnchor ca, AnchorData d,
			ArrayList<ConnectionAnchor> l) {

		l.remove(ca);
		int cdx = (d.side == LEFT || d.side == RIGHT) ? d.height : d.width;
		if (l.size() > 0)
			cdx = cdx + anchorSpread_;
		cdx = -1 * cdx;
		updateAnchorPositions(d.side, cdx, l);
	}

	public ArrayList<ConnectionAnchor> getSideList(int side) {
		switch (side) {
		case LEFT:
			return left_;
		case RIGHT:
			return right_;
		case BOTTOM:
			return bottom_;
		case TOP:
			return top_;
		}

		return null;
	}

	public int getSideLength(int side) {
		switch (side) {
		case LEFT:
			return leftHeight_;
		case RIGHT:
			return rightHeight_;
		case BOTTOM:
			return bottomWidth_;
		case TOP:
			return topWidth_;
		}

		return 0;
	}

	public void setSideLength(int side, int l) {
		switch (side) {
		case LEFT:
			leftHeight_ = l;
			break;
		case RIGHT:
			rightHeight_ = l;
			break;
		case BOTTOM:
			bottomWidth_ = l;
			break;
		case TOP:
			topWidth_ = l;
			break;
		}
	}

	public ArrayList<ConnectionAnchor> getAnchors() {
		return anchors_;
	}

	public void setAnchors(ArrayList<ConnectionAnchor> anchors) {
		anchors_ = anchors;
	}

	public void directionChanged(ConnectionAnchor ca) {
		ArrayList<ConnectionAnchor> l = getSideList(ca.getDirection());
		AnchorData d = anchorData_.get(ca);
		removeFromSide(ca, d, getSideList(d.side));
		d.side = ca.getDirection();
		addToSide(ca, d, l);
	}

	public Point calculateOffsets(ConnectionAnchor c,
			ArrayList<ConnectionAnchor> anchors) {
		return new Point(0, 0);
	}

	public void update(ConnectionAnchor ca) {
		AnchorData ad = anchorData_.get(ca);
		ca.translate(ad.xOff, ad.yOff);
		// if (dirty_) {
		// xOffset_ = 0;
		// yOffset_ = 0;
		//		
		// List<ConnectionAnchor> anchors = anchorsOnSide(side);
		//		
		// for (ConnectionAnchor ca : anchors) {
		// if (ca != this) {
		// if (side == LEFT || side == RIGHT) {
		// yOffset_ -= Util.half(ca.getBoundingBox().height
		// + anchorSpread_);
		// } else {
		// xOffset_ -= Util.half(ca.getBoundingBox().width
		// + anchorSpread_);
		// }
		// }
		// }
		//
		// for (ConnectionAnchor ca : anchors) {
		// if (ca != this) {
		// if (side == LEFT || side == RIGHT) {
		// yOffset_ += ca.getBoundingBox().height
		// + anchorSpread_;
		// } else {
		// xOffset_ += ca.getBoundingBox().width
		// + anchorSpread_;
		// }
		// } else {
		// break;
		// }
		// }
		//
		// dirty_ = false;
		// }
		//	
		// location_.x += xOffset_;
		// location_.y += yOffset_;
	}

	public List<ConnectionAnchor> anchorsOnSide(int side) {
		ArrayList<ConnectionAnchor> anchors = new ArrayList<ConnectionAnchor>();
		for (ConnectionAnchor ca : getAnchors()) {
			if (ca instanceof WidgetSideAnchor) {
				if (((WidgetSideAnchor) ca).getDirection() == side) {
					anchors.add(ca);
				}
			}
		}
		return anchors;
	}
}
