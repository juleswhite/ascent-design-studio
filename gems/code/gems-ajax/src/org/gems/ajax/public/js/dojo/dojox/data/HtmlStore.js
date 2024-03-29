if(!dojo._hasResource["dojox.data.HtmlStore"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.data.HtmlStore"] = true;
dojo.provide("dojox.data.HtmlStore");

dojo.require("dojox.data.dom");
dojo.require("dojo.data.util.simpleFetch");
dojo.require("dojo.data.util.filter");

dojo.declare("dojox.data.HtmlStore", null, {
	constructor: function(/*Object*/args){
		//	summary:
		//		Initializer for the HTML table store.  
		//	description:
		//		The HtmlStore can be created in one of two ways: a) by parsing an existing
		//		table or list DOM node on the current page or b) by referencing an external url and giving
		//		the id of the table or listin that page.  The remote url will be parsed as an html page.
		//
		//		The HTML table or list should be of the following form:
		//
		//		|	<table id="myTable">
		//		|		<thead>
		//		|			<tr>
		//		|				<th>Attribute1</th>
		//		|				<th>Attribute2</th>
		//		|			</tr>
		//		|		</thead>
		//		|		<tbody>
		//		|			<tr>
		//		|				<td>Value1.1</td>
		//		|				<td>Value1.2</td>
		//		|			</tr>
		//		|			<tr>
		//		|				<td>Value2.1</td>
		//		|				<td>Value2.2</td>
		//		|			</tr>
		//		|		</tbody>
		//		|	</table>
		//
		// -or-
		//
		//		|	<ul id="myUnorderedList">
		//		|		<li>Value.1</li>
		//		|		<li>Value.2</li>
		//		|	</ul>
		//
		// -or-
		//
		//		|	<ol id="myOrderedList">
		//		|		<li>Value.1</li>
		//		|		<li>Value.2</li>
		//		|	</ol>
		//
		//	args:
		//		An anonymous object to initialize properties.  It expects the following values:
		//		dataId:	The id of the HTML table to use.
		//		OR
		//		url:	The url of the remote page to load
		//		dataId:	The id of the table element in the remote page
		
		if(args.url){
			if(!args.dataId)
				throw new Error("dojo.data.HtmlStore: Cannot instantiate using url without an id!");
			this.url = args.url;
			this.dataId = args.dataId;
		}else{
			if(args.dataId){
				this._rootNode = dojo.byId(args.dataId);
				this.dataId = this._rootNode.id;
			}else{
				this._rootNode = dojo.byId(this.dataId);
			}
			this._indexItems();
		}
	},

	url: "",     // So the parser can instantiate the store via markup.
	dataId: "", // So the parser can instantiate the store via markup.
	
	_indexItems: function(){
		this._getHeadings();
		if (this._rootNode.rows){//tables
			if(this._rootNode.tBodies && this._rootNode.tBodies.length > 0) {
				this._rootNode = this._rootNode.tBodies[0];
			}
			var i;
			for(i=0; i<this._rootNode.rows.length; i++){
				this._rootNode.rows[i].store = this;
				this._rootNode.rows[i]._ident = i+1;
			}
		}else{//lists
			var c=1;
			for(i=0; i<this._rootNode.childNodes.length; i++){
				if(this._rootNode.childNodes[i].nodeType===1){
					this._rootNode.childNodes[i].store = this;
					this._rootNode.childNodes[i]._ident = c;
					c++;
				}
			}
		}
	},

	_getHeadings: function(){
		//	summary:
		//		Function to load the attribute names from the table header so that the 
		//		attributes (cells in a row), can have a reasonable name.
		//      For list items, returns single implicit heading, ["name"]
		this._headings = [];
		if(this._rootNode.tHead){
			dojo.forEach(this._rootNode.tHead.rows[0].cells, dojo.hitch(this, function(th){
				this._headings.push(dojox.data.dom.textContent(th));
			}));
		}else{
			this._headings = ["name"];
		}
	},
	
	_getAllItems: function(){
		//	summary:
		//		Function to return all rows in the table as an array of items.
		var items = [];
                var i;
		if(this._rootNode.rows){//table
			for(i=0; i<this._rootNode.rows.length; i++){
				items.push(this._rootNode.rows[i]);
			}
		}else{ //list
			for(i=0; i<this._rootNode.childNodes.length; i++){
				if (this._rootNode.childNodes[i].nodeType===1){
					items.push(this._rootNode.childNodes[i]);
				}
			}
		}
		return items; //array
	},
	
	_assertIsItem: function(/* item */ item){
		//	summary:
		//      This function tests whether the item passed in is indeed an item in the store.
		//	item: 
		//		The item to test for being contained by the store.
		if(!this.isItem(item)){ 
			throw new Error("dojo.data.HtmlStore: a function was passed an item argument that was not an item");
		}
	},

	_assertIsAttribute: function(/* String */ attribute){
		//	summary:
		//      This function tests whether the item passed in is indeed a valid 'attribute' like type for the store.
		//	attribute: 
		//		The attribute to test for being contained by the store.
		//
		//	returns:
		//		Returns the index (column) that the attribute resides in the row.
		if(typeof attribute !== "string"){ 
			throw new Error("dojo.data.HtmlStore: a function was passed an attribute argument that was not an attribute name string");
			return -1;
		}
		return dojo.indexOf(this._headings, attribute); //int
	},

/***************************************
     dojo.data.api.Read API
***************************************/
	
	getValue: function(	/* item */ item, 
						/* attribute-name-string */ attribute, 
						/* value? */ defaultValue){
		//	summary: 
		//      See dojo.data.api.Read.getValue()
		var values = this.getValues(item, attribute);
		return (values.length > 0)?values[0]:defaultValue; //Object || int || Boolean
	},

	getValues: function(/* item */ item, 
						/* attribute-name-string */ attribute){
		//	summary: 
		//		See dojo.data.api.Read.getValues()

		this._assertIsItem(item);
		var index = this._assertIsAttribute(attribute);

		if(index>-1){
			if (item.cells){
			  return [dojox.data.dom.textContent(item.cells[index])];
			}else{//return Value for lists
			    return [dojox.data.dom.textContent(item)];
			}
		}
		return []; //Array
	},

	getAttributes: function(/* item */ item){
		//	summary: 
		//		See dojo.data.api.Read.getAttributes()
		this._assertIsItem(item);
		var attributes = [];
		for(var i=0; i<this._headings.length; i++){
			if(this.hasAttribute(item, this._headings[i]))
				attributes.push(this._headings[i]);
		}
		return attributes; //Array
	},

	hasAttribute: function(	/* item */ item,
							/* attribute-name-string */ attribute){
		//	summary: 
		//		See dojo.data.api.Read.hasAttribute()
		return this.getValues(item, attribute).length > 0;
	},

	containsValue: function(/* item */ item, 
							/* attribute-name-string */ attribute, 
							/* anything */ value){
		//	summary: 
		//		See dojo.data.api.Read.containsValue()
		var regexp = undefined;
		if(typeof value === "string"){
		   regexp = dojo.data.util.filter.patternToRegExp(value, false);
		}
		return this._containsValue(item, attribute, value, regexp); //boolean.
	},

	_containsValue: function(	/* item */ item, 
								/* attribute-name-string */ attribute, 
								/* anything */ value,
								/* RegExp?*/ regexp){
		//	summary: 
		//		Internal function for looking at the values contained by the item.
		//	description: 
		//		Internal function for looking at the values contained by the item.  This 
		//		function allows for denoting if the comparison should be case sensitive for
		//		strings or not (for handling filtering cases where string case should not matter)
		//	
		//	item:
		//		The data item to examine for attribute values.
		//	attribute:
		//		The attribute to inspect.
		//	value:	
		//		The value to match.
		//	regexp:
		//		Optional regular expression generated off value if value was of string type to handle wildcarding.
		//		If present and attribute values are string, then it can be used for comparison instead of 'value'
		var values = this.getValues(item, attribute);
		for(var i = 0; i < values.length; ++i){
			var possibleValue = values[i];
			if(typeof possibleValue === "string" && regexp){
				return (possibleValue.match(regexp) !== null);
			}else{
				//Non-string matching.
				if(value === possibleValue){
					return true; // Boolean
				}
			}
		}
		return false; // Boolean
	},

	isItem: function(/* anything */ something){
		//	summary: 
		//		See dojo.data.api.Read.isItem()
		if(something && something.store && something.store === this){
			return true; //boolean
		}
		return false; //boolean
	},

	isItemLoaded: function(/* anything */ something){
		//	summary: 
		//		See dojo.data.api.Read.isItemLoaded()
		return this.isItem(something);
	},

	loadItem: function(/* Object */ keywordArgs){
		//	summary: 
		//		See dojo.data.api.Read.loadItem()
		this._assertIsItem(keywordArgs.item);
	},
	
	_fetchItems: function(request, fetchHandler, errorHandler){
		//	summary:
		//		Fetch items (XML elements) that match to a query
		//	description:
		//		If '_fetchUrl' is specified, it is used to load an XML document
		//		with a query string.
		//		Otherwise and if 'url' is specified, the XML document is
		//		loaded and list XML elements that match to a query (set of element
		//		names and their text attribute values that the items to contain).
		//		A wildcard, "*" can be used to query values to match all
		//		occurrences.
		//		If '_rootItem' is specified, it is used to fetch items.
		//	request:
		//		A request object
		//	fetchHandler:
		//		A function to call for fetched items
		//	errorHandler:
		//		A function to call on error
		
		if(this._rootNode){
			this._finishFetchItems(request, fetchHandler, errorHandler);
		}else{
			if(!this.url){
				this._rootNode = dojo.byId(this.dataId);
			}else{
				var getArgs = {
						url: this.url,
						handleAs: "text"
					};
				var self = this;
				var getHandler = dojo.xhrGet(getArgs);
				getHandler.addCallback(function(data){
					var findNode = function(node, id){
						if(node.id == id){
							return node; //object
						}
						if(node.childNodes){
							for(var i=0; i<node.childNodes.length; i++){
								var returnNode = findNode(node.childNodes[i], id);
								if(returnNode){
									return returnNode; //object
								}
							}
						}
						return null; //null
					}

					var d = document.createElement("div");
					d.innerHTML = data;
					self._rootNode = findNode(d, self.dataId);
					self._indexItems();
					self._finishFetchItems(request, fetchHandler, errorHandler);
				});
				getHandler.addErrback(function(error){
					errorHandler(error, request);
				});
			}
		}
	},
	
	_finishFetchItems: function(request, fetchHandler, errorHandler){
		//	summary:
		//		Internal function for processing the passed in request and locating the requested items.
		var items = null;
		var arrayOfAllItems = this._getAllItems();
		if(request.query){
			var ignoreCase = request.queryOptions ? request.queryOptions.ignoreCase : false; 
			items = [];

			//See if there are any string values that can be regexp parsed first to avoid multiple regexp gens on the
			//same value for each item examined.  Much more efficient.
			var regexpList = {};
			var key;
                        var value;
			for(key in request.query){
				value = request.query[key]+'';
				if(typeof value === "string"){
					regexpList[key] = dojo.data.util.filter.patternToRegExp(value, ignoreCase);
				}
			}

			for(var i = 0; i < arrayOfAllItems.length; ++i){
				var match = true;
				var candidateItem = arrayOfAllItems[i];
				for(key in request.query){
					value = request.query[key]+'';
					if (!this._containsValue(candidateItem, key, value, regexpList[key])){
						match = false;
					}
				}
				if(match){
					items.push(candidateItem);
				}
			}
			fetchHandler(items, request);
		}else{
			// We want a copy to pass back in case the parent wishes to sort the array.  We shouldn't allow resort 
			// of the internal list so that multiple callers can get listsand sort without affecting each other.
			if(arrayOfAllItems.length> 0){
				items = arrayOfAllItems.slice(0,arrayOfAllItems.length); 
			}
			fetchHandler(items, request);
		}
	},

	getFeatures: function(){
		//	summary: 
		//		See dojo.data.api.Read.getFeatures()
		return {
			'dojo.data.api.Read': true,
			'dojo.data.api.Identity': true
		};
	},
	
	close: function(/*dojo.data.api.Request || keywordArgs || null */ request){
		//	summary:
		//		See dojo.data.api.Read.close()
		// nothing to do here!
	},

	getLabel: function(/* item */ item){
		//	summary: 
		//		See dojo.data.api.Read.getLabel()
		if(this.isItem(item)){
			if(item.cells){
				return "Item #" + this.getIdentity(item);
			}else{
				return this.getValue(item,"name");
			}
		}
		return undefined;
	},

	getLabelAttributes: function(/* item */ item){
		//	summary: 
		//		See dojo.data.api.Read.getLabelAttributes()
		if(item.cells){
			return null;
		}else{
			return ["name"];
		}
	},

/***************************************
     dojo.data.api.Identity API
***************************************/

	getIdentity: function(/* item */ item){
		//	summary: 
		//		See dojo.data.api.Identity.getIdentity()
		this._assertIsItem(item);
		if(this.hasAttribute(item, "name")){
		 	return this.getValue(item,"name");
		}else{
			return item._ident;
		}
	},

	getIdentityAttributes: function(/* item */ item){
		 //	summary: 
		 //		See dojo.data.api.Identity.getIdentityAttributes()
		 //Identity isn't taken from a public attribute.
		 return null;
	},

	fetchItemByIdentity: function(keywordArgs){
		//	summary: 
		//		See dojo.data.api.Identity.fetchItemByIdentity()
		var identity = keywordArgs.identity;
		var self = this;
		var item = null;
		var scope = null;

		if(!this._rootNode){
			if(!this.url){
				this._rootNode = dojo.byId(this.dataId);
				this._indexItems();
				if(self._rootNode.rows){ //Table
					item = this._rootNode.rows[identity + 1];
				}else{ //Lists
					for(var i = 0; i < self._rootNode.childNodes.length; i++){
						if(self._rootNode.childNodes[i].nodeType === 1 && identity === dojox.data.dom.textContent(self._rootNode.childNodes[i])) {
								item = self._rootNode.childNodes[i];
						}
					}
				}
				if(keywordArgs.onItem){
					scope = keywordArgs.scope?keywordArgs.scope:dojo.global;
					keywordArgs.onItem.call(scope, item);
				}

			}else{
				var getArgs = {
						url: this.url,
						handleAs: "text"
					};
				var getHandler = dojo.xhrGet(getArgs);
				getHandler.addCallback(function(data){
					var findNode = function(node, id){
						if(node.id == id){
							return node; //object
						}
						if(node.childNodes){
							for(var i=0; i<node.childNodes.length; i++){
								var returnNode = findNode(node.childNodes[i], id);
								if(returnNode){
									return returnNode; //object
								}
							}
						}
						return null; //null
					}
					var d = document.createElement("div");
					d.innerHTML = data;
					self._rootNode = findNode(d, self.dataId);
					self._indexItems();
					if(self._rootNode.rows && identity <= self._rootNode.rows.length){ //Table
						item = self._rootNode.rows[identity-1];
					}else{ //List
						for(var i = 0; i < self._rootNode.childNodes.length; i++){
							if(self._rootNode.childNodes[i].nodeType === 1 && identity === dojox.data.dom.textContent(self._rootNode.childNodes[i])) {
									item = self._rootNode.childNodes[i];
									break;
							}
						}
					}
					if(keywordArgs.onItem){
						scope = keywordArgs.scope?keywordArgs.scope:dojo.global;
						keywordArgs.onItem.call(scope, item);
					}
				});
				getHandler.addErrback(function(error){
					if(keywordArgs.onError){
						scope = keywordArgs.scope?keywordArgs.scope:dojo.global;
						keywordArgs.onError.call(scope, error);

					}
				});
			}
		}else{
			if(this._rootNode.rows[identity+1]){
				item = this._rootNode.rows[identity+1];
				if(keywordArgs.onItem){
					scope = keywordArgs.scope?keywordArgs.scope:dojo.global;
					keywordArgs.onItem.call(scope, item);
				}
			}
		}
	}
});
dojo.extend(dojox.data.HtmlStore,dojo.data.util.simpleFetch);

}
