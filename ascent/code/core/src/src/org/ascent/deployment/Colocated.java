/**************************************************************************
 * Copyright 2008 Jules White                                              *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/

package org.ascent.deployment;

import java.util.List;

public class Colocated extends ColocationConstraint {

	public Colocated(Component source, List<Component> targets) {
		super(source, targets);
	}

	public Colocated(Component source, Component target) {
		super(source, target);
	}

	public Colocated(Component source) {
		super(source);
	}

	public boolean isEnforced(Node sh, Node th) {
		return sh == th;
	}

}