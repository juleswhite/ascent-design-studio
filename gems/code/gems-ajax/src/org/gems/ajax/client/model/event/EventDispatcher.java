package org.gems.ajax.client.model.event;

import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.model.ModelElement;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class EventDispatcher {

	private static final EventDispatcher dispatcher_ = new EventDispatcher();

	private boolean suspendEvents_ = false;
	private boolean recordEvents_ = false;

	private List<ModelResourceListener> preDispatchResourceListeners_ = new ArrayList<ModelResourceListener>();
	private List<ModelResourceListener> postDispatchResourceListeners_ = new ArrayList<ModelResourceListener>();
	
	private List<ModelListener> preDispatchListeners_ = new ArrayList<ModelListener>();
	private List<ModelListener> postDispatchListeners_ = new ArrayList<ModelListener>();

	private List<ModelEvent> recordQueue_;

	public static EventDispatcher get() {
		return dispatcher_;
	}
	
	private EventDispatcher() {}

	public void dispatch(ModelEvent evt, List<ModelListener> listeners) {
		dispatch(null, evt, listeners);
	}

	public void dispatch(ModelElement producer, ModelEvent evt,
			List<ModelListener> listeners) {
		if (!suspendEvents_) {
			for (ModelListener pre : preDispatchListeners_)
				evt.dispatchImpl(pre);
			
			if(producer != null && producer.getModelResource() != null)
				for (ModelResourceListener pre : preDispatchResourceListeners_)
					pre.resourceChanged(producer.getModelResource(), producer, evt);

			if (listeners != null)
				evt.dispatch(listeners);

			if (recordEvents_ && !evt.vetoed() && recordQueue_ != null)
				recordQueue_.add(evt);

			for (ModelListener post : postDispatchListeners_)
				evt.dispatchImpl(post);
			
			if(producer != null && producer.getModelResource() != null)
				for (ModelResourceListener post : postDispatchResourceListeners_)
					post.resourceChanged(producer.getModelResource(), producer, evt);
		}
	}

	public void dispatch(ModelEvent evt) {
		dispatch(null, evt, null);
	}

	public List<ModelListener> getPreDispatchListeners() {
		return preDispatchListeners_;
	}

	public void setPreDispatchListeners(List<ModelListener> preDispatchListeners) {
		preDispatchListeners_ = preDispatchListeners;
	}

	public List<ModelListener> getPostDispatchListeners() {
		return postDispatchListeners_;
	}

	public void setPostDispatchListeners(
			List<ModelListener> postDispatchListeners) {
		postDispatchListeners_ = postDispatchListeners;
	}

	public boolean eventsSuspended() {
		return suspendEvents_;
	}

	public void disableEvents() {
		suspendEvents_ = true;
	}
	
	public void enableEvents() {
		suspendEvents_ = false;
	}

	public boolean recordingEvents() {
		return recordEvents_;
	}
	
	public void stopRecordingEvents(){
		recordEvents_ = false;
	}

	public void startRecordingEvents() {
		if (recordQueue_ != null)
			recordQueue_.clear();

		recordQueue_ = new ArrayList<ModelEvent>();
		recordEvents_ = true;
	}

	public List<ModelEvent> getRecordedEvents() {
		return recordQueue_;
	}

}
