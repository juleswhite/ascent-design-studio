if(!dojo._hasResource["dojox.sketch._Plugin"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.sketch._Plugin"] = true;
dojo.provide("dojox.sketch._Plugin");
//dojo.require("dojox.sketch");
dojo.require("dijit.form.Button");

dojo.declare("dojox.sketch._Plugin", null, {
	// summary
	//		This represents a "plugin" to the dojox.sketch.Figure, which is basically
	//		a single button on the Toolbar and some associated code
	constructor: function(/*Object?*/args){
		if(args){
			dojo.mixin(this, args);
		}
		this._connects=[];
	},

	figure: null,
	iconClassPrefix: "dojoxSketchIcon",
	itemGroup: 'toolsGroup',
	button: null,
	queryCommand: null,
	shape: "",
	useDefaultCommand: true,
	buttonClass: dijit.form.ToggleButton,
	_initButton: function(){
		if(this.shape.length){
			//TODO: i18n
//			var label = dojox.sketch.shapes[this.shape];
			var className = this.iconClassPrefix+" "+this.iconClassPrefix + this.shape.charAt(0).toUpperCase() + this.shape.substr(1);
			if(!this.button){
				var props = {
					label: this.shape,
					showLabel: false,
					iconClass: className,
					dropDown: this.dropDown,
					tabIndex: "-1"
				};
				this.button = new this.buttonClass(props);
				this.connect(this.button,'onClick','activate');
			}
		}
	},
	attr: function(name,/*?*/value){
		if(arguments.length>1){
			this.button.setAttribute(name,value);
		}else{
			this.button.getAttribute(name);
		}
	},
	onActivate: function(){},
	activate: function(/*?*/e){
		this.onActivate();
		this.figure.setTool(this);
		this.attr('checked',true);
	},
	onMouseDown: function(e){},
	onMouseMove: function(e){},
	onMouseUp: function(e){},
	destroy: function(f){
		dojo.forEach(this._connects,dojo.disconnect);
	},
	connect: function(o,f,tf){
		this._connects.push(dojo.connect(o,f,this,tf));
	},
	setFigure: function(/*Widget*/figure){
		// FIXME: detatch from previous figure!!
		this.figure = figure;

		// FIXME: prevent creating this if we don't need to (i.e., figure can't handle our command)
		this._initButton();
	},
	setToolbar: function(/*Widget*/toolbar){
		if(this.button){
			toolbar.addChild(this.button);
		}
		if(this.itemGroup){
			toolbar.addGroupItem(this,this.itemGroup);
		}
	}
});

}
