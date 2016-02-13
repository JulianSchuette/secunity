FROM centos:7

MAINTAINER Julian Schütte "julian.schuette@aisec.fraunhofer.de"

ENV TOMCAT_MAJOR_VERSION 8
ENV TOMCAT_MINOR_VERSION 8.0.30
ENV CATALINA_HOME /tomcat
ENV CATALINA_BASE /tomcat
ENV SESAME_VERSION 4.0.0
ENV SESAME_DATA /data
ENV JAVA_MAXMEMORY 4096


# IMPORT the Centos-7 GPG key to prevent warnings
RUN rpm --import http://mirror.centos.org/centos/RPM-GPG-KEY-CentOS-7

# Base Install + JDK
RUN yum -y update && \
    yum -y install java-1.8.0-openjdk && \
    yum -y install java-1.8.0-openjdk-devel && \
    yum -y update bash && \
    rm -rf /var/cache/yum/* && \
    yum clean all

# Install Tomcat
RUN curl -sS -O https://archive.apache.org/dist/tomcat/tomcat-${TOMCAT_MAJOR_VERSION}/v${TOMCAT_MINOR_VERSION}/bin/apache-tomcat-${TOMCAT_MINOR_VERSION}.tar.gz && \
    curl -sS https://archive.apache.org/dist/tomcat/tomcat-${TOMCAT_MAJOR_VERSION}/v${TOMCAT_MINOR_VERSION}/bin/apache-tomcat-${TOMCAT_MINOR_VERSION}.tar.gz.md5 | md5sum -c - && \
    tar zxf apache-tomcat-*.tar.gz && \
    rm apache-tomcat-*.tar.gz && \
    mv apache-tomcat* ${CATALINA_HOME}

# Remove docs and examples
RUN rm -rf $CATALINA_BASE/webapps/docs && \
    rm -rf $CATALINA_BASE/webapps/examples

# Add administrator account for http://<tomcat url>/manager
RUN echo "<tomcat-users>\
            <role rolename=\"manager-gui\"/> \
                <user username=\"admin\" password=\"admin\" roles=\"manager-gui\"/> \
           </tomcat-users>" > $CATALINA_BASE/conf/tomcat-users.xml

# Install Sesame
RUN curl -sS -o /tmp/sesame.tar.gz -L http://sourceforge.net/projects/sesame/files/Sesame%204/$SESAME_VERSION/openrdf-sesame-$SESAME_VERSION-sdk.tar.gz/download && \
    tar -xzf /tmp/sesame.tar.gz -C /opt && \ 
    ln -s /opt/openrdf-sesame-$SESAME_VERSION /opt/sesame && \
    rm /tmp/sesame.tar.gz

# Deploy 
RUN mkdir ${CATALINA_BASE}/webapps/openrdf-sesame && \
    cd ${CATALINA_BASE}/webapps/openrdf-sesame && \
    jar xf /opt/sesame/war/openrdf-sesame.war && \
    mkdir ${CATALINA_BASE}/webapps/openrdf-workbench && \
    cd ${CATALINA_BASE}/webapps/openrdf-workbench && \
    jar xf /opt/sesame/war/openrdf-workbench.war


ADD run.sh /run.sh
RUN chmod +x /*.sh

EXPOSE 8080
CMD ["/run.sh"]