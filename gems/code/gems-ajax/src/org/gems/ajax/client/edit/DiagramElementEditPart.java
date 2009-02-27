package org.gems.ajax.client.edit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gems.ajax.client.dnd.DnDManager;
import org.gems.ajax.client.dnd.DropTarget;
import org.gems.ajax.client.edit.exdata.ExtendedData;
import org.gems.ajax.client.edit.exdata.RectangleData;
import org.gems.ajax.client.figures.AbstractDiagramElement;
import org.gems.ajax.client.figures.AbstractFloatingFigure;
import org.gems.ajax.client.figures.ConnectableDiagramElement;
import org.gems.ajax.client.figures.DetailFigure;
import org.gems.ajax.client.figures.DiagramElement;
import org.gems.ajax.client.figures.DiagramElementListener;
import org.gems.ajax.client.figures.DiagramPanel;
import org.gems.ajax.client.figures.FloatingHorizontalPanel;
import org.gems.ajax.client.figures.FloatingVerticalPanel;
import org.gems.ajax.client.figures.GEMSDiagram;
import org.gems.ajax.client.figures.PaletteFigure;
import org.gems.ajax.client.figures.Port;
import org.gems.ajax.client.figures.properties.PropertyEditor;
import org.gems.ajax.client.figures.toolbox.ConnectionHandle;
import org.gems.ajax.client.figures.toolbox.OpenFullDiagramViewTool;
import org.gems.ajax.client.figures.toolbox.ToolBox;
import org.gems.ajax.client.geometry.Rectangle;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.Property;
import org.gems.ajax.client.model.Type;
import org.gems.ajax.client.model.event.ModelListener;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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

public class DiagramElementEditPart extends AbstractModelEditPart implements
		ModelListener, ModelEditPart, EditConstants, DiagramElementListener,
		MovementAware, GraphicsConstants {

	public static final String DEFAULT_CONTAINER_ID = "children";
	private ConnectableDiagramElement figure_;
	private ToolBox toolBox_;
	private Map<Type, String> containerIds_;

	private Map<PropertyEditor, Property> propertyEditors_;
	private VerticalPanel propertiesPanel_ = new VerticalPanel();
	private AbstractFloatingFigure detailFigure_;

	private PaletteFigure paletteFigure_;

	private FloatingVerticalPanel leftPortPanel_;
	private FloatingVerticalPanel rightPortPanel_;
	private FloatingHorizontalPanel topPortPanel_;
	private FloatingHorizontalPanel bottomPortPanel_;

	private boolean selected_ = false;

	public DiagramElementEditPart(ModelHelper modelHelper,
			EditPartFactory fact, Object model) {
		super(modelHelper, fact, model);
		HTMLPanel hp = new HTMLPanel(
				"<table><tbody><tr><td>Properties:</td></tr><tr><td id=\"attrs\"></td></tr><tr><td></td></tr></tbody></table>");
		hp.add(propertiesPanel_, "attrs");
		detailFigure_ = new DetailFigure(hp);
	}

	public void dispose() {
		if (leftPortPanel_ != null)
			leftPortPanel_.detach();
		if (rightPortPanel_ != null)
			rightPortPanel_.detach();
		if (topPortPanel_ != null)
			topPortPanel_.detach();
		if (bottomPortPanel_ != null)
			bottomPortPanel_.detach();

		if (detailFigure_.isAttached())
			detailFigure_.detach();

		figure_.dispose();
	}

	public ConnectableDiagramElement createFigure(GEMSDiagram d) {
		figure_ = new DiagramPanel(d);
		figure_.addDiagramElementListener(this);

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				DnDManager.getInstance().addDropTarget(
						new DropTarget(DiagramElementEditPart.this, figure_
								.getDiagramWidget()));
			}
		});
		// extPanel_.addListener(new ContainerListenerAdapter(){
		//
		// public void onRender(Component component) {
		// extPanel_.getEl().addListener("click", new EventCallback(){
		// public void execute(com.gwtext.client.core.EventObject e) {
		// System.out.println("clicked");
		// } });
		//				
		// }
		//			
		// });
		// extPanel_.setHtml(fillTemplate(template_));
		// extPanel_.setSize("200px", "200px");
		// extPanel_.setHtml("<p>Lorem ipsum dolor sit amet, consectetuer
		// adipiscing elit. Sed metus nibh, sodales a, porta at, vulputate eget,
		// dui. In pellentesque nisl non sem. Suspendisse nunc sem, pretium
		// eget, cursus a, fringilla vel, urna.");
		EditPartManager.mapPart(getModel(), figure_, this);
		EditPartManager.mapPartToElement(figure_.getDiagramWidget()
				.getElement(), this);
		updateTags();

		return figure_;
	}

	public ToolBox getToolBox() {
		if (toolBox_ == null) {
			toolBox_ = new ToolBox(getEditDomain().getCommandStack());
			toolBox_.addTool(new ConnectionHandle());
			toolBox_.addTool(new OpenFullDiagramViewTool());
		}
		return toolBox_;
	}

	public void onDeSelect() {
		selected_ = false;
		figure_.onDeSelect();
		getToolBox().detach();
		writePropertiesToModel();
		detailFigure_.detach();
		if (paletteFigure_ != null) {
			paletteFigure_.detach();
			paletteFigure_ = null;
		}
	}

	public void onSelect() {
		selected_ = true;
		focus();
		figure_.onSelect();
		getToolBox().attach(getModelFigure());

		populateDetailFigure();
		detailFigure_.addStyleDependentName("small");
		detailFigure_.attach(figure_);

		if (paletteFigure_ == null) {
			paletteFigure_ = new PaletteFigure();
			paletteFigure_.attach(getFigure());
		}

		Util.bringToFront(toolBox_);
		Util.moveBehind(detailFigure_, toolBox_);
	}

	protected Widget getDiagramWidget() {
		return getFigure().getDiagramWidget();
	}

	public boolean isSelected() {
		return selected_;
	}

	public void focus() {
		// extPanel_.focus();
		if (getDiagramWidget() instanceof HasFocus)
			((HasFocus) getDiagramWidget()).setFocus(true);
	}

	public void addChild(EditPart child) {
		super.addChild(child);
		addChild((Widget) child.getFigure(), child.getModel(), child
				.getExtendedData());
	}

	public void removeChild(EditPart child) {
		super.removeChild(child);
		removeChild((Widget) child.getFigure(), child.getModel());
	}

	public String fillTemplate(String template) {
		String id = figure_.getId();
		template = template.replaceAll("\\sid[\\s]*\\=[\\s]*\\\"[\\s]*",
				" id=\"" + id + "-");
		return template;
	}

	public Widget getContainer(Object child) {
		return null;
	}

	public Element getContainerElement(Object child) {
		if (containerIds_ != null) {
			
			Type[] types = getModelHelper().getTypes(child);
			if (types != null) {
				for (Type type : types) {
					String cid = containerIds_.get(type);
					if (cid != null) {
						Element el = Util.getDescendantById(figure_
								.getElement(), cid);
						if (el != null)
							return el;
					}
				}
			}

			String cid = DEFAULT_CONTAINER_ID;
			Element el = Util.getDescendantById(figure_.getElement(), cid);
			if (el != null)
				return el;
		}
		return null;
	}

	// protected void addChild(DiagramPanel child, Object model) {
	// Element el = getContainerElement(model);
	// if (el != null) {
	// DOM.appendChild(el, child.getElement());
	// } else {
	// ((AbsolutePanel)extPanel_.getBodyPanel()).add(child);
	// }
	// // extPanel_.doLayout();
	// }
	//
	// protected void removeChild(DiagramPanel child, Object model) {
	// Element el = getContainerElement(model);
	// if (el != null) {
	// DOM.removeChild(el, child.getElement());
	// } else {
	// ((AbsolutePanel)extPanel_.getBodyPanel()).remove(child);
	// }
	// }

	protected void addChild(Widget child, Object model, ExtendedData d) {
		Widget w = getContainer(model);
		if (w != null) {
			if (w instanceof AbsolutePanel) {
				Rectangle bounds = d.get(BOUNDS_ATTR, RectangleData.INSTANCE);
				((AbsolutePanel) w).add(child, bounds.x, bounds.y);
			}
		} else {
			Element el = getContainerElement(model);
			if (el != null) {
				el.appendChild(child.getElement());
			}
		}
	}

	protected void removeChild(Widget child, Object model) {
		Widget w = getContainer(model);
		if (w != null) {
			child.removeFromParent();
		} else {
			Element el = getContainerElement(model);
			if (el != null) {
				el.removeChild(child.getElement());
			}
		}
	}

	public void onChildAdded(AbstractDiagramElement p) {
	}

	public void onChildRemoved(AbstractDiagramElement p) {
	}

	public void onCollapse(AbstractDiagramElement p) {
	}

	public void onExpand(AbstractDiagramElement p) {
	}

	public void onMove(DiagramElement p) {
		getModelFigure().updateConnections();

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				writePropertiesToModel();
				detailFigure_.detach();
			}
		});

		for (EditPart ep : getChildren()) {
			if (ep instanceof MovementAware) {
				((MovementAware) ep).onAncestorMoved(p);
			}
		}
	}

	public void onAncestorMoved(DiagramElement p) {
		getModelFigure().onMove();
	}

	public void onResize(AbstractDiagramElement p, String w, String h) {
	}

	protected Widget createPortFigure() {
		return new Port(getModelFigure().getDiagram());
	}

	public int getPreferredPortSide() {
		return RIGHT;
	}

	public FloatingVerticalPanel getLeftPortPanel() {
		if (leftPortPanel_ == null) {
			leftPortPanel_ = new FloatingVerticalPanel(false);
			DeferredCommand.addCommand(new Command() {

				public void execute() {
					leftPortPanel_.attach(figure_);
				}

			});
		}
		return leftPortPanel_;
	}

	public FloatingVerticalPanel getRightPortPanel() {
		if (rightPortPanel_ == null) {
			rightPortPanel_ = new FloatingVerticalPanel(true);
			DeferredCommand.addCommand(new Command() {

				public void execute() {
					rightPortPanel_.attach(figure_);
				}

			});
		}
		return rightPortPanel_;
	}

	public FloatingHorizontalPanel getTopPortPanel() {
		if (topPortPanel_ == null) {
			topPortPanel_ = new FloatingHorizontalPanel(false);
			DeferredCommand.addCommand(new Command() {

				public void execute() {
					topPortPanel_.attach(figure_);
				}

			});
		}
		return topPortPanel_;
	}

	public FloatingHorizontalPanel getBottomPortPanel() {
		if (bottomPortPanel_ == null) {
			bottomPortPanel_ = new FloatingHorizontalPanel(true);
			DeferredCommand.addCommand(new Command() {

				public void execute() {
					bottomPortPanel_.attach(figure_);
				}

			});
		}
		return bottomPortPanel_;
	}

	public ConnectableDiagramElement getModelFigure() {
		return (ConnectableDiagramElement) getFigure();
	}

	public ConnectableDiagramElement getFigure() {
		if (figure_ == null) {
			figure_ = createFigure(getDiagram());
			registerFigure();
		}
		return figure_;
	}

	protected void registerFigure() {
		figure_.addDiagramElementListener(this);

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				DnDManager.getInstance().addDropTarget(
						new DropTarget(DiagramElementEditPart.this, figure_
								.getDiagramWidget(), new String[] {
								CONTAINMENT_TARGET, CONNECTION_TARGET }));
			}
		});

		EditPartManager.mapPart(getModel(), figure_, this);
		EditPartManager.mapPartToElement(figure_.getDiagramWidget()
				.getElement(), this);
		updateTags();
	}

	protected void populateDetailFigure() {
		propertiesPanel_.clear();
		if (propertyEditors_ == null)
			propertyEditors_ = new HashMap<PropertyEditor, Property>();

		List<Property> props = getModelHelper().getProperties(getModel());
		for (Property p : props) {
			PropertyEditor editor = getFactory().createPropertyEditor(
					getView(), getModel(), p);
			propertyEditors_.put(editor, p);
			HorizontalPanel holder = new HorizontalPanel();
			holder.add(new Label(p.getName() + ":"));
			holder.add(editor.getWidget());
			propertiesPanel_.add(holder);
		}
	}

	protected void writePropertiesToModel() {
		if (propertyEditors_ != null) {
			for (PropertyEditor ed : propertyEditors_.keySet()) {
				Property p = propertyEditors_.get(ed);
				getModelHelper().setProperty(getModel(), p.getName(),
						ed.getValue());
			}
			propertyEditors_.clear();
		}
	}

	protected void setFigure(ConnectableDiagramElement figure) {
		figure_ = figure;
	}

	public boolean showsPorts() {
		return true;
	}

	public Map<Type, String> getContainerIds() {
		return containerIds_;
	}

	public void setContainerIds(Map<Type, String> containerIds) {
		containerIds_ = containerIds;
	}

	public String toString() {
		return "EditPart[" + getModel() + "]";
	}
}
