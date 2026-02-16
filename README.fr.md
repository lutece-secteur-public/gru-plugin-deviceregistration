![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=plugin-deviceregistration-deploy)
[![Alerte](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-deviceregistration&metric=alert_status)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-deviceregistration)
[![Line of code](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-deviceregistration&metric=ncloc)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-deviceregistration)
[![Coverage](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-deviceregistration&metric=coverage)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-deviceregistration)

# Plugin DeviceRegistration

## Introduction

Le plugin DeviceRegistration permet la gestion des enregistrements d'appareils mobiles pour l'envoi de notifications push. Il offre une solution complète pour enregistrer, consulter et supprimer les tokens d'enregistrement associés aux identitésdes utilisateurs (GUID/CUID).

Ce plugin expose une API REST sécurisée permettant aux applications mobiles et services externes de gérer les enregistrements d'appareils. Il maintient également un historique complet de toutes les opérations (créations et suppressions) pour des besoinsd'audit et de traçabilité.

Les principales fonctionnalités incluent la création d'enregistrements d'appareils avec validation, la récupération des tokens d'enregistrement par critères (customer_id, connection_id), la suppression d'enregistrements et l'historisation automatiquede toutes les opérations.

## Configuration

 **Propriétés disponibles** 

Le fichier de configuration `deviceregistration.properties` contient les propriétés suivantes :

 
*  `context` : Contexte de l'application (par défaut : /lutece)

 **Beans Spring à injecter** 

Les beans suivants doivent être configurés dans le contexte Spring (fichier `deviceregistration_context.xml` ) :

 
*  `deviceregistration.deviceRegistrationDAO` : DAO pour la gestion des enregistrements d'appareils (classe : fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistrationDAO)
*  `deviceregistration.deviceRegistrationHistoryDAO` : DAO pour la gestion de l'historique (classe : fr.paris.lutece.plugins.deviceregistration.business.history.DeviceRegistrationHistoryDAO)
*  `deviceregistration.deviceRegistrationRest` : Service REST principal (classe : fr.paris.lutece.plugins.deviceregistration.rs.DeviceRegistrationRest)
*  `deviceregistration.swaggerRest` : Service Swagger pour la documentation API (classe : fr.paris.lutece.plugins.deviceregistration.rs.SwaggerRest)
*  `deviceregistration.uncaughtDeviceRegistrationExceptionMapper` : Gestionnaire d'exceptions REST (classe : fr.paris.lutece.plugins.deviceregistration.rs.error.UncaughtDeviceRegistrationExceptionMapper)

 **Classes Daemon** 

Ce plugin ne contient aucune classe Daemon à activer.

 **Caches** 

Ce plugin ne nécessite aucun cache à activer.

## Usage

 **Droits d'administration** 

Le plugin définit le droit d'administration suivant :

 
*  `DEVICEREGISTRATION_MANAGEMENT` : Gestion des enregistrements d'appareils (niveau 1) - Accès via l'URL : jsp/admin/plugins/deviceregistration/ManageDeviceRegistrations.jsp

 **Services Java exposés** 

Le plugin expose les services Java suivants :

 *DeviceRegistrationService* (singleton)

 
*  `getInstance()` : Obtenir l'instance unique du service
*  `createDeviceRegistration(String customerId, String connectionId, String registrationToken, String tokenIssuer)` : Créer un nouvel enregistrement d'appareil avec validation des données et création automatique d'une entrée dans l'historique
*  `deleteDeviceRegistration(String customerId, String connectionId, String registrationToken, String tokenIssuer)` : Supprimer un ou plusieurs enregistrements d'appareils selon les critères fournis avec historisation automatique
*  `getRegistrationTokensByCriteria(String customerId, String connectionId, String tokenIssuer)` : Récupérer la liste des tokens d'enregistrement selon les critères customer_id et/ou connection_id

 *DeviceRegistrationHistoryService* (singleton)

 
*  `getInstance()` : Obtenir l'instance unique du service
*  `createDeviceRegistrationHistory(DeviceRegistration deviceRegistration, ChangeType changeType)` : Créer une entrée dans l'historique pour tracer les opérations de création et suppression

 **API REST** 

Le plugin expose les API REST suivantes (base path : `/rest/deviceregistration/v1` ) :

 *GET /rest/deviceregistration/v1/token* 

 
* Description : Récupérer tous les tokens d'enregistrement par identité (GUID/CUID)
* Query Parameters : `connectionId` (optionnel), `customerId` (optionnel)
* Headers requis : `CLIENT_CODE` (obligatoire) - Code client/émetteur du token
* Réponse 200 : Token(s) d'enregistrement trouvé(s) (DeviceRegistrationResponse)
* Réponse 400 : Erreur de traitement avec message d'explication
* Réponse 404 : Token d'enregistrement non trouvé
* Content-Type : application/json

 *POST /rest/deviceregistration/v1/token* 

 
* Description : Créer un nouvel enregistrement d'appareil
* Headers requis : `CLIENT_CODE` (obligatoire) - Code client/émetteur du token
* Body : DeviceRegistrationRequest (JSON) avec customerId, connectionId et registrationToken
* Réponse 200 : Enregistrement d'appareil créé (DeviceRegistrationResponse)
* Réponse 400 : Erreur de traitement avec message d'explication
* Réponse 409 : Le token existe déjà
* Content-Type : application/json

 *DELETE /rest/deviceregistration/v1/token* 

 
* Description : Supprimer un enregistrement d'appareil
* Headers requis : `CLIENT_CODE` (obligatoire) - Code client/émetteur du token
* Body : DeviceRegistrationRequest (JSON) avec customerId, connectionId et registrationToken
* Réponse 204 : Enregistrement d'appareil supprimé avec succès
* Réponse 400 : Erreur de traitement avec message d'explication
* Réponse 404 : Aucun enregistrement n'a été supprimé
* Content-Type : application/json

Documentation Swagger disponible via le plugin swaggerui.


[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/plugin-deviceregistration/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*