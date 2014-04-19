/**
 * Constructor
 */
function Firebase(child) {
	
	var instance = require('com.mlabieniec.ti.firebase');
	var path = Titanium.Platform.macaddress + "/" + child;
	var that = this;
	
	/**
	 * Connects to a Firebase
	 * @param {Object} child Firebase path i.e. "/settings/123456"
	 * @param {Object} args {changeHandler, completeHandler}
	 */
	this.connect = function(args) {
		// The path is always prepended with this devices mac address
		Ti.API.info("Firebase connecting to: " + path);
		var changeHandler = (args.change)?args.change:null;
		// initialize the native module
		instance.init(Firebase.config.url, Firebase.config.key, path, function(data) {
			that.isConnected = true;
			if (args.complete) args.complete(data);
		},changeHandler);
	};
	
	/**
	 * Push a new value at the paths location to the given value
	 * The propertyChange handler will also fire when this completes
	 * @param {Object} child
	 * @param {Object} value
	 */
	this.push = function(value) {
		instance.push({collection:path,data:value});
	};
	
	/**
	 * Append a new value to the child in the given location
	 * the location prepends with the child for this instance
	 * 
	 * For example, if the instance was created with 'logs' you can pass
	 * append('2014-3-6/1200', {some:value}) to add sub-folders in the child 
	 * 
	 * @param {Object} collection i.e. something/somewhere
	 * @param {Object} value {some:value}
	 */
	this.append = function(location,value) {
		var loc = path + "/" + location;
 		instance.push({collection:loc,data:value});
	};
	
	/**
	 * Updates a collections children
	 */
	this.update = function(data) {
		instance.updateChildren(path,data,null);
	};
	
	/**
	 * This is the default property change handler which should be over-ridden when
	 * connecting to Firebase
	 * @param {Object} data
	 */
	this.propertyChange = function(data) {
		Ti.API.info('Firebase.propertyChange: ' + JSON.stringify(data));
	};
	
	/**
	 * Check if a Firebase is connected
	 */
	this.isConnected = false;
	
};

/**
 * Firebase url and api-key
 */
Firebase.config = {
	url:"https://YOUR_FIREBASE.firebaseIO.com",
	key:"YOUR_FIREBASE_SECRET_KEY"
};

module.exports = Firebase;