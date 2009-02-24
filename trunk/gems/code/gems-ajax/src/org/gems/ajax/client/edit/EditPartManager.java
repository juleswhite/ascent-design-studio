package org.gems.ajax.client.edit;

import java.util.HashMap;
import java.util.Map;

import org.gems.ajax.client.figures.DiagramElement;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class EditPartManager {

	private static Map partMap_;

	private static Map getParts() {
		if (partMap_ == null)
			partMap_ = new HashMap();

		return partMap_;
	}

	public static Object getModel(DiagramElement el) {
		EditPart part = getEditPart(el);
		if (part != null)
			return part.getModel();
		else
			return null;
	}

	public static EditPart getEditPart(DiagramElement el) {
		return (EditPart) getParts().get(el);
	}

	public static EditPart getEditPart(Element el) {
		return (EditPart) getParts().get(el);
	}

	public static void mapPartToElement(Element e, EditPart ep) {
		getParts().put(e, ep);
	}

	public static EditPart getEditPart(Object model) {
		return (EditPart) getParts().get(model);
	}

	public static void mapPart(Object model, DiagramElement figure,
			EditPart part) {
		getParts().put(model, part);
		getParts().put(figure, part);
	}

	public static void mapPartToFigure(DiagramElement figure, EditPart part) {
		getParts().put(figure, part);
	}

	public static void mapPartToModel(Object model, EditPart part) {
		getParts().put(model, part);
	}
	
	public static void mapPartToWidget(Widget w, EditPart part) {
		getParts().put(w, part);
	}


	public static void unMap(Object key) {
		getParts().remove(key);
	}

	public static EditPart findEditPart(Element e) {
		EditPart ep = getEditPart(e);

		if (ep == null) {
			Element parent = DOM.getParent(e);
			if (parent != null) {
				return findEditPart(parent);
			}
		}

		return ep;
	}
}
