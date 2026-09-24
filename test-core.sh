#!/usr/bin/env sh
set -eu
OUT="${TMPDIR:-/tmp}/multihotbar-core-test"
rm -rf "$OUT" && mkdir -p "$OUT"
javac -d "$OUT" $(find shared/src/main/java shared/src/test/java -name '*.java')
java -cp "$OUT" dev.astra.multihotbar.core.CoreTest
