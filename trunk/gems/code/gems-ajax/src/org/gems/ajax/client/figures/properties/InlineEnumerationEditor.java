package org.gems.ajax.client.figures.properties;

import java.util.List;

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

public class InlineEnumerationEditor extends InlineEditor {

	public InlineEnumerationEditor(String[] vals, String curr) {
		super(new EnumerationPropertyEditor(vals),new TextPropertyViewer(curr));
	}
	public InlineEnumerationEditor(List<String> vals, String curr) {
		super(new EnumerationPropertyEditor(vals),new TextPropertyViewer(curr));
	}
}
