if(!dojo._hasResource["dojox.wire.ml.Service"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.wire.ml.Service"] = true;
dojo.provide("dojox.wire.ml.Service");
dojo.provide("dojox.wire.ml.RestHandler");
dojo.provide("dojox.wire.ml.XmlHandler");
dojo.provide("dojox.wire.ml.JsonHandler");

dojo.require("dijit._Widget");
dojo.require("dojox.data.dom");
dojo.require("dojox.wire._base");
dojo.require("dojox.wire.ml.util");

dojo.declare("dojox.wire.ml.Service", dijit._Widget, {
	//	summary:
	//		A widget for a service
	//	description:
	//		This widget represents a service defined by a service description
	//		specified with 'url' attribute.
	//		If 'serviceType' and 'serviceUrl' attributes are specified, 'url'
	//		attribute can be omitted.
	//	url:
	//		A URL to a service description
	//	serviceUrl:
	//		A URL to a service
	//	serviceType:
	//		A service type
	//	handlerClass:
	//		A service handler class name
	url: "",
	serviceUrl: "",
	serviceType: "",
	handlerClass: "",
	preventCache: true,

	postCreate: function(){
		//	summary:
		//		Call _createHandler()
		//	description:
		//		See _createHandler().
		this.handler = this._createHandler();
	},

	_handlerClasses: {
		"TEXT": "dojox.wire.ml.RestHandler",
		"XML": "dojox.wire.ml.XmlHandler",
		"JSON": "dojox.wire.ml.JsonHandler",
		"JSON-RPC": "dojo.rpc.JsonService"
	},

	_createHandler: function(){
		//	summary:
		//		Create a service handler
		//	desription:
		//		A service handler class is determined by:
		//		1. 'handlerClass' attribute
		//		2. 'serviceType' attribute
		//		3. 'serviceType' property in a service description
		//	returns:
		//		A service handler
		if(this.url){
			var self = this;
			var d = dojo.xhrGet({
				url: this.url,
				handleAs: "json",
				sync: true
			});
			d.addCallback(function(result){
				self.smd = result;
			});
			if(this.smd && !this.serviceUrl){
				this.serviceUrl = (this.smd.serviceUrl || this.smd.serviceURL);
			}
		}
		var handlerClass = undefined;
		if(this.handlerClass){
			handlerClass = dojox.wire._getClass(this.handlerClass);
		}else if(this.serviceType){
			handlerClass = this._handlerClasses[this.serviceType];
			if(handlerClass && dojo.isString(handlerClass)){
				handlerClass = dojox.wire._getClass(handlerClass);
				this._handlerClasses[this.serviceType] = handlerClass;
			}
		}else if(this.smd && this.smd.serviceType){
			handlerClass = this._handlerClasses[this.smd.serviceType];
			if(handlerClass && dojo.isString(handlerClass)){
				handlerClass = dojox.wire._getClass(handlerClass);
				this._handlerClasses[this.smd.serviceType] = handlerClass;
			}
		}
		if(!handlerClass){
			return null; //null
		}
		return new handlerClass(); //Object
	},

	callMethod: function(method, parameters){
		//	summary:
		//		Call a service method with parameters
		//	method:
		//		A method name
		//	parameters:
		//		An array parameters
		var deferred = new dojo.Deferred();
		this.handler.bind(method, parameters, deferred, this.serviceUrl);
		return deferred;
	}
});

dojo.declare("dojox.wire.ml.RestHandler", null, {
	//	summary:
	//		A REST service handler
	//	description:
	//		This class serves as a base REST service.
	//		Sub-classes may override _getContent() and _getResult() to handle
	//		specific content types.
	contentType: "text/plain",
	handleAs: "text",

	bind: function(method, parameters, deferred, url){
		//	summary:
		//		Call a service method with parameters.
		//	description:
		//		A service is called with a URL generated by _getUrl() and
		//		an HTTP method specified with 'method'.
		//		For "POST" and "PUT", a content is generated by _getContent().
		//		When data is loaded, _getResult() is used to pass the result to
		//		Deferred.callback().
		//	method:
		//		A method name
		//	parameters:
		//		An array of parameters
		//	deferred:
		//		'Deferred'
		//	url:
		//		A URL for the method
		method = method.toUpperCase();
		var self = this;
		var args = {
			url: this._getUrl(method, parameters, url),
			contentType: this.contentType,
			handleAs: this.handleAs,
			headers: this.headers,
			preventCache: this.preventCache
		};
		var d = null;
		if(method == "POST"){
			args.postData = this._getContent(method, parameters);
			d = dojo.rawXhrPost(args);
		}else if(method == "PUT"){
			args.putData = this._getContent(method, parameters);
			d = dojo.rawXhrPut(args);
		}else if(method == "DELETE"){
			d = dojo.xhrDelete(args);
		}else{ // "GET"
			d = dojo.xhrGet(args);
		}
		d.addCallbacks(function(result){
			deferred.callback(self._getResult(result));
		}, function(error){
			deferred.errback(error);
		});
	},

	_getUrl: function(/*String*/method, /*Array*/parameters, /*String*/url){
		//	summary:
		//		Generate a URL
		//	description:
		//		If 'method' is "GET" or "DELETE", a query string is generated
		//		from a query object specified to the first parameter in
		//		'parameters' and appended to 'url'.
		//		If 'url' contains variable seguments ("{parameter_name}"),
		//		they are replaced with corresponding parameter values, instead.
		//	method:
		//		A method name
		//	parameters:
		//		An array of parameters
		//	url:
		//		A base URL
		//	returns:
		//		A URL
		var query;
		if(method == "GET" || method == "DELETE"){
			if(parameters.length > 0){
				query = parameters[0];
			}
		}else{ // "POST" || "PUT"
			if(parameters.length > 1){
				query = parameters[1];
			}
		}
		if(query){
			var queryString = "";
			for(var name in query){
				var value = query[name];
				if(value){
					value = encodeURIComponent(value);
					var variable = "{" + name + "}";
					var index = url.indexOf(variable);
					if(index >= 0){ // encode in path
						url = url.substring(0, index) + value + url.substring(index + variable.length);
					}else{ // encode as query string
						if(queryString){
							queryString += "&";
						}
						queryString += (name + "=" + value);
					}
				}
			}
			if(queryString){
				url += "?" + queryString;
			}
		}
		return url; //String
	},

	_getContent: function(/*String*/method, /*Array*/parameters){
		//	summary:
		//		Generate a request content
		//	description:
		//		If 'method' is "POST" or "PUT", the first parameter in
		//		'parameters' is returned.
		//	method:
		//		A method name
		//	parameters:
		//		An array of parameters
		//	returns:
		//		A request content
		if(method == "POST" || method == "PUT"){
			return (parameters ? parameters[0] : null); //anything
		}else{
			return null; //null
		}
	},

	_getResult: function(/*anything*/data){
		//	summary:
		//		Extract a result
		//	description:
		//		A response data is returned as is.
		//	data:
		//		A response data returned by a service
		//	returns:
		//		A result object
		return data; //anything
	}
});

dojo.declare("dojox.wire.ml.XmlHandler", dojox.wire.ml.RestHandler, {
	//	summary:
	//		A REST service handler for XML
	//	description:
	//		This class provides XML handling for a REST service.
	contentType: "text/xml",
	handleAs: "xml",

	_getContent: function(/*String*/method, /*Array*/parameters){
		//	description:
		//		If 'method' is "POST" or "PUT", the first parameter in
		//		'parameters' is used to generate an XML content.
		//	method:
		//		A method name
		//	parameters:
		//		An array of parameters
		//	returns:
		//		A request content
		var content = null;
		if(method == "POST" || method == "PUT"){
			var p = parameters[0];
			if(p){
				if(dojo.isString(p)){
					content = p;
				}else{
					var element = p;
					if(element instanceof dojox.wire.ml.XmlElement){
						element = element.element;
					}else if(element.nodeType === 9 /* DOCUMENT_NODE */){
						element = element.documentElement;
					}
					var declaration = "<?xml version=\"1.0\"?>"; // TODO: encoding?
					content = declaration + dojox.data.dom.innerXML(element);
				}
			}
		}
		return content;
	},

	_getResult: function(/*Document*/data){
		//	summary:
		//		Extract a result
		//	description:
		//		A response data (XML Document) is returned wrapped with
		//		XmlElement.
		//	data:
		//		A response data returned by a service
		//	returns:
		//		A result object
		if(data){
			data = new dojox.wire.ml.XmlElement(data);
		}
		return data;
	}
});

dojo.declare("dojox.wire.ml.JsonHandler", dojox.wire.ml.RestHandler, {
	//	summary:
	//		A REST service handler for JSON
	//	description:
	//		This class provides JSON handling for a REST service.
	contentType: "text/json",
	handleAs: "json",
	headers: {"Accept": "*/json"},

	_getContent: function(/*String*/method, /*Array*/parameters){
		//	summary:
		//		Generate a request content
		//	description:
		//		If 'method' is "POST" or "PUT", the first parameter in
		//		'parameter' is used to generate a JSON content.
		//	method:
		//		A method name
		//	parameters:
		//		An array of parameters
		//	returns:
		//		A request content
		var content = null;
		if(method == "POST" || method == "PUT"){
			var p = (parameters ? parameters[0] : undefined);
			if(p){
				if(dojo.isString(p)){
					content = p;
				}else{
					content = dojo.toJson(p);
				}
			}
		}
		return content; //String
	}
});

}
