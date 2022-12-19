#!/bin/bash

./mvnw se.bjurr.gitchangelog:git-changelog-maven-plugin:semantic-version \
  && git commit -a -m "chore: setting version in pom" && git push || echo "No new version" \
  && ./mvnw release:prepare release:perform -B \
  && ./mvnw se.bjurr.gitchangelog:git-changelog-maven-plugin:git-changelog \
  && git commit -a -m "chore: updating changelog" \
  && git push \
  || git clean -f
