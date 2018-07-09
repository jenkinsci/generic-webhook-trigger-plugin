#!/bin/sh
mvn -q hpi:run -Djava.util.logging.config.file=logging.properties -Djenkins.version=2.89.3 -Denforcer.skip=true
