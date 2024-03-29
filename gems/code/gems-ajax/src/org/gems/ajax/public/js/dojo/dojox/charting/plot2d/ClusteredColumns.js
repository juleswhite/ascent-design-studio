if(!dojo._hasResource["dojox.charting.plot2d.ClusteredColumns"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.charting.plot2d.ClusteredColumns"] = true;
dojo.provide("dojox.charting.plot2d.ClusteredColumns");

dojo.require("dojox.charting.plot2d.common");
dojo.require("dojox.charting.plot2d.Columns");

dojo.require("dojox.lang.functional");
dojo.require("dojox.lang.functional.reversed");

(function(){
	var df = dojox.lang.functional, dc = dojox.charting.plot2d.common,
		purgeGroup = df.lambda("item.purgeGroup()");

	dojo.declare("dojox.charting.plot2d.ClusteredColumns", dojox.charting.plot2d.Columns, {
		render: function(dim, offsets){
			if(this.dirty){
				dojo.forEach(this.series, purgeGroup);
				this.cleanGroup();
				var s = this.group;
				df.forEachRev(this.series, function(item){ item.cleanGroup(s); });
			}
			var t = this.chart.theme, color, stroke, fill, f,
				gap = this.opt.gap < this._hScaler.scale / 3 ? this.opt.gap : 0,
				thickness = (this._hScaler.scale - 2 * gap) / this.series.length;
			for(var i = 0; i < this.series.length; ++i){
				var run = this.series[i];
				if(!this.dirty && !run.dirty){ continue; }
				run.cleanGroup();
				var s = run.group;
				if(!run.fill || !run.stroke){
					// need autogenerated color
					color = run.dyn.color = new dojo.Color(t.next("color"));
				}
				stroke = run.stroke ? run.stroke : dc.augmentStroke(t.series.stroke, color);
				fill = run.fill ? run.fill : dc.augmentFill(t.series.fill, color);
				var baseline = Math.max(0, this._vScaler.bounds.lower),
					xoff = offsets.l + this._hScaler.scale * (0.5 - this._hScaler.bounds.lower) + gap + thickness * i,
					yoff = dim.height - offsets.b - this._vScaler.scale * (baseline - this._vScaler.bounds.lower);
				for(var j = 0; j < run.data.length; ++j){
					var v = run.data[j],
						width  = thickness,
						height = this._vScaler.scale * (v - baseline),
						h = Math.abs(height);
					if(width >= 1 && h >= 1){
						var shape = s.createRect({
							x: xoff + this._hScaler.scale * j,
							y: yoff - (height < 0 ? 0 : height),
							width: width, height: h
						}).setFill(fill).setStroke(stroke);
						run.dyn.fill   = shape.getFill();
						run.dyn.stroke = shape.getStroke();
					}
				}
				run.dirty = false;
			}
			this.dirty = false;
			return this;
		}
	});
})();

}
