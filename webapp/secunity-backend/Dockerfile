### Dockerfile

# Base Image: official tomcat 8 image, with jre 8 underneath
FROM tomcat:8-jre8

# Add tomcat users file for manager app
ADD container-files/conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml

# Add generated app war files to the webapps directory
ADD build/libs/secunity-backend-1.0.war /usr/local/tomcat/webapps/secunity-backend.war

# Add context config file for the application
ADD container-files/conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
ADD container-files/conf/logging.properties /usr/local/tomcat/conf/logging.properties
