Pivotal Mobile Services Suite Data SDK Samples for Android
==========================================================

Data SDK Usage
--------------
For more information please visit the [docs site](https://github.com/cfmobile/docs-datasync-android)


Data Demo Application
---------------------

The Data Demo Application is an example of the simplest application possible that uses the Pivotal Mobile Services Suite
Data Client SDK.  At this time, it only demonstrates how to authenticate the user and has code examples of how to persist
objects to the server.

Data Sample Application
-----------------------

There is a small sample application included in this repository to demonstrate and exercise the features in the Data
Client SDK.

You can use this sample application to test authorization using an OpenID Connect enabled identity server and the 
Pivotal Mobile Services Suite back-end server for data storage.  You are able to define an `DataObject` name and id 
and store and retrieve key-value data to the backend server for a given user.  

You can save your own project details by editing the values in the sample project's `data_default_preferences.xml` resource files.

Watch the log output in the sample application's display to see what the Data SDK is doing in the background.  This
log output should also be visible in the Android device log (for debug builds), but the sample application registers a
"listener" with the Data Library's logger so it can show you what's going on.