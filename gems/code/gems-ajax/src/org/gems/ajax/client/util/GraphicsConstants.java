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
public interface GraphicsConstants {
	public static final int TOP = 0;
	public static final int BOTTOM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	public static final int DEFAULT_MODEL_LAYER = 10;
	public static final int SELECTED_MODEL_LAYER = 20;
	public static final int FRONT_MODEL_LAYER = 30;
	
	public static final int UP = TOP;
	public static final int DOWN = BOTTOM;
	
	public static final int START = 1;
	public static final int MIDDLE = 2;
	public static final int END = 3;
	
	public static final String ID_ATTR = "id";
	public static final String STYLE_NAME_ATTR = "class";
	
	public static final String RELATIVE = "relative";
	public static final String ABSOLUTE = "absolute";
	public static final String POSITION_ATTR = "position";
	public static final String ZINDEX_ATTR = "zIndex";
	public static final String SIZE_ATTR = "size";
	public static final String BOUNDS_ATTR = "bounds";
	
	public static final String HORIZONTAL_LINE_STYLE = "hline";
	public static final String VERTICAL_LINE_STYLE = "vline";
	public static final String SELECTED_HORIZONTAL_LINE_STYLE = "shline";
	public static final String SELECTED_VERTICAL_LINE_STYLE = "svline";
	
	public static final String CONNECTION_END_STYLE = "gems-connection-end";
	public static final String CONNECTION_START_STYLE = "gems-connection-start";
	public static final String CONNECTION_MIDDLE_STYLE = "gems-connection-middle";
	public static final String CONNECTION_PART_STYLE = "gems-connection";
	
	public static final String DIAGRAM_WRAPPER_STYLE_NAME = "gemsdiagramwrapper";
	public static final String DIAGRAM_STYLE_NAME = "gemsdiagram";
	public static final String FIGURE_STYLE = "gemsfigure";
	public static final String SELECTED_FIGURE_STYLE = "selected";
	
	public static final String CURSOR_ATTR = "cursor";
	public static final String CURSOR_NORMAL = "pointer";
	public static final String CURSOR_WAIT = "pointer";
	public static final String CURSOR_INVALID = "pointer";
	
	public static final String VALID_CURSOR_STYLE = "valid-cursor";
	public static final String INVALID_CURSOR_STYLE = "invalid-cursor";
	public static final String NORMAL_CURSOR_STYLE = "normal-cursor";
	public static final String WAIT_CURSOR_STYLE = "wait-cursor";

	public static final String PANEL_STYLE ="gems-panel";
	public static final String CONTAINER_STYLE = "gems-container";
	public static final String BODY_PANEL_STYLE = "gems-body-panel";
	public static final String HEADER_PANEL_STYLE = "gems-header-panel";
	public static final String HEADER_PANEL_TOOLBAR_STYLE = "gems-header-panel-toolbar";
	public static final String HEADER_PANEL_CONTAINER_STYLE = "gems-header-panel-container";
	public static final String HEADER_PANEL_TITLE_STYLE = "gems-header-title";
	public static final String HEADER_PANEL_EXPAND_BUTTON_STYLE = "gems-header-button";
	public static final String HEADER_PANEL_EXPAND_BUTTON_ACTIVE_STYLE = "gems-header-button-active";
	public static final String HEADER_PANEL_CONTRACT_BUTTON_STYLE = "gems-header-cxbutton";
	public static final String HEADER_PANEL_CONTRACT_BUTTON_ACTIVE_STYLE = "gems-header-cxbutton-active";
	public static final String HEADER_PANEL_LEFT_CORNER_STYLE = "gems-header-panel-lc";
	public static final String HEADER_PANEL_RIGHT_CORNER_STYLE = "gems-header-panel-rc";
	
	public static final String RESIZER_STYLE = "gems-resizer-handle";
	public static final String RESIZER_GHOST_STYLE = "gems-resizer-ghost";
	
	public static final String TOOLBOX_STYLE = "gems-toolbox";
	public static final String CONNECTION_HANDLE_STYLE = "gems-toolbox-connector";
	public static final String CONNECTION_HANDLE_ACTIVE_STYLE = "gems-toolbox-connector-active";
	public static final String CONNECTION_HANDLE_DRAGGING_STYLE = "gems-toolbox-connector-dragging";
	public static final String CONNECTION_HANDLE_FLOAT_STYLE = "gems-toolbox-connector-float";
	public static final String CONNECTION_HANDLE_FLOAT_ACCEPT_STYLE = "gems-toolbox-connector-float-valid";
	public static final String CONNECTION_HANDLE_FLOAT_REJECT_STYLE = "gems-toolbox-connector-float-invalid";
	
	public static final String EXPANDO_EXPANDED_STYLE = "gems-expando-expanded";
	public static final String EXPANDO_COLLAPSED_STYLE = "gems-expando-collapsed";
	
	public static final String INLINE_EDITOR_STYLE = "gems-inline-editor";

	public static final String DETAIL_FIGURE_POINT_STYLE = "gems-detail-figure-point";
	public static final String DETAIL_FIGURE_FOOTER_STYLE = "gems-detail-figure-footer";
	public static final String DETAIL_FIGURE_FOOTER_LEFT_SPACER_STYLE = "gems-detail-figure-footer-spacer-left";
	public static final String DETAIL_FIGURE_FOOTER_RIGHT_SPACER_STYLE = "gems-detail-figure-footer-spacer-right";
	public static final String DETAIL_FIGURE_FOOTER_LEFT_STYLE = "gems-detail-figure-footer-left";
	public static final String DETAIL_FIGURE_FOOTER_RIGHT_STYLE = "gems-detail-figure-footer-right";
	public static final String DETAIL_FIGURE_BODY_STYLE = "gems-detail-figure-body";
	public static final String DETAIL_FIGURE_HEADER_STYLE = "gems-detail-figure-header";
	public static final String DETAIL_FIGURE_HEADER_LEFT_STYLE = "gems-detail-figure-header-left";
	public static final String DETAIL_FIGURE_HEADER_RIGHT_STYLE = "gems-detail-figure-header-right";
	public static final String DETAIL_FIGURE_LEFT_STYLE = "gems-detail-figure-left";
	public static final String DETAIL_FIGURE_RIGHT_STYLE = "gems-detail-figure-right";
	
	public static final String DRAGGER_AVATAR_STYLE = "gems-dragger-avatar";
	
	public static final String FLOATING_PANEL_STYLE = "gems-side-panel";
	
	public static final String PALETTE_PANEL_STYLE = "gems-palette";
	public static final String PALETTE_PANEL_HEADER_LEFT_STYLE = "gems-palette-lhd";
	public static final String PALETTE_PANEL_HEADER_CENTER_STYLE = "gems-palette-chd";
	public static final String PALETTE_PANEL_HEADER_RIGHT_STYLE = "gems-palette-rhd";
	public static final String PALETTE_PANEL_CENTER_STYLE = "gems-palette-center";
	
}
