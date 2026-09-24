#!/usr/bin/env bash
set -euo pipefail
OUT=${1:-dist}
mkdir -p "$OUT"
for dir in targets/*; do
  [[ -f "$dir/gradle.properties" ]] || continue
  v=${dir#targets/}
  version=$(sed -n 's/^version=//p' "$dir/gradle.properties")
  for loader in fabric forge neoforge; do
    jar="$dir/$loader/build/libs/multihotbar-$loader-$v-$version.jar"
    [[ -f "$jar" ]] || continue
    cp "$jar" "$OUT/multihotbar-${v}-${loader}.jar"
  done
done
