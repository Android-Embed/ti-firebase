// create the library class
var Firebase = require('firebase');
var firebase = new Firebase();

var settingsChangeHandler = function(data) {

	Ti.API.info("remote data changed: " + data);
	// do something with the remote data
	var remote = JSON.parse(data);

}; 

firebase.connect({
	complete:function(data) {
		Ti.API.info("Firebase Connected: " + JSON.stringify(data));
		// create a new child listener on /settings
		firebase.child({path:"settings",change:settingsChangeHandler});
	}
});

// Create a new node
function push() {

	var data = {
		"data" : {
			"id":"123",
			"this":"is",
			"a":"example"
		}
	}

	firebase.push({
		collection:"root",
		data:data
	});

}