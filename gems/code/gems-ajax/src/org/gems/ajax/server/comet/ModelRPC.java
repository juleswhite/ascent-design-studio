package org.gems.ajax.server.comet;

import java.util.List;
import java.util.Map;

import org.cometd.Bayeux;
import org.cometd.Channel;
import org.cometd.Client;
import org.cometd.Message;
import org.gems.ajax.client.model.resources.ModelResource;
import org.mortbay.cometd.BayeuxService;

/******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ****************************************************************************/
public class ModelRPC extends BayeuxService {

	private static ModelRPC instance_;
	
	public static ModelRPC getInstance(){
		return instance_;
	}
	
	public static ModelRPC launch(Bayeux b){
		if(instance_ == null){
			instance_ = new ModelRPC(b);
		}
		return instance_;
	}
	
	private ModelRPC(Bayeux bayeux) {
		super(bayeux, "model");
		subscribe("/model", "modelEvent");
		subscribe("/meta/subscribe", "monitorSubscribe");
		subscribe("/meta/unsubscribe", "monitorUnsubscribe");
		subscribe("/meta/*", "monitorMeta");
	}

	public void modelEvent(Client client, String channel,
			Map<String, Object> data, String messageId) {
	}

	protected void exception(Client fromClient, Client toClient,
			Map<String, Object> msg, Throwable th) {
		th.printStackTrace();
		super.exception(fromClient, toClient, msg, th);
	}

	public void monitorSubscribe(Client client, Message message) {
//		String clientid = client.getId();
		String channelid = "" + message.get(Bayeux.SUBSCRIPTION_FIELD);
		
		Channel channel = getBayeux().getChannel(channelid, false);
		
		if (channel != null) {
			channel.subscribe(client);
		}
	}

	public void monitorUnsubscribe(Client client, Message message) {
//		String clientid = client.getId();
//		String channel = "" + message.get(Bayeux.SUBSCRIPTION_FIELD);
	}

	public void monitorMeta(Client client, Message message) {
	}
	
	public Channel getModelChannel(){
		return null;
	}
	
	public List<String> getAllModelingClients(){
		return null;
	}
	
	public List<String> getClientsOfModel(ModelResource res){
		return null;
	}
	
	public void sendMessageToAllClients(Map<String,String> msg){
		
	}
	
	public void sendMessageToClients(Map<String,String> msg, List<String> clients){
		
	}
	
	public void sendMessageToClientsOfModel(Map<String,String> msg, ModelResource res){
		
	}
	
	public void sendMessageToChannel(Map<String,String> msg, Channel c){
		
	}
}
