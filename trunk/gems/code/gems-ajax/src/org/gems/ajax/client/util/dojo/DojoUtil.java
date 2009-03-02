package org.gems.ajax.client.util.dojo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/

public class DojoUtil {

	private static final String DOJOX_COMETD_TIMESTAMP = "dojox.cometd.timestamp";
	private static final String DOJOX_COMETD = "dojox.cometd";

	static {
		enableDebugging();
	}

	public static final String DOJO_FX = "dojo.fx";
	public static final String DOJO_NODELIST_FX = "dojo.NodeList-fx";
	public static final String DOJO_STYLE = "dojo.style";
	public static final String DOJO_EVENT = "dojo.event";
	public static final String DOJO_MOVER = "dojo.dnd.Mover";
	public static final String DOJO_MOVEABLE = "dojo.dnd.Moveable";
	public static final String DOJO_DND = "dojo.dnd";
	public static final String DOJO_MOVE = "dojo.dnd.move";
	public static final String DOJO_DND_SOURCE = "dojo.dnd.Source";
	public static final String DOJO_DND_TARGET = "dojo.dnd.target";
	public static final String DOJO_RESIZE_HANDLE = "dojox.layout.ResizeHandle";
	public static final String DOJO_PARSER = "dojo.parser";

	private static HashSet<String> loadedFeatures_ = new HashSet<String>();

	public static native void enableDebugging()/*-{
						$wnd.djConfig = {isDebug: false};
					}-*/;

	public static void wipeOut(Element el) {
		require(DOJO_FX);
		wipeOutImpl(el);
	}

	public static void wipeIn(Element el) {
		require(DOJO_FX);
		wipeInImpl(el);
	}

	private static native void wipeOutImpl(Element el)/*-{
						var animArgs = {
							node: el,
				    	};
					    $wnd.dojo.fx.wipeOut(animArgs).play();
					}-*/;

	private static native void wipeInImpl(Element el)/*-{
						var animArgs = {
							node: el,
						};
				    	$wnd.dojo.fx.wipeIn(animArgs).play();
					}-*/;

	public static native Object createfadeOut(Element el, int dur, int dly)/*-{
						var animArgs = {
							node: el,
							duration: dur, // ms to run animation
							delay: dly // ms to stall before playing
						};
						return $wnd.dojo.fadeOut(animArgs);
					}-*/;

	public static native Object createfadeIn(Element el, int dur, int dly)/*-{
						var animArgs = {
							node: el,
							duration: dur, // ms to run animation
							delay: dly // ms to stall before playing
						};
						return $wnd.dojo.fadeIn(animArgs);
					}-*/;

	public static native void play(Object o)/*-{
						o.play();
					}-*/;

	public static Object chain(Object a1, Object a2) {
		require(DOJO_FX);
		return chainImpl(a1, a2);
	}

	private static native Object chainImpl(Object a1, Object a2)/*-{
						return $wnd.dojo.fx.chain([a1,a2]);
					}-*/;

	public static native void fadeOut(Element el, int dur, int dly)/*-{
						var animArgs = {
							node: el,
							duration: dur, // ms to run animation
							delay: dly // ms to stall before playing
						};
						$wnd.dojo.fadeOut(animArgs).play();
					}-*/;

	public static native void fadeIn(Element el, int dur, int dly)/*-{
						var animArgs = {
							node: el,
							duration: dur, // ms to run animation
							delay: dly // ms to stall before playing
						};
						$wnd.dojo.fadeIn(animArgs).play();
					}-*/;

	public static void makeMoveable(Element el) {
		require(DOJO_MOVE);
		require(DOJO_MOVEABLE);
		require(DOJO_MOVER);
		makeMoveableImpl(el);
	}
	
	public static void makeMoveable(Element el, MoveListener l) {
		require(DOJO_MOVE);
		require(DOJO_MOVEABLE);
		require(DOJO_MOVER);
		makeMoveableImpl(el,l);
	}

	public static native void makeMoveableImpl(Element el)/*-{
					new $wnd.dojo.dnd.Moveable(el);
					}-*/;

	public static void makeResizeable(Element e){
		require(DOJO_NODELIST_FX);
		makeResizeableImpl(e);
	}
	
	public static void connectToCometdHost(String host){
		require(DOJOX_COMETD);
		require(DOJOX_COMETD_TIMESTAMP);
		connectToCometdHostImpl(host);
	}
	
	public static native void connectToCometdHostImpl(String host)/*-{
	    $wnd.dojox.cometd.init(host);
	}-*/;
	
	public static native void subscribeToChannel(String channel, CometCallback callback)/*-{ 
	
	    recvr = function(data){
	       msg = data.data;
	       callback.@org.gems.ajax.client.util.dojo.CometCallback::recv(Lorg/gems/ajax/client/util/dojo/CometMessage;)(msg);
	    }
	    
        $wnd.dojox.cometd.subscribe("/model",recvr);
     }-*/;
	
	
	
	public static void publishToChannel(String channel, Map<String,String> data){
		CometMessage msg = newCometMsg();
		for(String key : data.keySet()){
			msg.put(key, data.get(key));
		}
		publishToChannel(channel, msg);
	}
	
	public static void publishToChannel(String channel, String data){
		CometMessage msg = newCometMsg();
		msg.put("msg", data);
		publishToChannel(channel, msg);
	}
	
	public static native void publishToChannel(String channel, CometMessage msg)/*-{   
        $wnd.dojox.cometd.publish(channel,msg);
    }-*/;
	
	public static native CometMessage newCometMsg()/*-{   
        return {};
    }-*/;
	
	public static native String getCometClientId()/*-{
		return $wnd.dojox.cometd.clientId;
	}-*/;
	
	public static native void makeResizeableImpl(Element el)/*-{
			var args = {targetContainer:el};
			new $wnd.dojox.layout.ResizeHandle(args);
	}-*/;
	
	public static native void makeMoveableImpl(Element el, MoveListener l)/*-{
			var m1 = new $wnd.dojo.dnd.Moveable(el);
			$wnd.dojo.connect(m1, "onMoved", function(mover){
	      		l.@org.gems.ajax.client.util.dojo.MoveListener::onMove()();
	    	});
	    	$wnd.dojo.connect(m1, "onMoveStart", function(mover){
	      		l.@org.gems.ajax.client.util.dojo.MoveListener::onMoveStart()();
	    	});
	    	$wnd.dojo.connect(m1, "onMoveStop", function(mover){
	      		l.@org.gems.ajax.client.util.dojo.MoveListener::onMoveStop()();
	    	});
		}-*/;

	public static native Element byId(String id)/*-{
				    	return $wnd.dojo.byId(id);  
				 	}-*/;
	
	public static void createDropTarget(Element el, String[] types){
		require(DOJO_DND_SOURCE);
		createDropTargetImpl(el, types);
	}
	
	public static void createDragSource(Element el, String[] types){
		require(DOJO_DND_SOURCE);
		createDragSourceImpl(DOM.getElementAttribute(el, "id"), types);
	}
	
	public static native void createDropTargetImpl(Element el, String[] types)/*-{
		new $wnd.dojo.dnd.Target(el, {accept:["foo"]});
	}-*/;
	
	public static native void createDragSourceImpl(String el, String[] types)/*-{
		new $wnd.dojo.dnd.Source(el, {accept: ["foo"]});
		$wnd.dojo.subscribe("/dnd/source/over", function(source){
  		   
		});

	}-*/;
	
	public static void loadDojoParser(){
		require(DOJO_PARSER);
	}

	public static boolean require(String dojofeature) {
		if (!loadedFeatures_.contains(dojofeature)) {
			if (requireImpl(dojofeature)) {
				loadedFeatures_.add(dojofeature);
			} else {
				return false;
			}
		}
		return true;
	}
	
	public static native void connect(Element e, DomChangeListener l)/*-{
		$wnd.dojo.connect(e,'onattrmodified',function(event){
	      		l.@org.gems.ajax.client.util.dojo.DomChangeListener::onAttributeChanged()();
	    	});
	}-*/;

	private static native boolean requireImpl(String dojofeature)/*-{
				      try {
				         $wnd.dojo.require(dojofeature);
				      } catch (exception) {        
				         return false;
				      }
				      	return true;      
				      }-*/;

}
