package org.gems.ajax.client.util;

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
import org.gems.ajax.client.GEMS;
import org.gems.ajax.client.GEMSAsync;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.geometry.Point;
import org.gems.ajax.client.geometry.Rectangle;
import org.gems.ajax.client.util.dojo.DojoUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Util implements GraphicsConstants {

	private static GEMSAsync GEMS_SVC;

	private Util() {
	}

	public static boolean isAncestor(Element anc, Element child) {
		if (anc.equals(child))
			return true;

		Element parent = child.getParentElement();// DOM.getParent(child);
		while (parent != null) {
			if (parent.equals(anc))
				return true;
			parent = parent.getParentElement();// DOM.getParent(parent);
		}
		return false;
	}

	public static int bringToFront(Widget w) {
		int origz = getZIndex(w);
		setZIndex(w, FRONT_MODEL_LAYER);
		return origz;
	}

	public static int moveBehind(Widget w, Widget behindthiswidget) {
		int origz = getZIndex(w);
		setZIndex(w, getZIndex(behindthiswidget) - 1);
		return origz;
	}

	public static int moveInFrontOf(Widget w, Widget behindthiswidget) {
		int origz = getZIndex(w);
		setZIndex(w, getZIndex(behindthiswidget) + 1);
		return origz;
	}

	public static void setZIndex(Widget w, int z) {
		setZIndex(w.getElement(), z);
	}

	public static int getZIndex(Widget w) {
		return getZIndex(w.getElement());
	}

	public static native int getZIndex(Element e)/*-{
							return e.style.zIndex;
						}-*/;

	public static native void setZIndex(Element e, int z)/*-{
							e.style.zIndex = z;
						}-*/;

	public static int getOffsetWidth(Widget w) {
		if (w.getParent() == null) {
			RootPanel.get().add(w);
			int width = w.getOffsetWidth();
			RootPanel.get().remove(w);
			return width;
		} else {
			return w.getOffsetWidth();
		}
	}

	public static int getOffsetHeight(Widget w) {
		if (w.getParent() == null) {
			RootPanel.get().add(w);
			int h = w.getOffsetHeight();
			RootPanel.get().remove(w);
			return h;
		} else {
			return w.getOffsetHeight();
		}
	}

	public static native Object eval(String javascript)
	/*-{
	   try{
	    return $wnd.eval(javascript);
	   }catch(ex){
	     alert('Error:'+ex+' evaluating javascript javascript:'+javascript);
	   }
	     
	   return null;
	}-*/;

	public static native boolean evalBoolean(String javascript)
	/*-{
	   try{
	    return $wnd.eval(javascript);
	   }catch(ex){
	     alert('Error:'+ex+' evaluating javascript javascript:'+javascript);
	   }
	     
	   return false;
	}-*/;

	public static native boolean functionExists(String funcname)
	/*-{
	   try{
	    return $wnd.eval('window.'+funcname+' != null');
	   }catch(ex){}
	     
	   return false;
	}-*/;

	public static int getLeftBorderWidth(Element el) {
		int v = getPixelSize(el, "border-left-width");
		if (v == 0)
			v = getPixelSize(el, "border-width");
		if (v == 0) {
			String b = el.getStyle().getProperty("border");// DOM.getStyleAttribute(el,
			// "border");
			System.out.println(b);
		}
		return v;
	}

	public static native void cancelAllDocumentSelections()
	/*-{
	  try {
	    $wnd.getSelection().removeAllRanges();
	  } catch(ex) {}
	}-*/;

	public static int getPixelSize(Element el, String attr) {
		String v = el.getStyle().getProperty(attr);// DOM.getStyleAttribute(el,
		// attr);

		if (v == null)
			return 0;

		v = v.trim();
		if (v.endsWith("px")) {
			v = v.substring(0, v.length() - 2);
		}
		return Integer.parseInt(v);
	}

	public static Element getDescendantById(Element parent, String id) {
		for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
			Element el = parent.getChildNodes().getItem(i).cast();// DOM.getChild(parent,
			// i);
			String cid = el.getId();// DOM.getElementAttribute(el, ID_ATTR);
			if (id.equals(cid))
				return el;
			else {
				el = getDescendantById(el, id);
				if (el != null)
					return el;
			}
		}
		return null;
	}

	public static int half(int size) {
		if (size % 2 != 0)
			size++;

		return size / 2;
	}

	public static boolean horizontalLine(Point a, Point b) {
		return a.y == b.y;
	}

	public static boolean verticlLine(Point a, Point b) {
		return a.x == b.x;
	}

	public static Element findRelativeParent(Widget w) {
		Element rparent = w.getElement().getParentElement();// DOM.getParent(w.getElement());
		while (rparent != null) {
			if (RELATIVE.equals(rparent.getStyle().getProperty(POSITION_ATTR))) {
				return rparent;
			}
			rparent = rparent.getParentElement();// DOM.getParent(rparent);
		}
		return null;
	}

	public static Element findParentDiagram(Widget w) {
		Element rparent = w.getElement();// DOM.getParent(w.getElement());
		Element dig2 = null;
		while (rparent != null) {
			if (RELATIVE.equals(rparent.getStyle().getProperty(POSITION_ATTR))) {
				dig2 = rparent;
			}
			rparent = rparent.getParentElement();// DOM.getParent(rparent);
		}
		return dig2;
	}

	public static int getContainerX(Element e) {
		if (e == null)
			return 0;
		else
			return e.getAbsoluteLeft();// DOM.getAbsoluteLeft(e);
	}

	public static int getContainerY(Element e) {
		if (e == null)
			return 0;
		else
			return e.getAbsoluteTop();// DOM.getAbsoluteTop(e);
	}

	public static int getDiagramX(Widget w) {
		return w.getAbsoluteLeft() - getContainerX(findParentDiagram(w));
	}

	public static int getDiagramY(Widget w) {
		return w.getAbsoluteTop() - getContainerY(findParentDiagram(w));
	}

	public static int toDiagramX(Widget w, int x) {
		return x - getContainerX(findParentDiagram(w));
	}

	public static int toDiagramY(Widget w, int y) {
		return y - getContainerY(findParentDiagram(w));
	}

	public static Point getDiagramLocation(Widget w) {
		return new Point(getDiagramX(w), getDiagramY(w));
	}

	public static Point getRelativeLocationToAncestor(Widget w, Widget ancestor) {
		return new Point(getDiagramX(w), getDiagramY(w));
	}

	public static Rectangle getBoundingBox(Point a, Point b) {
		int x = Math.min(a.x, b.x);
		int y = Math.min(a.y, b.y);
		int width = Math.max(a.x, b.x) - x;
		int height = Math.max(a.y, b.y) - y;
		return new Rectangle(x, y, width, height);
	}

	public static Rectangle getBoundingBox(Point[] points) {
		Rectangle bounds = new Rectangle(points[0], points[0]);
		for (int i = 1; i < points.length; i++) {
			bounds.union(points[i]);
		}
		return bounds;
	}

	public static Point makeRelativeTo(Point ref, Point abs) {
		return new Point(abs.x - ref.x, abs.y - ref.y);
	}

	public static Point[] makeRelativeTo(Point ref, Point[] abs) {
		Point[] rel = new Point[abs.length];
		for (int i = 0; i < abs.length; i++) {
			rel[i] = makeRelativeTo(ref, abs[i]);
		}
		return rel;
	}

	public static void showInvalidCursor() {
		RootPanel.get().setStyleName(INVALID_CURSOR_STYLE);
	}

	public static void showValidCursor() {
		RootPanel.get().setStyleName(VALID_CURSOR_STYLE);
	}

	public static void showWaitCursor() {
		RootPanel.get().setStyleName(WAIT_CURSOR_STYLE);
	}

	public static void showNormalCursor() {
		RootPanel.get().setStyleName(NORMAL_CURSOR_STYLE);
	}

	public static void showErrorMessage(String error) {
		DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
		simplePopup.ensureDebugId("cwBasicPopup-simplePopup");
		simplePopup.setWidth("150px");
		simplePopup.setWidget(new HTML(error));
		simplePopup.setPopupPosition(Util.half(Window.getClientWidth()), Util
				.half(Window.getClientHeight()));
		simplePopup.show();
	}

	public static native void scrollElementRight(Element container, int amount)/*-{
										  container.scrollLeft += amount;
										}-*/;

	public static native void scrollElementLeft(Element container, int amount)/*-{
										  container.scrollLeft -= amount;
										}-*/;

	public static native void scrollElementDown(Element container, int amount)/*-{
										  container.scrollTop += amount;
										}-*/;

	public static native void scrollElementUp(Element container, int amount)/*-{
										  container.scrollTop -= amount;
										}-*/;

	public static native int getScrollTop(Element container)/*-{
										  return container.scrollTop;
										}-*/;

	public static native int getScrollLeft(Element container)/*-{
										  return container.scrollLeft;
										}-*/;

	public static void attachMovementListener(Widget w, MovementListener l) {
		MovementDetector d = new MovementDetector(w, l);
		DojoUtil.connect(w.getElement(), d);
	}

	public static boolean isAncestor(EditPart anc, EditPart child) {
		if (anc == child)
			return true;
		EditPart parent = child.getParent();
		while (parent != null) {
			if (parent == anc)
				return true;
			parent = parent.getParent();
		}
		return false;
	}

	public static void addCSS(String cssUrl) {
		Element head = getElementByTagName("head");
		Element cssLink = DOM.createElement("link");
		setAttribute(cssLink, "type", "text/css");
		setAttribute(cssLink, "rel", "stylesheet");
		setAttribute(cssLink, "href", cssUrl);
		head.appendChild(cssLink);
	}

	public static void addScript(String scripturl) {
		Element head = getElementByTagName("head");
		Element cssLink = DOM.createElement("script");
		setAttribute(cssLink, "type", "text/javascript");
		setAttribute(cssLink, "src", scripturl);
		head.appendChild(cssLink);
	}

	public native static void setAttribute(Element el, String key, String val) /*-{ 
	    el.setAttribute(key, val); 
	    }-*/;

	public static String getScriptLoader(String spath) {
		String script = " "
				+ "   var old = document.getElementById('"
				+ spath
				+ "');\r\n"
				+ "   if (old == null) {\n"
				+ "   var head = document.getElementsByTagName(\"head\")[0];\r\n"
				+ "   var script = document.createElement('script');\r\n"
				+ "   script.id = '" + spath + "';\r\n"
				+ "   script.type = 'text/javascript';\r\n"
				+ "   script.src = \"" + spath + "\";\r\n"
				+ "   head.appendChild(script);" + "  }";
		return script;
	}

	public static void loadScriptOnce(String spath) {
		eval(getScriptLoader(spath));
	}

	public native static Element getElementByTagName(String tagName) /*-{ 
	    var elem = $doc.getElementsByTagName(tagName); 
	    return elem ? elem[0] : null; 
	    }-*/;

	public static void makeDraggable() {
		// public boolean onEventPreview(Event event) {
		// if (DOM.eventGetType(event) == Event.ONMOUSEDOWN &&
		// DOM.isOrHasChild(getElement(), DOM.eventGetTarget(event))) {
		// DOM.eventPreventDefault(event);
		// }
		// // Always returning true as we don’t want to cancel
		// // the event, just to prevent the default behaviour.
		// return true;
		// }
		// }

	}

	

	public static GEMSAsync getGEMS() {
		if (GEMS_SVC == null) {
			GEMS_SVC = (GEMSAsync) GWT.create(GEMS.class);
			ServiceDefTarget endpoint = (ServiceDefTarget) GEMS_SVC;
			String moduleRelativeURL = GWT.getModuleBaseURL()
					+ GEMS.SERVICE_NAME;
			endpoint.setServiceEntryPoint(moduleRelativeURL);
		}
		return GEMS_SVC;
	}
}
