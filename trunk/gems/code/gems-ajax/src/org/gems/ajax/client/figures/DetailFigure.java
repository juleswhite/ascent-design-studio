package org.gems.ajax.client.figures;

import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
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

public class DetailFigure extends AbstractFloatingFigure implements GraphicsConstants {

	private Widget contentFigure_;
	private VerticalPanel center_;
	private SimplePanel headerLeft_;
	private SimplePanel headerRight_;
	private Widget header_;
	private SimplePanel footer_;
	private SimplePanel footerLeft_;
	private SimplePanel footerRight_;
	private SimplePanel left_;
	private SimplePanel right_;
	private SimplePanel footerLeftSpacer_;
	private SimplePanel footerRightSpacer_;
	private SimplePanel point_;

	public DetailFigure(Widget content) {
		contentFigure_ = content;
		point_ = new SimplePanel();
		point_.setStylePrimaryName(DETAIL_FIGURE_POINT_STYLE);

		header_ = new SimplePanel();
		header_.setStylePrimaryName(DETAIL_FIGURE_HEADER_STYLE);
		headerLeft_ = new SimplePanel();
		headerLeft_.setStylePrimaryName(DETAIL_FIGURE_HEADER_LEFT_STYLE);
		headerRight_ = new SimplePanel();
		headerRight_.setStylePrimaryName(DETAIL_FIGURE_HEADER_RIGHT_STYLE);

		footer_ = new SimplePanel();
		footer_.setStylePrimaryName(DETAIL_FIGURE_FOOTER_STYLE);
		footerLeft_ = new SimplePanel();
		footerLeft_.setStylePrimaryName(DETAIL_FIGURE_FOOTER_LEFT_STYLE);
		footerRight_ = new SimplePanel();
		footerRight_.setStylePrimaryName(DETAIL_FIGURE_FOOTER_RIGHT_STYLE);

		left_ = new SimplePanel();
		left_.setStylePrimaryName(DETAIL_FIGURE_LEFT_STYLE);

		right_ = new SimplePanel();
		right_.setStylePrimaryName(DETAIL_FIGURE_RIGHT_STYLE);

		center_ = new VerticalPanel();
		center_.setStylePrimaryName(DETAIL_FIGURE_BODY_STYLE);
		center_.add(header_);
		add(center_, CENTER);

		VerticalPanel left = new VerticalPanel();
		left.add(headerLeft_);
		left.add(left_);
		left.add(footerLeft_);
		add(left, WEST);

		VerticalPanel right = new VerticalPanel();
		right.add(headerRight_);
		right.add(right_);
		right.add(footerRight_);
		add(right, EAST);

		center_.add(contentFigure_);

		footerLeftSpacer_ = new SimplePanel();
		footerLeftSpacer_
				.setStylePrimaryName(DETAIL_FIGURE_FOOTER_LEFT_SPACER_STYLE);
		footerRightSpacer_ = new SimplePanel();
		footerRightSpacer_
				.setStylePrimaryName(DETAIL_FIGURE_FOOTER_RIGHT_SPACER_STYLE);
		HorizontalPanel fcont = new HorizontalPanel();
		fcont.add(footerLeftSpacer_);
		fcont.add(footer_);
		fcont.add(footerRightSpacer_);
		center_.add(fcont);

		add(point_, SOUTH);
		setCellHorizontalAlignment(point_, DockPanel.ALIGN_CENTER);
	}

	public void addStyleDependentName(String styleSuffix) {
		center_.addStyleDependentName(styleSuffix);
		headerLeft_.addStyleDependentName(styleSuffix);
		headerRight_.addStyleDependentName(styleSuffix);
		header_.addStyleDependentName(styleSuffix);
		footer_.addStyleDependentName(styleSuffix);
		footerLeft_.addStyleDependentName(styleSuffix);
		footerRight_.addStyleDependentName(styleSuffix);
		left_.addStyleDependentName(styleSuffix);
		right_.addStyleDependentName(styleSuffix);
		footerLeftSpacer_.addStyleDependentName(styleSuffix);
		footerRightSpacer_.addStyleDependentName(styleSuffix);
		point_.addStyleDependentName(styleSuffix);
		super.addStyleDependentName(styleSuffix);
	}

	public void removeStyleDependentName(String styleSuffix) {
		center_.removeStyleDependentName(styleSuffix);
		headerLeft_.removeStyleDependentName(styleSuffix);
		headerRight_.removeStyleDependentName(styleSuffix);
		header_.removeStyleDependentName(styleSuffix);
		footer_.removeStyleDependentName(styleSuffix);
		footerLeft_.removeStyleDependentName(styleSuffix);
		footerRight_.removeStyleDependentName(styleSuffix);
		left_.removeStyleDependentName(styleSuffix);
		right_.removeStyleDependentName(styleSuffix);
		footerLeftSpacer_.removeStyleDependentName(styleSuffix);
		footerRightSpacer_.removeStyleDependentName(styleSuffix);
		point_.removeStyleDependentName(styleSuffix);
		super.removeStyleDependentName(styleSuffix);
	}

	public void setStylePrimaryName(String style) {
		center_.setStylePrimaryName(style);
		headerLeft_.setStylePrimaryName(style);
		headerRight_.setStylePrimaryName(style);
		header_.setStylePrimaryName(style);
		footer_.setStylePrimaryName(style);
		footerLeft_.setStylePrimaryName(style);
		footerRight_.setStylePrimaryName(style);
		left_.setStylePrimaryName(style);
		right_.setStylePrimaryName(style);
		footerLeftSpacer_.setStylePrimaryName(style);
		footerRightSpacer_.setStylePrimaryName(style);
		point_.setStylePrimaryName(style);
		super.setStylePrimaryName(style);
	}
	
	

	public void updateSize() {
		int h = (contentFigure_.getOffsetHeight() + header_.getOffsetHeight() + footer_
				.getOffsetHeight())
				- headerLeft_.getOffsetHeight() - footerLeft_.getOffsetHeight();
		left_.setHeight(h + "px");
		right_.setHeight(h + "px");
		if (contentFigure_.getOffsetWidth() > footer_.getOffsetWidth()) {
			int w = Util.half(contentFigure_.getOffsetWidth()
					- footer_.getOffsetWidth());
			footerLeftSpacer_.setWidth(w + "px");
			footerRightSpacer_.setWidth(w + "px");
		} else {
			footerLeftSpacer_.setWidth("0px");
			footerRightSpacer_.setWidth("0px");
		}
	}
}
