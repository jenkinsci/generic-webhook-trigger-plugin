#!/bin/sh
./mvnw versions:update-properties
MAVEN_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=n" \
 ./mvnw hpi:run \
 -Djava.util.logging.config.file=logging.properties \
 -Djenkins.version=2.361.4 \
 -Denforcer.skip=true \
 -Dhudson.model.ParametersAction.keepUndefinedParameters=true
