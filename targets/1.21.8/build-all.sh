#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
./gradlew clean :common:compileJava :fabric:build :neoforge:build
