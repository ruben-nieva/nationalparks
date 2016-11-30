# Sample application for OpenShift 3

This sample application will create and deploy a Java EE application server. It will not autodeploy the DB or the populate the data.
Those steps need to be done separately to provide a different demo. It is intended to use an external MongoDB cluster (external
to OpenShift), such as MongoDB Atlas.

The sample application will display a map and perform geospatial queries to populate the map with all National Parks in the world.

There are two options for this sample application depending on what you have available in your environment.  The options are to use JBoss EAP latest or Wildfly latest.  If you are using the openshift all-in-one image, use Wildfly.  If you are using OpenShift Online 3, Dedicated, or Enteprise, use EAP.

## Quick instructions to just get this working on an OpenShift 3 deployment as a normal user

````
$ oc login https://yourOpenShiftServer
$ oc new-project nationalparks
````
Since this is using Atlas we need to load the configuration file into a ConfigMap. 
`````
oc create configmap mongo-config --from-file=atlas-connection.prop
`````
If your environment (all-in-one) has Wildfly, you can use this:
`````
 oc new-app .\nationalparks-template-wildfly.json
`````

Or if you want to make a template for all users you can do this:

`````
$ oc create -f https://gitlab.com/jorgemoralespou/openshift3nationalparks/raw/master/nationalparks-template-wildfly.json
$ oc new-app nationalparks-wildfly
`````
