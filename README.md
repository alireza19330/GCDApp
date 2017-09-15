ASSUMPTIONS
-----------
-This application developed and tested using Jboss EAP 7.0.0, Java8 and MySQL
-A JMS queue with JNDI "java:/jms/GCDQueue" should be available on the JBoss
-Application uses MySQL database to persist data so a XA datasource with JNDI "java:jboss/datasources/GCDAppDS2" should define in JBoss

REST SERVICES
-------------
- "list" method is accessible via a GET request to url "https://ip:port/GCDApp/rest/queue/"
- "push" method is accessible via a POST request to url "https://ip:port/GCDApp/rest/queue/push", data of i1 amd i2 should pass in the body of request in JSON format, for example: {"i1":15,"i2":25} They are to be added to the database and JMS queue in the same order. First i1 and after that i2 will be added.

SOAP SERVICES
-------------
- In all responses from the server there is always a response message which specifies the status of the request. It comes in response XML element. It can be one of the following: SUCCESSFUL, ERR_DB, ERR_SEND_QUEUE, ERR_OBJECT_MSG, ERR_CALCULATE, ERR_RECEIVE_QUEUE, ERR_AUTHENTICATION, ERR_AUTHORIZATION, ERR_UNKNOW.
- The WSDL file of the service is accessible via url "https://ip:port/GCDApp/gcd?wsdl"
- "gcd" method tries to get numbers from the queue and calculate the gcd, if there is no message in the queue it waits for 2 seconds and after that it closes the connection and return -1 as gcd to the client (In mathematics GCD is always positive so -1 can be distinguished as a bad result).
- "gcdSum" method returns sum of all calculated GCDs from the database if there is no GCD in the db it returns -1 as gcdSum.
- "gcdList" method returns null if there is no gcd stored in the database.

SECURITY
--------
- It assumed that consumer of the application's services are other applications and therefore services are not to be called from web pages directly. 
- According to OWASP(https://www.owasp.org/index.php/Web_Service_Security_Cheat_Sheet) for web service security following criteria are really important:
	-Transport Confidentiality
	-Server Authentication
	-User Authentication
	-Message Integrity
	-Message Confidentiality
	-Authorization
	-Content Validation
	-XML Denial of Service Protection

- Therefore by using two-way authentication TLS (Which is recommended by OWASP), some of these issues will be covered including: transport confidentiality, server authentication, user authentication, message integrity, message confidentiality, replay defenses.
- Settings related to the "two-way authentication TLS" should be implemented between the server (JBoss EAP 7) and the clients (Consumers of the services) according to JBoss web-site documentation (https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html-single/how_to_configure_server_security/#application_2-way_ssl)  
- To implement authorization, settings related to certification base authentication should be applied on JBoss EAP. By use of this approach, just authenticated requests from the trusted clients come to the application and users' principals carry the roles of the authenticated users, so role base authorization is used in the code by utilizing @RolesAllowed annotation on EJB methods. (https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html/how_to_configure_identity_management/cert_based_auth_security_domain) in the code it is assumed that there are two roles: "restclient" and "soapclient".

- To monitor every user's activities, unified form of logging is hired to log the result of each service invocation. The format of the log is as follows:
"service[WEB_SERVICE_TYPE] method[METHOD_NAME] user[USERNAME] response[RESPONSE_CODE]"
This format of logging is really useful specially in production environment where the system is up and running to find and monitor the live events in the application by using some basic tools and unix commands like grep, tail and ... Here is the explanation of information in log:
	- WEB_SERVICE_TYPE: will be "soap" for SOAP services and "rest" for REST services.
	- METHOD_NAME: will be one of the gcd, gcdList, gcdSum, push, list depending to the called method
	- USERNAME: username of the user invoked the service, we used certificate base authentication therefore username would be common name of the certificate
	- RESPONSE_CODE: is a number, the numbers and their definitions can be seen in ResponseCode enum. 













