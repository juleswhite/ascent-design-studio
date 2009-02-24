package org.gems.ajax.client.figures;

import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;
import org.gems.ajax.client.util.dojo.DojoUtil;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class ExpandoFigure extends DockPanel implements GraphicsConstants{
	private ImageButton expansionButton_;
	private Widget expando_;
	private Image effectsPanel_;
	private GEMSDiagram diagram_;
	private boolean expanded_ = false;

	public ExpandoFigure(GEMSDiagram diagram){
		diagram_ = diagram;
		expansionButton_ = new ImageToggleButton(HEADER_PANEL_CONTRACT_BUTTON_STYLE,
					HEADER_PANEL_CONTRACT_BUTTON_ACTIVE_STYLE,
					HEADER_PANEL_EXPAND_BUTTON_STYLE,
					HEADER_PANEL_EXPAND_BUTTON_ACTIVE_STYLE);
		
		expando_ = new HTML("<b>foo</b>");
		effectsPanel_ = new Image("img/shdw.png");
	
		init();
	}
	
	private void init(){
		expansionButton_.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				toggleExpansion();
			}

		});
		
		effectsPanel_.addStyleDependentName("effects");
		
		add(expansionButton_,NORTH);
		add(expando_,CENTER);
		
		DojoUtil.wipeOut(expando_.getElement());
	}
	
	public void toggleExpansion() {

		if (!expanded_) {
			setStylePrimaryName(EXPANDO_EXPANDED_STYLE);
			int x = Util.getDiagramX(this) - 20;
			int y = Util.getDiagramY(this) - 20;
			int w = 100;
			int h = 100;
			effectsPanel_.setSize(w+"px", h+"px");
			DojoUtil.wipeIn(expando_.getElement());
			diagram_.add(effectsPanel_,x, y);
			Util.bringToFront(this);
		} else {
			setStylePrimaryName(EXPANDO_COLLAPSED_STYLE);
			setSize(expansionButton_.getOffsetWidth()+"px", expansionButton_.getOffsetHeight()+"px");
			diagram_.remove(effectsPanel_);
			DojoUtil.wipeOut(expando_.getElement());
		}

		expanded_ = !expanded_;
	}
	
	

	public void setStylePrimaryName(String style) {
		effectsPanel_.setStylePrimaryName(style);
	}

	public ImageButton getExpansionButton() {
		return expansionButton_;
	}

	public void setExpansionButton(ImageButton expansionButton) {
		expansionButton_ = expansionButton;
	}

}
