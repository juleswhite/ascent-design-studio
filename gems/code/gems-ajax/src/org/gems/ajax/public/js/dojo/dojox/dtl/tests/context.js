if(!dojo._hasResource["dojox.dtl.tests.context"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.dtl.tests.context"] = true;
dojo.provide("dojox.dtl.tests.context");

dojo.require("dojox.dtl");
dojo.require("dojox.dtl.Context");

doh.register("dojox.dtl.context", 
	[
		function test_context_creation(t){
			var context = new dojox.dtl.Context({ foo: "foo", bar: "bar" });
			t.is("foo", context.foo);
			t.is("bar", context.bar);
		},
		function test_context_push(t){
			var context = new dojox.dtl.Context({ foo: "foo", bar: "bar" });
			context.push();
			for(var key in context._dicts[0]){
				t.t(key == "foo" || key == "bar");
			}
		},
		function test_context_pop(t){
			var context = new dojox.dtl.Context({ foo: "foo", bar: "bar" });
			context.push();
			t.is("undefined", typeof context.foo);
			t.is("undefined", typeof context.bar);
			context.pop();
			t.is("foo", context.foo);
			t.is("bar", context.bar);
		},
		function test_context_overpop(t){
			var context = new dojox.dtl.Context();
			try{
				context.pop();
				t.t(false);
			}catch(e){
				t.is("pop() called on empty Context", e.message);
			}
		},
		function test_context_filter(t){
			var context = new dojox.dtl.Context({ foo: "one", bar: "two", baz: "three" });
			var filtered = context.filter("foo", "bar");
			t.is(filtered.foo, "one");
			t.is(filtered.bar, "two");
			t.f(filtered.baz);

			filtered = context.filter({ bar: true, baz: true });
			t.f(filtered.foo);
			t.is(filtered.bar, "two");
			t.is(filtered.baz, "three");

			filtered = context.filter(new dojox.dtl.Context({ foo: true, baz: true }));
			t.is(filtered.foo, "one");
			t.f(filtered.bar);
			t.is(filtered.baz, "three");
		},
		function test_context_extend(t){
			var context = new dojox.dtl.Context({ foo: "one" });
			var extended = context.extend({ bar: "two", baz: "three" });
			t.is(extended.foo, "one");
			t.is(extended.bar, "two");
			t.is(extended.baz, "three");

			extended = context.extend({ barr: "two", bazz: "three" });
			t.is(extended.foo, "one");
			t.f(extended.bar);
			t.f(extended.baz);
			t.is(extended.barr, "two");
			t.is(extended.bazz, "three");

			t.f(context.bar)
			t.f(context.baz);
			t.f(context.barr);
			t.f(context.bazz);
		}
	]
);

}
