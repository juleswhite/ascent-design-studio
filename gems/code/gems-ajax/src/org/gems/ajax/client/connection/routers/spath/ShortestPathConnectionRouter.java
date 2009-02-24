package org.gems.ajax.client.connection.routers.spath;

/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: IBM Corporation - initial API and implementation
 *               Jules White - redesign for GEMS
 ******************************************************************************/

import org.gems.ajax.client.connection.Connection;

/**
 * Provides a {@link Connection}with a Shortest Path route that does not
 * intersect obstacles between the Connection's source and target anchors.
 * 
 * @author Whitney Sorenson
 * @since 3.0
 */
public class ShortestPathConnectionRouter {/*implements GEMSPanelListener,
		ConnectionRouter {

	private Map constraintMap = new HashMap();

	private Map figuresToBounds;

	private Map connectionToPaths;

	private boolean isDirty;

	private ShortestPathRouter algorithm = new ShortestPathRouter();

	private GEMSDiagram container;

	private Set staleConnections = new HashSet();

	private List<Rectangle> obstructions_ = new ArrayList<Rectangle>();

	private GEMSPanelListener figureListener = new GEMSPanelListener() {

		public void onMove(GEMSPanel source) {
			Rectangle newBounds = source.getBounds().getCopy();
			if (algorithm.updateObstacle((Rectangle) figuresToBounds
					.get(source), newBounds)) {
				queueSomeRouting();
				isDirty = true;
			}

			figuresToBounds.put(source, newBounds);
		}

		public void onChildAdded(GEMSPanel child) {
		}

		public void onChildRemoved(GEMSPanel child) {
		}

		public void onCollapsed(DiagramPanel source) {
			Rectangle newBounds = source.getBounds().getCopy();
			if (algorithm.updateObstacle((Rectangle) figuresToBounds
					.get(source), newBounds)) {
				queueSomeRouting();
				isDirty = true;
			}

			figuresToBounds.put(source, newBounds);
		}

		public void onExpanded(DiagramPanel source) {
			Rectangle newBounds = source.getBounds().getCopy();
			if (algorithm.updateObstacle((Rectangle) figuresToBounds
					.get(source), newBounds)) {
				queueSomeRouting();
				isDirty = true;
			}

			figuresToBounds.put(source, newBounds);
		}

	};

	private boolean ignoreInvalidate;


	public ShortestPathConnectionRouter(GEMSDiagram container) {
		isDirty = false;
		algorithm = new ShortestPathRouter();
		this.container = container;
		container.addPanelListener(this);
	}

	void queueSomeRouting() {
		if (connectionToPaths == null || connectionToPaths.isEmpty())
			return;
		try {
			ignoreInvalidate = true;
			((BendpointConnection) connectionToPaths.keySet().iterator().next())
					.update();
		} finally {
			ignoreInvalidate = false;
		}
	}

	void addChild(DiagramPanel child) {
		if (connectionToPaths == null)
			return;
		if (figuresToBounds.containsKey(child))
			return;
		Rectangle bounds = child.getBounds().getCopy();
		obstructions_.add(bounds);
		algorithm.addObstacle(bounds);

		figuresToBounds.put(child, bounds);
		child.addPanelListener(figureListener);
		isDirty = true;
	}

	void removeChild(DiagramPanel child) {
		if (connectionToPaths == null)
			return;
		Rectangle bounds = child.getBounds().getCopy();
		obstructions_.remove(bounds);
		boolean change = algorithm.removeObstacle(bounds);
		figuresToBounds.remove(child);
		child.removePanelListener(figureListener);

		if (change) {
			isDirty = true;
			queueSomeRouting();
		}
	}

	private void hookAll() {
		figuresToBounds = new HashMap();
		for (int i = 0; i < container.getFigureChildren().size(); i++)
			addChild((DiagramPanel) container.getFigureChildren().get(i));
	}

	private void unhookAll() {
		if (figuresToBounds != null) {
			Iterator figureItr = figuresToBounds.keySet().iterator();
			while (figureItr.hasNext()) {
				// Must use iterator's remove to avoid concurrent modification
				DiagramPanel child = (DiagramPanel) figureItr.next();
				figureItr.remove();
				removeChild(child);
			}
			figuresToBounds = null;
		}
	}


	public Object getConstraint(BendpointConnection connection) {
		return constraintMap.get(connection);
	}

	
	public void invalidate(BendpointConnection connection) {
		if (ignoreInvalidate)
			return;
		staleConnections.add(connection);
		isDirty = true;
	}

	
	public void remove(BendpointConnection connection) {
		Path path = (Path) connectionToPaths.remove(connection);
		staleConnections.remove(connection);
		constraintMap.remove(connection);
		algorithm.removePath(path);
		isDirty = true;
		if (connectionToPaths.isEmpty()) {
			unhookAll();
			connectionToPaths = null;
		} else {
			// Make sure one of the remaining is revalidated so that we can
			// re-route again.
			queueSomeRouting();
		}
	}

	private void processLayout() {
		if (staleConnections.isEmpty())
			return;
		((BendpointConnection) staleConnections.iterator().next()).update();
	}


	public void route(BendpointConnection conn) {
		Point ts = Util.getRelativeLocation(conn.getSource().getOwner());
		Point tt = Util.getRelativeLocation(conn.getTarget().getOwner());
		if (ts.equals(tt)){
			conn.setBendPoints(new Point[]{ts,tt});
			return;
		}

		if (isDirty) {
			ignoreInvalidate = true;
			processStaleConnections();
			isDirty = false;
			List updated = algorithm.solve();
			BendpointConnection current;
			for (int i = 0; i < updated.size(); i++) {
				Path path = (Path) updated.get(i);
				current = (BendpointConnection) path.data;
				// current.update();

				PointList points = path.getPoints().getCopy();
				Point ref1, ref2, start, end;
				ref1 = new PrecisionPoint(points.getPoint(1));
				ref2 = new PrecisionPoint(points.getPoint(points.size() - 2));
				// current.translateToAbsolute(ref1);
				// current.translateToAbsolute(ref2);

				start = current.getSource().getLocation(ref1).getCopy();
				end = current.getTarget().getLocation(ref2).getCopy();

				// current.translateToRelative(start);
				// current.translateToRelative(end);
				points.setPoint(start, 0);
				points.setPoint(end, points.size() - 1);
				updateConnection(current, points);
			}
			ignoreInvalidate = false;
		}
	}

	public void updateConnection(BendpointConnection current, PointList points) {
		current.setBendPoints(points.getPoints());
	}

	
	private void processStaleConnections() {
		Iterator iter = staleConnections.iterator();
		if (iter.hasNext() && connectionToPaths == null) {
			connectionToPaths = new HashMap();
			hookAll();
		}

		while (iter.hasNext()) {
			BendpointConnection conn = (BendpointConnection) iter.next();

			Path path = (Path) connectionToPaths.get(conn);
			if (path == null) {
				path = new Path(conn);
				connectionToPaths.put(conn, path);
				algorithm.addPath(path);
			}

			List constraint = (List) getConstraint(conn);
			if (constraint == null)
				constraint = Collections.EMPTY_LIST;

			Point start = Util.getRelativeLocation(conn.getSource().getOwner());
			Point end = Util.getRelativeLocation(conn.getTarget().getOwner());

			path.setStartPoint(start);
			path.setEndPoint(end);

			if (!constraint.isEmpty()) {
				PointList bends = new PointList(constraint.size());
				for (int i = 0; i < constraint.size(); i++) {
					Bendpoint bp = (Bendpoint) constraint.get(i);
					bends.addPoint(bp.getLocation());
				}
				path.setBendPoints(bends);
			} else
				path.setBendPoints(null);

			isDirty |= path.isDirty;
		}
		staleConnections.clear();
	}

	public List<Rectangle> getObstructions() {
		return obstructions_;
	}

	public void setObstructions(List<Rectangle> obstructions) {
		obstructions_ = obstructions;
	}

	public void setConstraint(BendpointConnection connection, Object constraint) {
		// Connection.setConstraint() already calls revalidate, so we know that
		// a
		// route() call will follow.
		staleConnections.add(connection);
		constraintMap.put(connection, constraint);
		isDirty = true;
	}

	public void onChildAdded(DiagramPanel child) {
		addChild(child);
	}

	public void onChildRemoved(DiagramPanel child) {
		removeChild(child);
	}

	public void onMove(DiagramPanel p) {
	}

	public void onCollapsed(DiagramPanel p) {
	}

	public void onExpanded(DiagramPanel p) {
	}*/

}
