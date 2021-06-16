#!/bin/sh
mvn versions:update-properties
mvn hpi:run -Djava.util.logging.config.file=logging.properties -Djenkins.version=2.277.4 -Denforcer.skip=true
