#!/bin/sh
mvnDebug -q hpi:run -Djava.util.logging.config.file=logging.properties -Djenkins.version=2.121.1 -Denforcer.skip=true

