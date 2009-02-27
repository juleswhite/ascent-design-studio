package org.gems.ajax.client.connection.routers.spath;

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

import java.util.List;
import java.util.ListIterator;

import org.gems.ajax.client.geometry.LineSeg;
import org.gems.ajax.client.geometry.Point;
import org.gems.ajax.client.geometry.PointList;
import org.gems.ajax.client.geometry.Rectangle;



/*******************************************************************************
 * Copyright (c) 2005, 2006 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public class GEMSManhattanRouterHelper {

       private static GEMSManhattanRouterHelper instance_;

       public static GEMSManhattanRouterHelper getInstance() {
               if (instance_ == null) {
                       instance_ = new GEMSManhattanRouterHelper();
               }
               return instance_;
       }

       public void route(PointList newLine, List obstacles) {
               PointList result = new PointList(newLine.size() * 2);

               for (int i = 0; i < newLine.size() - 1; i++) {
                       PointList rectroute = rectilinearize(newLine.getPoint(i), newLine
                                       .getPoint(i + 1), obstacles);
                       rectroute.removePoint(rectroute.size() - 1);
                       result.addAll(rectroute);
               }

               result.addPoint(newLine.getLastPoint());
               newLine.removeAllPoints();
               newLine.addAll(result);
       }

       public void print(PointList pl) {
               System.out.println("{");
               for (int i = 0; i < pl.size(); i++) {
                       System.out
                                       .println("  " + pl.getPoint(i).x + "," + pl.getPoint(i).y);
               }
               System.out.println("}");
       }

       public PointList rectilinearize(Point start, Point end, List obstacles) {
               if (start.x == end.x || start.y == end.y) {
                       PointList p = new PointList();
                       p.addPoint(start);
                       p.addPoint(end);
                       return p;
               }

               PointList r = null;
               Point intermediate = rectilinearIntermediatePoint(start, end, obstacles);
               if (intermediate != null) {
                       r = new PointList(3);
                       r.addPoint(start);
                       r.addPoint(intermediate);
                       r.addPoint(end);
               } else {
                       int xdelta = end.x - start.x;
                       int ydelta = end.y - start.y;
                       int x = (xdelta % 2 == 0) ? xdelta / 2 : (xdelta + 1) / 2;
                       int y = (ydelta % 2 == 0) ? ydelta / 2 : (ydelta + 1) / 2;

                       intermediate = new Point(x + start.x, y + start.y);
                       PointList first = rectilinearize(start, intermediate, obstacles);
                       PointList second = rectilinearize(intermediate, end, obstacles);
                       second.removePoint(0);
                       first.addAll(second);
                       r = first;
               }
               return r;
       }

       public Point movePointOutsideObstruction(Point p, Rectangle ob) {
               LineSeg closestside = closestSide(p, ob);
               Point np = new Point(p.x, p.y);
               if (closestside.getOrigin().x == closestside.getTerminus().x) {
                       np.x = closestside.getOrigin().x;
               } else {
                       np.y = closestside.getOrigin().y;
               }
               return np;
       }

       public LineSeg closestSide(Point pt, Rectangle rObstruct) {
               LineSeg top = new LineSeg(rObstruct.getTopLeft(), rObstruct
                               .getTopRight());
               LineSeg bottom = new LineSeg(rObstruct.getBottomLeft(), rObstruct
                               .getBottomRight());
               LineSeg left = new LineSeg(rObstruct.getTopLeft(), rObstruct
                               .getBottomLeft());
               LineSeg right = new LineSeg(rObstruct.getTopRight(), rObstruct
                               .getBottomRight());
               double tdist = midHPoint(top).getDistance(pt);
               double bdist = midHPoint(bottom).getDistance(pt);
               double rdist = midVPoint(right).getDistance(pt);
               double ldist = midVPoint(left).getDistance(pt);

               LineSeg closest = top;
               double min = tdist;
               if (bdist < min) {
                       closest = bottom;
                       min = bdist;
               }
               if (rdist < min) {
                       closest = right;
                       min = rdist;
               }
               if (ldist < min) {
                       closest = left;
                       min = ldist;
               }
               return closest;
       }

       public Point midVPoint(LineSeg s) {
               int up = s.getTerminus().y - s.getOrigin().y;
               int yup = (up % 2 == 0) ? up / 2 : (up + 1) / 2;
               return new Point(s.getOrigin().x, s.getOrigin().y + yup);
       }

       public Point midHPoint(LineSeg s) {
               int up = s.getTerminus().x - s.getOrigin().x;
               int xup = (up % 2 == 0) ? up / 2 : (up + 1) / 2;
               return new Point(s.getOrigin().x + xup, s.getOrigin().y);
       }

       public Point getIntersectionPoint(LineSeg first, Rectangle rObstruct) {
               LineSeg side = getIntersectedSide(first, rObstruct);
               Point intersection = null;
               if (side != null) {
                       intersection = side.intersect(first, 0);
               }
               return intersection;
       }

       public LineSeg getIntersectedSide(LineSeg first, Rectangle rObstruct) {
               LineSeg top = new LineSeg(rObstruct.getTopLeft(), rObstruct
                               .getTopRight());
               LineSeg bottom = new LineSeg(rObstruct.getBottomLeft(), rObstruct
                               .getBottomRight());
               LineSeg left = new LineSeg(rObstruct.getTopLeft(), rObstruct
                               .getBottomLeft());
               LineSeg right = new LineSeg(rObstruct.getTopRight(), rObstruct
                               .getBottomRight());
               if (top.intersect(first, 0) != null)
                       return top;
               else if (bottom.intersect(first, 0) != null)
                       return bottom;
               else if (left.intersect(first, 0) != null)
                       return left;
               else if (right.intersect(first, 0) != null)
                       return right;

               return null;
       }

       public boolean validRectilinearPath(LineSeg first, LineSeg second,
                       Rectangle rObstruct) {
               LineSeg sanity1 = new LineSeg(first.getOrigin(), first.getOrigin());
               LineSeg sanity2 = new LineSeg(second.getTerminus(), second
                               .getTerminus());
               LineSeg top = new LineSeg(rObstruct.getTopLeft(), rObstruct
                               .getTopRight());
               LineSeg bottom = new LineSeg(rObstruct.getBottomLeft(), rObstruct
                               .getBottomRight());
               LineSeg left = new LineSeg(rObstruct.getTopLeft(), rObstruct
                               .getBottomLeft());
               LineSeg right = new LineSeg(rObstruct.getTopRight(), rObstruct
                               .getBottomRight());
               if (top.intersect(first, 0) != null || top.intersect(second, 0) != null
                               || bottom.intersect(first, 0) != null
                               || bottom.intersect(second, 0) != null
                               || left.intersect(first, 0) != null
                               || left.intersect(second, 0) != null
                               || right.intersect(first, 0) != null
                               || right.intersect(second, 0) != null) {
                       if (top.getOrigin().y != sanity1.getOrigin().y
                                       && top.getOrigin().y != sanity2.getOrigin().y
                                       && bottom.getOrigin().y != sanity1.getOrigin().y
                                       && bottom.getOrigin().y != sanity2.getOrigin().y
                                       && left.getOrigin().x != sanity1.getOrigin().x
                                       && left.getOrigin().x != sanity2.getOrigin().x
                                       && right.getOrigin().x != sanity1.getOrigin().x
                                       && right.getOrigin().x != sanity2.getOrigin().x) {
                               return false;
                       }
               }
               return true;
       }

       public Point rectilinearIntermediatePoint(Point start, Point end,
                       List obList) {
              
               Point a = new Point(start.x, end.y);
               Point b = new Point(start.y, end.x);
               LineSeg first = new LineSeg(start, a);
               LineSeg second = new LineSeg(a, end);
               Point r = null;
               ListIterator iter = obList.listIterator();
               boolean elim = false;
               while (iter.hasNext()) {
                       Rectangle rObstruct = (Rectangle) iter.next();
                       if (!validRectilinearPath(first, second, rObstruct)) {
                               elim = true;
                               break;
                       }
               }
               if (!elim) {
                       r = a;
               } else {
                       first = new LineSeg(start.getCopy(), b);
                       second = new LineSeg(b, end.getCopy());
                       while (iter.hasNext()) {
                               Rectangle rObstruct = (Rectangle) iter.next();
                               if (!validRectilinearPath(first, second, rObstruct)) {
                                       elim = true;
                                       break;
                               }
                       }
                       if (!elim) {
                               r = b;
                       }
               }

               return r;
       }

       public void smooth(PointList line, List obList) {
               int threshold = 1;

               Point prev = null;

               for (int i = 0; i < line.size() - 1; i++) {
                       Point curr = line.getPoint(i);
                       if (prev != null) {
                               int ydelta = Math.abs(prev.y - curr.y);
                               int xdelta = Math.abs(prev.x - curr.x);
                               if (prev != null && xdelta > 0 && xdelta <= threshold) {
                                       curr.x = prev.x;
                                       line.setPoint(curr, i);
                               } else if (prev != null && ydelta > 0 && ydelta <= threshold) {
                                       curr.y = prev.y;
                                       line.setPoint(curr, i);
                               }
                       }
                       prev = curr;
               }

               PointList temp = filterDuplicatePoints(line);
               line.removeAllPoints();
               line.addAll(temp);

               System.out.println("Pre Smooth:");
               print(line);
               for (int i = 0; i < line.size() - 1; i++) {
                       Point first = line.getPoint(i);
                       Point second = line.getPoint(i + 1);
                       if (prev != null && i < line.size() - 2) {
                               Point term = line.getPoint(i + 2);

                               if (prev.x == first.x && second.x == prev.x && term.x != prev.x
                                               && second.y != term.y) {
                                       second.x = first.x;
                                       second.y = first.y;
                               } else if (prev.x == first.x && second.x != prev.x) {
                                       Point alt = new Point(first.x, term.y);
                                       if (validPath(new LineSeg(first, alt), new LineSeg(alt,
                                                       term), obList)) {
                                               second = alt;
                                               // line.setPoint(alt, i + 1);
                                               updateConsecutive(line, i + 1, alt);
                                       }
                               } else if (prev.y == first.y && second.y != prev.y) {
                                       Point alt = new Point(term.x, first.y);
                                       if (validPath(new LineSeg(first, alt), new LineSeg(alt,
                                                       term), obList)) {
                                               second = alt;
                                               // line.setPoint(alt, i + 1);
                                               updateConsecutive(line, i + 1, alt);
                                       }
                               }
                       }
                       prev = first;
               }
               System.out.println("Post Smooth:");
               print(line);
       }

       public PointList filterDuplicatePoints(PointList pts) {
               Point last = null;
               PointList filtered = new PointList(pts.size());
               for (int i = 0; i < pts.size(); i++) {
                       Point pt = pts.getPoint(i);
                       if (last == null || last.x != pt.x || last.y != pt.y) {
                               last = pt;
                               filtered.addPoint(pt);
                       }
               }
               return filtered;
       }

       public void updateConsecutive(PointList line, int curr, Point updated) {
               Point orig = line.getPoint(curr);
               for (int i = curr; i < line.size(); i++) {
                       Point p = line.getPoint(i);
                       if (p.x == orig.x && p.y == orig.y) {
                               line.setPoint(updated, i);
                       } else {
                               break;
                       }
               }
       }

       public boolean validPath(LineSeg first, LineSeg second, List obList) {
               ListIterator iter = obList.listIterator();
               while (iter.hasNext()) {
                       Rectangle r = (Rectangle) iter.next();
                       if (!validRectilinearPath(first, second, r)) {
                               return false;
                       }
               }
               return true;
       }

}
