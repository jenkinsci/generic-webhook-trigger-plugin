#!/bin/sh
mvn versions:update-properties
mvn hpi:run -Djava.util.logging.config.file=logging.properties -Djenkins.version=2.346.3 -Denforcer.skip=true
