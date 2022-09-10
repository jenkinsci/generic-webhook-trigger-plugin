#!/bin/bash

gpg -o /tmp/dummy --sign .gitignore \
 && nextVersion=$(npx git-changelog-command-line --print-next-version) \
 && ./mvnw versions:set -DnewVersion=$nextVersion-SNAPSHOT \
 && git commit -a -m "chore: setting version $nextVersion-SNAPSHOT" \
 && ./mvnw release:prepare release:perform -B -DperformRelease=true \
 && npx git-changelog-command-line -of CHANGELOG.md -ip ".*maven-release-plugin.*" \
 && git commit -a -m "chore: updating changelog" \
 && git push \
 || git clean -f && git checkout pom.xml