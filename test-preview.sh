#!/usr/bin/env bash
set -euo pipefail
root=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
out=$(mktemp -d)
trap 'rm -rf "$out"' EXIT
for version in 1.20.1 1.20.4 1.21.1 1.21.4 1.21.5 1.21.8 1.21.11; do
  base="$root/targets/$version/common/src/main/java/dev/astra/multihotbar/client"
  javac -d "$out" \
    "$root/preview-tests/src/net/minecraft/world/item/ItemStack.java" \
    "$root/preview-tests/src/dev/astra/multihotbar/MultiHotbar.java" \
    "$root/preview-tests/src/dev/astra/multihotbar/core/HotbarBundleData.java" \
    "$root/preview-tests/src/dev/astra/multihotbar/client/PreviewAcceptanceTest.java" \
    "$base/PressChain.java" "$base/PreviewState.java" "$base/MultiHotbarClientState.java"
  echo "$version: $(java -cp "$out" dev.astra.multihotbar.client.PreviewAcceptanceTest)"
done
