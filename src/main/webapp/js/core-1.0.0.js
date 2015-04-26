// Core module
define(["jquery"], function($) {
	var core = {
		
		// name of library
		name: "Core Library",
		
		// library version
		version: "1.0.0",
		
		// log function
		log: function(o) { console.log(o); },
		
		// convert object to JSON string
		stringify: JSON.stringify,
		
		// parse JSON string into object
		parseJSON: $.parseJSON,
		
		// load a property from server
		prop: function(key) {
			var lastIdx = key.lastIndexOf("/")
			var bundleName = key.substr(0, lastIdx);
			var propKey = key.substr(lastIdx + 1);
			var bundle = this._getBundle("properties/" + bundleName);
			return bundle[propKey];
		},
		
		// get a bundle
		_getBundle: function(bundleName) {
			var bundle = this._bundles[bundleName]; 
			if (!bundle) {
				this._bundles[bundleName] = bundle = this._loadBundle(bundleName);
				this.log(bundle);
			}
			return bundle;
		},
		
		// load a bundle
		_loadBundle: function(bundleName) {
			var url = contextPath + "/i18n?bundleName=" + bundleName;
			var jsonStr = $.ajax({
				url: url,
				dataType: "json",
				async: false
			}).responseText;
			return this.parseJSON(jsonStr);
		},
		
		_bundles: {}
	
	};
	return core;
});