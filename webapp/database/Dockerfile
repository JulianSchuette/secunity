FROM tomcat:8-jre8

MAINTAINER Julian Schütte "julian@schuette.io"

ENV CATALINA_BASE /usr/local/tomcat
ENV SESAME_VERSION 4.1.1
ENV SESAME_DATA /data
ENV JAVA_MAXMEMORY 256

RUN apt-get update && apt-get install -y wget

# Download OpenRDF
RUN wget http://sourceforge.net/projects/sesame/files/Sesame%204/$SESAME_VERSION/openrdf-sesame-$SESAME_VERSION-sdk.tar.gz/download -O /tmp/sesame.tar.gz && tar xzf /tmp/sesame.tar.gz -C /opt && ln -s /opt/openrdf-sesame-$SESAME_VERSION /opt/sesame && rm /tmp/sesame.tar.gz

# Remove docs and examples
RUN rm -rf $CATALINA_BASE/webapps/docs && rm -rf $CATALINA_BASE/webapps/examples

# Deploy 
RUN cp /opt/sesame/war/*.war ${CATALINA_BASE}/webapps/

COPY docker-files/run.sh /run.sh

VOLUME /data
