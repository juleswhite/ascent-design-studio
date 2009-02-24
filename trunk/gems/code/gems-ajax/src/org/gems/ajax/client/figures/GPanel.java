package org.gems.ajax.client.figures;

import java.util.Iterator;

import org.gems.ajax.client.geometry.Dimension;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.dojo.DojoUtil;
import org.gems.ajax.client.util.dojo.MoveListener;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class GPanel extends AbstractDiagramElement implements
		GraphicsConstants, MoveListener {

	private class ExpandButton extends ImageToggleButton {

		public ExpandButton(boolean ex) {
			super(HEADER_PANEL_CONTRACT_BUTTON_STYLE,
					HEADER_PANEL_CONTRACT_BUTTON_ACTIVE_STYLE,
					HEADER_PANEL_EXPAND_BUTTON_STYLE,
					HEADER_PANEL_EXPAND_BUTTON_ACTIVE_STYLE);
			setPressed(!ex);
			updateStyle();
		}

		public void onMouseUp(Widget sender, int x, int y) {
			super.onMouseUp(sender, x, y);

			if (!getPressed()) {
				expand();
			} else {
				collapse();
			}

		}

	}

	private boolean autoSize_ = true;
	private boolean resizeable_;
	private SimplePanel titleContainer_;
	private SimplePanel headerRightCorner_;
	private SimplePanel headerCenter_;
	private SimplePanel headerLeftCorner_;
	private DockPanel contentPanel_;
	private DeckPanel swapPanel_;
	private Resizer resizer_;
	private Dimension expandSize_;
	private String id_;
	private Label titleLabel_;
	private HorizontalPanel toolBar_;
	private ExpandButton collapseButton_;
	private Widget bodyPanel_;
	private HorizontalPanel headerPanel_;
	private Widget collapseToWidget_;

	/**
	 * This constructor creates a new panel that is resizeable and has a title
	 * bar.
	 * 
	 * @param diagram
	 *            - the parent diagram
	 */
	public GPanel(GEMSDiagram diagram) {
		this(diagram, true);
	}

	/**
	 * This constructor creates a new panel that is resizeable.
	 * 
	 * @param diagram
	 * @param withheader
	 *            - if this is true, the title bar will be added
	 */
	public GPanel(GEMSDiagram diagram, boolean withheader) {
		this(diagram, withheader, true, true, false);
	}

	/**
	 * This constructor creates a new panel.
	 * 
	 * @param diagram
	 * @param withheader
	 * @param moveable
	 * @param resizeable
	 * @param collapsible
	 */
	public GPanel(GEMSDiagram diagram, boolean withheader, boolean moveable,
			boolean resizeable, boolean collapsible) {
		this(diagram, withheader, moveable, resizeable, collapsible, false);
	}

	public GPanel(GEMSDiagram diagram, boolean withheader, boolean moveable,
			boolean resizeable, boolean collapsible, boolean manualinit) {
		super(diagram);

		if (!manualinit) {
			init();

			if (withheader)
				addTitleBar();

			setResizeable(resizeable);
			setCollapsible(collapsible);
			setMoveable(moveable);
		}
	}

	protected void init() {
		setStylePrimaryName(PANEL_STYLE);

		contentPanel_ = new DockPanel();
		contentPanel_.setStylePrimaryName(CONTAINER_STYLE);
		contentPanel_.setSize("100%", "100%");

		add(contentPanel_);

		bodyPanel_ = createBodyPanel();
		bodyPanel_.getElement().setId("panel-body");
		bodyPanel_.setStylePrimaryName(BODY_PANEL_STYLE);
		contentPanel_.add(bodyPanel_, DockPanel.CENTER);
	}

	/**
	 * Subclasses that want to change the type of body panel that is created
	 * should override this method. The widget produced by this method will be
	 * used as the center of the panel.
	 * 
	 * @return
	 */
	protected Widget createBodyPanel() {
		return new AbsolutePanel();
	}

	public void setMoveable(boolean moveable) {
		if (moveable)
			DojoUtil.makeMoveable(getElement(), this);
		// new Dragger(this);
	}

	/**
	 * Adds a title bar to the panel.
	 */
	public void addTitleBar() {
		if (headerPanel_ == null)
			createHeaderPanel();
	}

	/**
	 * Removes the title bar from the panel. This should only be called after
	 * the panel is attached to something. If you want to construct a panel
	 * without a title bar, use the constructor: <code>
	 * .. = new GPanel(diagram,false);
	 * </code>
	 */
	public void removeTitleBar() {
		if (headerPanel_ != null) {
			remove(headerPanel_);
			headerPanel_ = null;
		}
	}

	/**
	 * Sets the title that appears on the title bar.
	 */
	public void setTitle(String t) {
		if (headerPanel_ == null) {
			addTitleBar();
		}
		titleLabel_.setText(t);
	}

	/**
	 * This method globally sets the stylePrimaryName.
	 * 
	 * @param styleSuffix
	 */
	public void setGlobalStylePrimaryName(String styleSuffix) {
		setStylePrimaryName(styleSuffix);
		contentPanel_.setStylePrimaryName(styleSuffix);
		bodyPanel_.setStylePrimaryName(styleSuffix);

		if (headerPanel_ != null) {
			headerPanel_.setStylePrimaryName(styleSuffix);
			titleContainer_.setStylePrimaryName(styleSuffix);
			headerLeftCorner_.setStylePrimaryName(styleSuffix);
			headerCenter_.setStylePrimaryName(styleSuffix);
			headerRightCorner_.setStylePrimaryName(styleSuffix);
			toolBar_.setStylePrimaryName(styleSuffix);
			titleLabel_.setStylePrimaryName(styleSuffix);

			Iterator<Widget> it = toolBar_.iterator();
			while (it.hasNext()) {
				it.next().setStylePrimaryName(styleSuffix);
			}

			if (collapseButton_ != null)
				collapseButton_.setStylePrimaryName(styleSuffix);
		}
	}

	/**
	 * This method globally applies a style dependent name to every part of this
	 * widget including all elements in the toolbar.
	 * 
	 * @param styleSuffix
	 */
	public void addGlobalStyleDependentName(String styleSuffix) {
		addStyleDependentName(styleSuffix);
		contentPanel_.addStyleDependentName(styleSuffix);
		bodyPanel_.addStyleDependentName(styleSuffix);

		if (headerPanel_ != null) {
			headerPanel_.addStyleDependentName(styleSuffix);
			titleContainer_.addStyleDependentName(styleSuffix);
			headerLeftCorner_.addStyleDependentName(styleSuffix);
			headerCenter_.addStyleDependentName(styleSuffix);
			headerRightCorner_.addStyleDependentName(styleSuffix);
			toolBar_.addStyleDependentName(styleSuffix);
			titleLabel_.addStyleDependentName(styleSuffix);

			Iterator<Widget> it = toolBar_.iterator();
			while (it.hasNext()) {
				it.next().addStyleDependentName(styleSuffix);
			}

			if (collapseButton_ != null)
				collapseButton_.addStyleDependentName(styleSuffix);
		}
	}

	/**
	 * This method globally removes a style dependent name to every part of this
	 * widget including all elements in the toolbar.
	 * 
	 * @param styleSuffix
	 */
	public void removeGlobalStyleDependentName(String styleSuffix) {
		removeStyleDependentName(styleSuffix);
		contentPanel_.removeStyleDependentName(styleSuffix);
		bodyPanel_.removeStyleDependentName(styleSuffix);

		if (headerPanel_ != null) {
			headerPanel_.removeStyleDependentName(styleSuffix);
			titleContainer_.removeStyleDependentName(styleSuffix);
			headerLeftCorner_.removeStyleDependentName(styleSuffix);
			headerCenter_.removeStyleDependentName(styleSuffix);
			headerRightCorner_.removeStyleDependentName(styleSuffix);
			toolBar_.removeStyleDependentName(styleSuffix);
			titleLabel_.removeStyleDependentName(styleSuffix);

			Iterator<Widget> it = toolBar_.iterator();
			while (it.hasNext()) {
				it.next().removeStyleDependentName(styleSuffix);
			}

			if (collapseButton_ != null)
				collapseButton_.removeStyleDependentName(styleSuffix);
		}
	}

	/**
	 * Subclasses that want to change how the header is constructed should
	 * override this method.
	 */
	protected void createHeaderPanel() {
		headerPanel_ = new HorizontalPanel();
		headerPanel_.setStylePrimaryName(HEADER_PANEL_CONTAINER_STYLE);
		contentPanel_.add(headerPanel_, DockPanel.NORTH);

		headerLeftCorner_ = new SimplePanel();
		headerLeftCorner_.setStylePrimaryName(HEADER_PANEL_LEFT_CORNER_STYLE);
		headerPanel_.add(headerLeftCorner_);

		titleContainer_ = new SimplePanel();
		titleContainer_.setStylePrimaryName(HEADER_PANEL_STYLE);
		titleContainer_.add(titleLabel_);
		titleLabel_ = new Label("foo", false);
		titleLabel_.setStylePrimaryName(HEADER_PANEL_TITLE_STYLE);
		titleContainer_.add(titleLabel_);
		headerPanel_.add(titleContainer_);

		headerCenter_ = new SimplePanel();
		headerCenter_.setStylePrimaryName(HEADER_PANEL_STYLE);
		headerPanel_.add(headerCenter_);
		headerPanel_.setCellWidth(headerCenter_, "100%");

		toolBar_ = new HorizontalPanel();
		toolBar_.setStylePrimaryName(HEADER_PANEL_TOOLBAR_STYLE);
		headerPanel_.add(toolBar_);

		headerRightCorner_ = new SimplePanel();
		headerRightCorner_.setStylePrimaryName(HEADER_PANEL_RIGHT_CORNER_STYLE);
		headerPanel_.add(headerRightCorner_);
	}

	/**
	 * If a subclass overrides this method, it is vital that it makes a call to
	 * super.onAttach().
	 */
	protected void onAttach() {
		super.onAttach();

		if (autoSize_) {
			int w = 100;
			int h = 100;

			if (headerPanel_ != null && titleLabel_ != null) {
				w = Math.max(120, titleLabel_.getOffsetWidth());
				if (toolBar_ != null)
					w += toolBar_.getOffsetWidth();
			}

			setSize(w + "px", h + "px");
			contentPanel_.setCellHeight(bodyPanel_, "80px");
			contentPanel_.setCellWidth(bodyPanel_, (w - 1) + "px");
			if (headerPanel_ != null)
				contentPanel_.setCellWidth(headerPanel_, w + "px");
			bodyPanel_.setWidth((w - 1) + "px");
			bodyPanel_.setHeight("80px");
		}
	}

	public void setHeight(String height) {
		contentPanel_.setCellHeight(bodyPanel_, "1px");
		bodyPanel_.setHeight("1px");
		super.setHeight(height);
		int voff = (headerPanel_ == null) ? 0 : headerPanel_.getOffsetHeight();
		String w = ((getOffsetHeight() - voff)) + "px";
		contentPanel_.setCellHeight(bodyPanel_, w);
		bodyPanel_.setHeight(w);
	}

	public void setWidth(String width) {
		bodyPanel_.setWidth("1px");
		contentPanel_.setCellWidth(bodyPanel_, "1px");

		super.setWidth(width);
		if (getOffsetWidth() > 0) {
			contentPanel_.setCellWidth(bodyPanel_, (getOffsetWidth() - 1)
					+ "px");
			bodyPanel_.setWidth((getOffsetWidth() - 1) + "px");
		} else {
			contentPanel_.setCellWidth(bodyPanel_, getOffsetWidth() + "px");
		}

		if (headerPanel_ != null)
			contentPanel_.setCellWidth(headerPanel_, getOffsetWidth() + "px");
	}

	/**
	 * This method toggles the resizing handle on the title bar.
	 * 
	 * @param resizeable
	 */
	public void setResizeable(boolean resizeable) {
		resizeable_ = resizeable;

		// if (headerPanel_ == null)
		// addTitleBar();

		if (resizeable && resizer_ == null) {
			addResizer();
		} else if (!resizeable && resizer_ != null) {
			removeResizer();
		}
	}

	private void addResizer() {
		resizer_ = new Resizer(new Resizeable() {

			public AbsolutePanel getTargetParent() {
				return getDiagram();
			}

			public void setSize(String w, String h) {
				GPanel.this.resize(w, h);
			}

			public void removeMouseListener(MouseListener l) {
				GPanel.this.removeMouseListener(l);
			}

			public void addMouseListener(MouseListener l) {
				GPanel.this.addMouseListener(l);
			}

			public Widget getTargetWidget() {
				return GPanel.this;
			}

			public boolean inDragHandle(int x, int y, int dhw, int dhh) {
				return x > bodyPanel_.getOffsetWidth() - dhw
						&& y > bodyPanel_.getOffsetHeight() - dhh;
			}
		});
	}
	
	public void resize(String w, String h){
		setSize(w, h);
	}

	private void removeResizer() {
		resizer_.dispose();
		resizer_ = null;
	}

	/**
	 * This method collapses the panel.
	 */
	public void collapse() {
		if (resizeable_ && resizer_ != null)
			removeResizer();

		if (collapseToWidget_ == null) {
			expandSize_ = new Dimension(bodyPanel_.getOffsetWidth(), bodyPanel_
					.getOffsetHeight());
			DojoUtil.fadeOut(bodyPanel_.getElement(), 300, 0);
			setSize(expandSize_.width + "px", headerPanel_.getOffsetHeight()
					+ "px");
		} else {
			DojoUtil.fadeOut(contentPanel_.getElement(), 300, 0);
			DojoUtil.fadeIn(collapseToWidget_.getElement(), 300, 0);
		}
	}

	/**
	 * This method expands the panel.
	 */
	public void expand() {
		if (resizeable_ && resizer_ == null)
			addResizer();

		if (collapseToWidget_ == null) {
			setSize(expandSize_.width + "px",
					(headerPanel_.getOffsetHeight() + expandSize_.height)
							+ "px");

			bodyPanel_.setSize("100%", "100%");

			DojoUtil.fadeIn(bodyPanel_.getElement(), 300, 0);
		} else {
			DojoUtil.fadeOut(collapseToWidget_.getElement(), 300, 0);
			DojoUtil.fadeIn(contentPanel_.getElement(), 300, 0);
		}
	}

	/**
	 * This method toggles the collapse button on the panel.
	 * 
	 * @param collapsible
	 */
	public void setCollapsible(boolean collapsible) {
		if (collapsible && collapseButton_ == null) {
			collapseButton_ = new ExpandButton(true);
			toolBar_.add(collapseButton_);
			expandSize_ = new Dimension(bodyPanel_.getOffsetWidth(), bodyPanel_
					.getOffsetHeight());
		} else if (!collapsible && collapseButton_ != null) {
			toolBar_.remove(collapseButton_);
			collapseButton_ = null;
		}
	}

	/**
	 * This method returns the id of the panel.
	 * 
	 * @return
	 */
	public String getId() {
		return id_;
	}

	/**
	 * This method sets the id of the panel.
	 * 
	 * @param id
	 */
	public void setId(String id) {
		id_ = id;
		DOM.setElementProperty(getElement(), ID_ATTR, id_);
	}

	public Resizer getResizer() {
		return resizer_;
	}

	public void setResizer(Resizer resizer) {
		resizer_ = resizer;
	}

	public Dimension getExpandSize() {
		return expandSize_;
	}

	public void setExpandSize(Dimension expandSize) {
		expandSize_ = expandSize;
	}

	public Label getTitleLabel() {
		return titleLabel_;
	}

	public void setTitleLabel(Label titleLabel) {
		titleLabel_ = titleLabel;
	}

	public HorizontalPanel getToolBar() {
		return toolBar_;
	}

	public void setToolBar(HorizontalPanel toolBar) {
		toolBar_ = toolBar;
	}

	public ExpandButton getCollapseButton() {
		return collapseButton_;
	}

	public void setCollapseButton(ExpandButton collapseButton) {
		collapseButton_ = collapseButton;
	}

	public Widget getBodyPanel() {
		return bodyPanel_;
	}

	public void setBodyPanel(Widget bodyPanel) {
		bodyPanel_ = bodyPanel;
	}

	public HorizontalPanel getHeaderPanel() {
		return headerPanel_;
	}

	public void setHeaderPanel(HorizontalPanel headerPanel) {
		headerPanel_ = headerPanel;
	}

	public DockPanel getContentPanel() {
		return contentPanel_;
	}

	public void setContentPanel(DockPanel contentPanel) {
		contentPanel_ = contentPanel;
	}

	public void onDeSelect() {
		removeGlobalStyleDependentName(SELECTED_FIGURE_STYLE);
		updateConnections();
	}

	public void onSelect() {
		addGlobalStyleDependentName(SELECTED_FIGURE_STYLE);
	}

	public boolean getAutoSize() {
		return autoSize_;
	}

	public void setAutoSize(boolean autoSize) {
		autoSize_ = autoSize;
	}

}
