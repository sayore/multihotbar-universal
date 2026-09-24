#!/usr/bin/env sh
set -eu
ROOT=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
OUT=$(mktemp -d)
trap 'rm -rf "$OUT"' EXIT
javac -d "$OUT" \
  "$ROOT/common/src/main/java/dev/astra/multihotbar/client/PressChain.java" \
  "$ROOT/common/src/test/java/dev/astra/multihotbar/client/PressChainTest.java"
java -cp "$OUT" dev.astra.multihotbar.client.PressChainTest
