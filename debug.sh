#!/bin/sh
mvn versions:update-properties
mvnDebug -q hpi:run -Djava.util.logging.config.file=logging.properties -Djenkins.version=2.150.1 -Denforcer.skip=true

