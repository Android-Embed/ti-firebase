/**
 * Firebase Titanium module
 * This Titanium module allows a native android app to 
 * interact with Firebase collections
 * 
 * @author Michael Labieniec<michaellabieniec@gmail.com>
 */
package com.mlabieniec.ti.firebase;

import java.util.HashMap;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.KrollPropertyChange;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;

import android.app.Activity;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Firebase.AuthListener;
import com.firebase.client.Firebase.CompletionListener;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

@Kroll.module(name="Firebaseti", id="com.mlabieniec.ti.firebase")
public class FirebasetiModule extends KrollModule
{
	//The firebase account URL
	@Kroll.constant
	public static final String FIREBASE_URL = "";
	
	//The firebase authentication secret
	@Kroll.constant
	public static final String FIREBASE_AUTH = "";
	
	// Standard Debugging variables
	private static final String TAG = "Firebase";
	
	private Firebase events;
	private ValueEventListener connectedListener;
	
	// The JavaScript callbacks (KrollCallback objects)
	private KrollFunction successCallback = null;
	private KrollFunction cancelCallback = null;
	private KrollFunction requestDataCallback = null;
	private KrollFunction connectedCallback = null;

	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;
	
	public FirebasetiModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		Log.d(TAG, "inside onAppCreate");
		// put module init code that needs to run when the application is created
	}
	
	// Lifecycle

	// NOTES:
	//
	// 1. Modules are created in the root context
	// 2. Using navBarHidden (or fullscreen or modal) causes the window, when opened, to run in a new Android Activity. 
	// 3. The root context/activity will be stopped when a new activity is launched
	// 4. Lifecycle notifications will NOT be received while the root activity is stopped.

	@Override
	public void onStart(Activity activity) 
	{
		// This method is called when the module is loaded and the root context is started

		Log.d(TAG, "[MODULE LIFECYCLE EVENT] start");

		super.onStart(activity);
	}

	@Override
	public void onStop(Activity activity) 
	{
		// This method is called when the root context is stopped 

		Log.d(TAG, "[FIREBASE MODULE LIFECYCLE EVENT] stop");

		super.onStop(activity);
	}

	@Override
	public void onPause(Activity activity) 
	{
		// This method is called when the root context is being suspended

		Log.d(TAG, "[FIREBASE MODULE LIFECYCLE EVENT] pause");

		super.onPause(activity);
	}

	@Override
	public void onResume(Activity activity) 
	{		
		// This method is called when the root context is being resumed

		Log.d(TAG, "[FIREBASE MODULE LIFECYCLE EVENT] resume");	

		super.onResume(activity);
	}

	@Override
	public void onDestroy(Activity activity) 
	{
		// This method is called when the root context is being destroyed

		Log.d(TAG, "[FIREBASE MODULE LIFECYCLE EVENT] destroy");

		super.onDestroy(activity);
	}
	
	@Kroll.method
	public void registerCallbacks(HashMap args)
	{
		Object callback;

		Log.d(TAG,"[Firebase] registerCallbacks called");

		// Save the callback functions, verifying that they are of the correct type
		if (args.containsKey("success")) {
			callback = args.get("success");
			if (callback instanceof KrollFunction) {
				successCallback = (KrollFunction)callback;
			}
		}
		if (args.containsKey("cancel")) {
			callback = args.get("cancel");
			if (callback instanceof KrollFunction) {
				cancelCallback = (KrollFunction)callback;
			}
		}	
		if (args.containsKey("requestData")) {
			callback = args.get("requestData");
			if (callback instanceof KrollFunction) {
				requestDataCallback = (KrollFunction)callback;
			}
		}
		if (args.containsKey("connected")) {
			callback = args.get("connected");
			if (callback instanceof KrollFunction) {
				connectedCallback = (KrollFunction)callback;
			}
		}

		Log.d(TAG,"[Firebase] Callbacks registered");
	}
	
	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, KrollProxy proxy) 
	{
        // If the 'modelListener' property has been set for this proxy then this method is called
        // whenever a proxy property value is updated. Note that this method is called whenever the
        // setter is called, so it will get called even if the value of the property has not changed.

        if ((oldValue == newValue) ||
            ((oldValue != null) && oldValue.equals(newValue))) {
            return;
        }

		Log.d(TAG, "[FIREBASE] Property " + key + " changed from " + oldValue + " to " + newValue);

		// If is a good idea to check if there are listeners for the event that
		// is about to fired. There could be zero or multiple listeners for the
		// specified event.
		if (hasListeners("propertyChange")) {
			HashMap<String, Object> event = new HashMap<String, Object>();
			event.put("property", key);
			event.put("oldValue",oldValue);
			event.put("newValue",newValue);

			fireEvent("propertyChange", event);
		}
	}
	
	@Kroll.method
	public void signalEvent()
	{
		Log.d(TAG,"[FIREBASE] signalEvent called");

		// It is a good idea to check if there are listeners for the event that
		// is about to fired. There could be zero or multiple listeners for the
		// specified event.
		if (hasListeners("demoEvent")) {
			HashMap<String, Object> event = new HashMap<String, Object>();
			event.put("index",1);
			event.put("value",100);
			event.put("name","userEvent");

			fireEvent("demoEvent", event);

			Log.d(TAG,"[FIREBASE] demoEvent fired");
		}
	}

	@Override
	public void propertiesChanged(List<KrollPropertyChange> changes, KrollProxy proxy) 
	{

		Log.d(TAG, "[FIREBASE] propertiesChanged");

		for (KrollPropertyChange change : changes) {
			propertyChanged(change.getName(), change.getOldValue(), change.getNewValue(), proxy);
		}
	}
	
	@Kroll.method
	public void init(String ref) 
	{
		// Create a reference to a Firebase location
		events = new Firebase(FIREBASE_URL).child(ref);
		events.auth(FIREBASE_AUTH, new AuthListener() {
			
			@Override
			public void onAuthSuccess(Object arg) {
				Log.d(TAG,"Firebase.onAuthSuccess: " + arg.toString());
			}
			
			@Override
			public void onAuthRevoked(FirebaseError arg) {
				Log.d(TAG,"Firebase.onAuthSuccess: " + arg.getMessage());
				
			}
			
			@Override
			public void onAuthError(FirebaseError arg) {
				Log.d(TAG,"Firebase.onAuthSuccess: " + arg.getMessage());
				
			}
		});
		
		// Read data and react to changes
		events.addValueEventListener(new ValueEventListener() {
	
		    @Override
		    public void onDataChange(DataSnapshot snap) {
		        Log.d(TAG,snap.getName() + " -> " + snap.getValue());
		        setPropertyAndFire(snap.getName(),snap.getValue());
		    }
	
			@Override
			public void onCancelled(FirebaseError error) {
				// TODO Auto-generated method stub
				Log.d(TAG,"FirebaseError: " + error.getCode() + " " + error.getMessage());
				
			}
		});
		
		// Finally, a little indication of connection status
        connectedListener = events.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean)dataSnapshot.getValue();
                if (connected) {
                    Log.d(TAG,"Connected to Firebase");
                } else {
                    Log.d(TAG,"Disconnected from Firebase");
                }
            }

			@Override
			public void onCancelled(FirebaseError error) {
				Log.d(TAG,"FirebaseError: " + error.getCode() + " " + error.getMessage());
			}
        });
	}
	
	@Kroll.method
	public void callThisCallbackDirectly(HashMap args)
	{
		// By specifying an explicit argument type in the method declaration (rather
		// than a generic Object array), the argument type has already been validated
		
		KrollFunction callback = null;
		Object object = args.get("callback");
		if (object instanceof KrollFunction) {
			callback = (KrollFunction)object;
		}
		
		Object data = args.get("data");

		// Our callback will be passed 2 arguments: the value of the data property
		// from the dictionary passed in and a fixed string
		Object[] arrayOfValues = new Object[]{ data, "Firebase" };

		if (callback != null) {
			// The 'callSync' method of the KrollCallback object can be used to directly 
			// call the associated JavaScript function and get a return value. In this
			// instance there is no return value for the callback.
			callback.call(getKrollObject(), arrayOfValues);

			//Log.d(TAG,"[KROLLDEMO] callback was called");
		}
	}
	
	/**
	 * Push a new  to a dataset
	 * @param args HashMaps {collection:'events', data:{...}}
	 */
	@Kroll.method
	public void push(HashMap args) 
	{
		events.getRoot().child(args.get("collection").toString()).push().setValue(args.get("data"));
	}
	
	/**
	 * Push a new item to a dataset
	 * @param args HashMaps {collection:'events', data:{...}}
	 */
	@Kroll.method
	public void setValue(HashMap args) 
	{
		events.getRoot().child(args.get("collection").toString()).setValue(args.get("data"));
	}
	
	@Kroll.method
	public void updateChildren(String collection, HashMap map, Object cb) 
	{
		events.getRoot().child(collection).updateChildren(map);
	}

}

