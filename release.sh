#!/bin/bash

./mvnw release:prepare release:perform -B -DperformRelease=true \
 && ./mvnw package \
 && git commit -a -m "chore: updating changelog" \
 && git push \
 || git clean -f && git checkout pom.xml
