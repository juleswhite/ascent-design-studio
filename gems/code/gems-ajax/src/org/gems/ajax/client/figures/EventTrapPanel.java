/*******************************************************************************
 * Copyright (c) 2008 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.gems.ajax.client.figures;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class EventTrapPanel extends FocusPanel {

	public EventTrapPanel() {
		super();
	}

	public EventTrapPanel(Widget child) {
		super(child);
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);

		if(DOM.eventGetType(event) == Event.ONMOUSEDOWN)
			DOM.eventCancelBubble(event, true);
	}

}
