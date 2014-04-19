// This is a test harness for your module
// You should do something interesting in this harness 
// to test out the module and to provide instructions 
// to users on how to use it by example.


// open a single window
var win = Ti.UI.createWindow({
	backgroundColor:'white'
});
var label = Ti.UI.createLabel();
win.add(label);
win.open();

var Firebase = require('firebase');
var fb = new Firebase("events");

var date = new Date();
var path = date.getFullYear() + "-" + date.getMonth() + "-" + date.getDate() + "/" + date.getHours();

if (fb.isConnected) {
	fb.append(path,options.data);
}

label.text = "Appending to collection: " + path;

}

