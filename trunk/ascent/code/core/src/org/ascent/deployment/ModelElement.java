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

import org.ascent.HasSize;

public class ModelElement implements Comparable<ModelElement>, HasSize {
	protected int id_;
	protected String label_;
	protected int[] resources_;

	public ModelElement(int id, String label, int[] resources) {
		super();
		id_ = id;
		label_ = label;
		resources_ = resources;
	}

	public int compareTo(ModelElement o) {
		return id_ - o.id_;
	}

	public int getId() {
		return id_;
	}

	public void setId(int id) {
		id_ = id;
	}

	public String getLabel() {
		return label_;
	}

	public void setLabel(String label) {
		label_ = label;
	}

	public int[] getResources() {
		return resources_;
	}

	public void setResources(int[] resources) {
		resources_ = resources;
	}

	public int[] getSize() {
		return getResources();
	}

	public String toString() {
		return label_
				+ " ("
				+ id_
				+ ") "
				+ DeploymentWithNetworkMinimizationConfig
						.toString(resources_);
	}
}
