#!/bin/sh
mvn versions:update-properties
mvn hpi:run -Djava.util.logging.config.file=logging.properties -Djenkins.version=2.181 -Denforcer.skip=true
