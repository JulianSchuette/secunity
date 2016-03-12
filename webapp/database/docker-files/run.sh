#!/bin/bash

# Sets data directory for OpenRDF
export JAVA_OPTS="$JAVA_OPTS -Dinfo.aduna.platform.appdata.basedir=/data"

# For parsing large XML files
export JAVA_OPTS="${JAVA_OPTS} -Djdk.xml.entityExpansionLimit=0"

# Allow slashes in paths to support JAX RS parameters containing slashes (INSECURE: http://tomcat.apache.org/security-6.html)
export JAVA_OPTS="${JAVA_OPTS} -Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true"

export CATALINA_PID=${CATALINA_HOME}/temp/tomcat.pid
export CATALINA_OPTS="$CATALINA_OPTS -Xmx${JAVA_MAXMEMORY}m -Djava.security.egd=file:/dev/./urandom"

exec ${CATALINA_HOME}/bin/catalina.sh run
