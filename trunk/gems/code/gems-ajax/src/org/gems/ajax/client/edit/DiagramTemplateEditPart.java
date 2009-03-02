package org.gems.ajax.client.edit;

import java.util.List;

import org.gems.ajax.client.edit.exdata.ExtendedData;
import org.gems.ajax.client.edit.exdata.RectangleData;
import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.figures.HtmlPanel;
import org.gems.ajax.client.figures.HtmlPanelListener;
import org.gems.ajax.client.figures.templates.ProcessedTemplate;
import org.gems.ajax.client.figures.templates.Template;
import org.gems.ajax.client.figures.templates.TemplateData;
import org.gems.ajax.client.figures.templates.TemplateElement;
import org.gems.ajax.client.figures.templates.TemplateProcessor;
import org.gems.ajax.client.figures.templates.TemplateUpdateCallback;
import org.gems.ajax.client.figures.templates.TemplateUpdater;
import org.gems.ajax.client.geometry.Rectangle;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.Type;
import org.gems.ajax.client.util.Util;

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

	private class UpdateExec implements TemplateUpdateCallback {

		private boolean isInit_ = false;

		public UpdateExec() {
		}

		public UpdateExec(boolean isInit) {
			super();
			isInit_ = isInit;
		}

		public void setTemplate(String html) {
			ProcessedTemplate t = TemplateProcessor.processTemplate(html);

			getTemplateFigure().setBodyHtml(new TemplateHTMLPanel(t));
			
			if(isInit_ && t.getProperties() != null){
				String w = t.getProperties().get(ProcessedTemplate.INIT_WIDTH);
				String h = t.getProperties().get(ProcessedTemplate.INIT_HEIGHT);
				if(w != null){
					getDiagramWidget().setWidth(w);
				}
				if(h != null){
					getDiagramWidget().setHeight(h);
				}
				
			}

			if (getModelFigure().getResizer() != null)
				getModelFigure().getResizer().updateDragHandle();
		}
	}

	private class TemplateHTMLPanel extends HTMLPanel {

		private ProcessedTemplate template_;

		public TemplateHTMLPanel(ProcessedTemplate t) {
			super(t.getHtml());
			template_ = t;
		}

		protected void onLoad() {
			super.onLoad();

			// The problem is that we don't know if the script
			// element is attached...we know that the html is attached
			// but the script has to be done before the init method
			// is fired
			List<TemplateElement> scripts = template_.getElementsToLoad();
			for (TemplateElement script : scripts)
				script.load();
		}
	}

	private Template template_;
	private boolean readyForUpdate_ = false;
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
	
	public void updateTemplate(String w, String h){
		updateTemplate(w, h, false);
	}

	public void updateTemplate(String w, String h, boolean init) {
		if (updater_ != null) {
//			if (w == null)
//				w = Util.getOffsetWidth(getTemplateFigure().getBodyPanel())
//						+ "px";
//			if (h == null)
//				h = Util.getOffsetHeight(getTemplateFigure().getBodyPanel())
//						+ "px";
			TemplateData dat = new TemplateData();
			if(w != null && h != null)
				dat.setSize(w, h);
			
			dat.setObjectId(getModelHelper().getId(getModel()));

			updater_.updateTemplate(dat, new UpdateExec(init));
		}
	}

	public void onLoad(HtmlPanel p) {
		readyForUpdate_ = true;
		updateTemplate(null, null, true);
	}

	public void resizeRequested(String w, String h) {
		updateTemplate(w, h);
	}

	public TemplateUpdater getUpdater() {
		return updater_;
	}

	public void setUpdater(TemplateUpdater updater) {
		updater_ = updater;
		if(readyForUpdate_)
			updateTemplate(null, null, true);
		/*
		 * DeferredCommand.addCommand(new Command() { public void execute() {
		 * updater_.updateTemplate(new TemplateData(), new
		 * TemplateUpdateCallback() {
		 * 
		 * public void setTemplate(String html) { ProcessedTemplate t =
		 * ScriptExtractor.processTemplate(html); //
		 * getTemplateFigure().setBodyHtml( // new HTMLPanel(t.getHtml()));
		 * getTemplateFigure().setBodyHtml(new TemplateHTMLPanel(t));
		 * 
		 * 
		 * // processTemplateElements(t); } }); // updateTemplate(null, null); }
		 * });
		 */
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
