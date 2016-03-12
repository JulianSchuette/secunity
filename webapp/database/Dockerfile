FROM tomcat:8-jre8

MAINTAINER Carlos Pedrinaci "carlos.pedrinaci@open.ac.uk"

ENV CATALINA_BASE /usr/local/tomcat
ENV SESAME_VERSION 4.1.1
ENV SESAME_DATA /data
ENV JAVA_MAXMEMORY 256

RUN apt-get update && apt-get install -y wget

RUN wget http://sourceforge.net/projects/sesame/files/Sesame%204/$SESAME_VERSION/openrdf-sesame-$SESAME_VERSION-sdk.tar.gz/download -O /tmp/sesame.tar.gz && tar xzf /tmp/sesame.tar.gz -C /opt && ln -s /opt/openrdf-sesame-$SESAME_VERSION /opt/sesame && rm /tmp/sesame.tar.gz
#RUN wget https://sourceforge.net/projects/sesame/files/Sesame%204/$SESAME_VERSION/openrdf-sesame-$SESAME_VERSION-sdk.tar.gz/download

# Remove docs and examples
RUN rm -rf $CATALINA_BASE/webapps/docs && rm -rf $CATALINA_BASE/webapps/examples

# Deploy 
#RUN mkdir ${CATALINA_BASE}/webapps/openrdf-sesame && cd ${CATALINA_BASE}/webapps/openrdf-sesame && jar xf /opt/sesame/war/openrdf-sesame.war &&  mkdir ${CATALINA_BASE}/webapps/openrdf-workbench && cd ${CATALINA_BASE}/webapps/openrdf-workbench && jar xf /opt/sesame/war/openrdf-workbench.war
RUN cp /opt/sesame/war/*.war ${CATALINA_BASE}/webapps/

COPY docker-files/run.sh /run.sh

VOLUME /data