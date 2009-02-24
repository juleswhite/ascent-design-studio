package org.gems.ajax.client.edit;

import org.gems.ajax.client.edit.exdata.ExtendedData;
import org.gems.ajax.client.edit.exdata.RectangleData;
import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.figures.HtmlPanel;
import org.gems.ajax.client.figures.HtmlPanelListener;
import org.gems.ajax.client.figures.templates.Template;
import org.gems.ajax.client.figures.templates.TemplateData;
import org.gems.ajax.client.figures.templates.TemplateUpdateCallback;
import org.gems.ajax.client.figures.templates.TemplateUpdater;
import org.gems.ajax.client.geometry.Rectangle;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.Type;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/

public class DiagramTemplateEditPart extends DiagramPanelEditPart implements
		HtmlPanelListener {

	private Template template_;

	private TemplateUpdater updater_;

	public DiagramTemplateEditPart(ModelHelper modelHelper,
			EditPartFactory fact, Object model, Template template) {
		super(modelHelper, fact, model);
		template_ = template;
	}

	public Widget getContainer(Object child) {
		return null;
	}

	public String getContainerElementId(Object child) {
		String cid = null;
		if (getContainerIds() != null) {
			String id = getFigure().getId();
			Type[] types = getModelHelper().getTypes(child);
			if (types != null) {
				for (Type type : types) {
					cid = getContainerIds().get(type);
					if (cid != null) {
						break;
					}
				}
			}
		}
		if (cid == null)
			cid = DEFAULT_CONTAINER_ID;
		return cid;
	}

	public ConnectableDiagramElement createFigure(GEMSDiagram d) {
		HtmlPanel p = new HtmlPanel(d, template_, template_.getMoveable());
		p.addHtmlPanelListener(this);
		return p;
	}

	protected void addChild(Widget child, Object model, ExtendedData d) {
		Widget w = getContainer(model);
		if (w != null) {
			if (w instanceof AbsolutePanel) {
				Rectangle bounds = d.get(BOUNDS_ATTR, RectangleData.INSTANCE);
				((AbsolutePanel) w).add(child, bounds.x, bounds.y);
			}
		} else {
			String cid = getContainerElementId(model);
			if (cid != null) {
				getTemplateFigure().getBodyHTML().add(child, cid);
			}
		}
	}

	public void updateTemplate(String w, String h) {
		if (updater_ != null) {
			if (w == null)
				w = Util.getOffsetWidth(getTemplateFigure().getBodyPanel())
						+ "px";
			if (h == null)
				h = Util.getOffsetHeight(getTemplateFigure().getBodyPanel())
						+ "px";
			TemplateData dat = new TemplateData();
			dat.setSize(w, h);
			dat.setObjectId(getModelHelper().getId(getModel()));

			updater_.updateTemplate(dat, new TemplateUpdateCallback() {

				public void setTemplate(String html) {
					getTemplateFigure().setHtml(html);
					if (getModelFigure().getResizer() != null)
						getModelFigure().getResizer().updateDragHandle();
				}
			});
		}
	}

	public void resizeRequested(String w, String h) {
		updateTemplate(w, h);
	}

	public TemplateUpdater getUpdater() {
		return updater_;
	}

	public void setUpdater(TemplateUpdater updater) {
		updater_ = updater;

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				updater_.updateTemplate(new TemplateData(),
						new TemplateUpdateCallback() {

							public void setTemplate(String html) {
								getTemplateFigure().setBodyHtml(
										new HTMLPanel(html));
							}
						});
				updateTemplate(null, null);
			}
		});
	}

	protected void removeChild(Widget child, Object model) {
		child.removeFromParent();
	}

	public HtmlPanel getTemplateFigure() {
		return (HtmlPanel) getFigure();
	}

	public void onDeSelect() {
		super.onDeSelect();
	}

	public void onSelect() {
		super.onSelect();
	}

}
