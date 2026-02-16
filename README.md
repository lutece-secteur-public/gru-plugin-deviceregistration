![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=plugin-deviceregistration-deploy)
[![Alerte](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-deviceregistration&metric=alert_status)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-deviceregistration)
[![Line of code](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-deviceregistration&metric=ncloc)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-deviceregistration)
[![Coverage](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-deviceregistration&metric=coverage)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-deviceregistration)

# Plugin DeviceRegistration

## Introduction

The DeviceRegistration plugin provides management of mobile device registrations for push notification delivery. It offers a complete solution for registering, querying, and deleting registration tokens associated with useridentities (GUID/CUID).

This plugin exposes a secure REST API allowing mobile applications and external services to manage device registrations. It also maintains a complete history of all operations (creations and deletions) for audit and traceability purposes.

Key features include device registration creation with validation, retrieval of registration tokens by criteria (customer_id, connection_id), registration deletion, and automatic historization of all operations.

## Configuration

 **Available Properties** 

The configuration file `deviceregistration.properties` contains the following properties:

 
*  `context` : Application context (default: /lutece)

 **Spring Beans to Inject** 

The following beans must be configured in the Spring context ( `deviceregistration_context.xml` file):

 
*  `deviceregistration.deviceRegistrationDAO` : DAO for device registration management (class: fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistrationDAO)
*  `deviceregistration.deviceRegistrationHistoryDAO` : DAO for history management (class: fr.paris.lutece.plugins.deviceregistration.business.history.DeviceRegistrationHistoryDAO)
*  `deviceregistration.deviceRegistrationRest` : Main REST service (class: fr.paris.lutece.plugins.deviceregistration.rs.DeviceRegistrationRest)
*  `deviceregistration.swaggerRest` : Swagger service for API documentation (class: fr.paris.lutece.plugins.deviceregistration.rs.SwaggerRest)
*  `deviceregistration.uncaughtDeviceRegistrationExceptionMapper` : REST exception handler (class: fr.paris.lutece.plugins.deviceregistration.rs.error.UncaughtDeviceRegistrationExceptionMapper)

 **Daemon Classes** 

This plugin does not contain any Daemon classes to activate.

 **Caches** 

This plugin does not require any cache activation.

## Usage

 **Administration Rights** 

The plugin defines the following administration right:

 
*  `DEVICEREGISTRATION_MANAGEMENT` : Device registration management (level 1) - Access via URL: jsp/admin/plugins/deviceregistration/ManageDeviceRegistrations.jsp

 **Exposed Java Services** 

The plugin exposes the following Java services:

 *DeviceRegistrationService* (singleton)

 
*  `getInstance()` : Get the unique service instance
*  `createDeviceRegistration(String customerId, String connectionId, String registrationToken, String tokenIssuer)` : Create a new device registration with data validation and automatic history entry creation
*  `deleteDeviceRegistration(String customerId, String connectionId, String registrationToken, String tokenIssuer)` : Delete one or more device registrations according to provided criteria with automatic historization
*  `getRegistrationTokensByCriteria(String customerId, String connectionId, String tokenIssuer)` : Retrieve the list of registration tokens according to customer_id and/or connection_id criteria

 *DeviceRegistrationHistoryService* (singleton)

 
*  `getInstance()` : Get the unique service instance
*  `createDeviceRegistrationHistory(DeviceRegistration deviceRegistration, ChangeType changeType)` : Create a history entry to track creation and deletion operations

 **REST API** 

The plugin exposes the following REST APIs (base path: `/rest/deviceregistration/v1` ):

 *GET /rest/deviceregistration/v1/token* 

 
* Description: Get all registration tokens by identity (GUID/CUID)
* Query Parameters: `connectionId` (optional), `customerId` (optional)
* Required Headers: `CLIENT_CODE` (mandatory) - Client code/token issuer
* Response 200: Registration token(s) found (DeviceRegistrationResponse)
* Response 400: Processing error with explanation message
* Response 404: Registration token not found
* Content-Type: application/json

 *POST /rest/deviceregistration/v1/token* 

 
* Description: Create a new device registration
* Required Headers: `CLIENT_CODE` (mandatory) - Client code/token issuer
* Body: DeviceRegistrationRequest (JSON) with customerId, connectionId and registrationToken
* Response 200: Device registration created (DeviceRegistrationResponse)
* Response 400: Processing error with explanation message
* Response 409: Token already exists
* Content-Type: application/json

 *DELETE /rest/deviceregistration/v1/token* 

 
* Description: Delete a device registration
* Required Headers: `CLIENT_CODE` (mandatory) - Client code/token issuer
* Body: DeviceRegistrationRequest (JSON) with customerId, connectionId and registrationToken
* Response 204: Device registration deleted successfully
* Response 400: Processing error with explanation message
* Response 404: Nothing was deleted
* Content-Type: application/json

Swagger documentation available via the swaggerui plugin.


[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/plugin-deviceregistration/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*