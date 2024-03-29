if(!dojo._hasResource["dojox.analytics.plugins.consoleMessages"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.analytics.plugins.consoleMessages"] = true;
dojo.provide("dojox.analytics.plugins.consoleMessages");

dojox.analytics.plugins.consoleMessages = new (function(){
	// summary:
	//	plugin to have analyitcs return the base info dojo collects
	this.addData = dojo.hitch(dojox.analytics, "addData", "consoleMessages");

	var lvls = dojo.config["consoleLogFuncs"] || ["error", "warn", "info", "rlog"];
	if(!console){
		console={};
	}

	for(var i=0; i<lvls.length;i++){
		if (console[lvls[i]]){
			dojo.connect(console, lvls[i], dojo.hitch(this, "addData", lvls[i]));
		}else{
			console[lvls[i]] = dojo.hitch(this, "addData", lvls[i]);	
		}
	}
})();

}
