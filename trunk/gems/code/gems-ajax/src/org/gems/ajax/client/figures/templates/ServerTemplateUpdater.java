package org.gems.ajax.client.figures.templates;


import com.google.gwt.user.client.rpc.AsyncCallback;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ServerTemplateUpdater implements TemplateUpdater {

	private class TemplateUpdateCallbackAsync implements AsyncCallback<String> {
		private TemplateUpdateCallback callback_;

		public TemplateUpdateCallback getCallback() {
			return callback_;
		}

		public void setCallback(TemplateUpdateCallback callback) {
			callback_ = callback;
		}

		public void onFailure(Throwable caught) {
		}

		public void onSuccess(String result) {
			callback_.setTemplate(result);
		}
	}

	private String id_;

	private TemplateManagerAsync manager_;

	public ServerTemplateUpdater() {
		super();
	}

	public ServerTemplateUpdater(String id, TemplateManagerAsync manager) {
		super();
		id_ = id;
		manager_ = manager;
	}

	public void updateTemplate(TemplateData data,
			TemplateUpdateCallback callback) {
		TemplateUpdateCallbackAsync async = new TemplateUpdateCallbackAsync();
		async.setCallback(callback);

		manager_.updateTemplate(id_, data, async);
	}

	public String getId() {
		return id_;
	}

	public void setId(String id) {
		id_ = id;
	}

	public TemplateManagerAsync getManager() {
		return manager_;
	}

	public void setManager(TemplateManagerAsync manager) {
		manager_ = manager;
	}
}
