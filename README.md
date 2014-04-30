### Titanium Native Firebase Module for Android

## Setup
Compile the native module and install into your app. Than drop example/lib/firebase.js into your app/lib directory and use in your app:

    var Firebase = require('firebase');
    var firebase = new Firebase();

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

## Titanium Studio
Compile the module into your titanium project. From Titanium Studio, in the app explorer,  Click "Publish" and "Package Android Module". This will compile and package into your Titanium Project.

## Manual Install
Just unzip the dist/com.mlabieniec.ti.firebase-android-0.1.0.zip to your modules/android/ folder, and select it (or add it to) from the available modules in your tiapp.xml.

This is a native module for communicating with Firebase. It allows you to listen for change events on firebase collections. This is a work in progress and offers minimal functionality right now. Soon we will have an alloy sync adapter that abstracts the Firebase logic so that you can work with standard models in alloy.
	
	var changeHandler = function(data) {
		Ti.API.info("New Data! " + data);
		var parsedJson = JSON.parse(data);
		var name = data.name;
	};

    var instance = require('com.mlabieniec.ti.firebase');
    instance.init(Firebase.config.url, Firebase.config.key, path, function(data) {
			that.isConnected = true;
			if (args.complete) args.complete(data);
		},changeHandler);
    

"path" is the name of your firebase collection you want to listen to changes for, i.e. You would reference it on your firebase like: https://myfirebase.firebaseio.com/ref. You can then listen for property changes on collections in firebase with a propertyChangeHandler. You can listen on multiple collections by using the provided lib/firebase.js library and creating new instances of the module:

    var Firebase = require('firebase');
    var instance = new Firebase('collection');
    instance.connect({
        change:function(data) {
        	Ti.API.info("Remote Firebase change to: " + data);
        	var json = JSON.parse(data);
        	...
        },
        complete:funciton(e) {
        	Ti.API.info("Firebase authenticated and connected");
        }
    );
>>>>>>> 6615b8b5d939a9d08168baf0671335c8c4260304
