#!/usr/bin/env bash
set -euo pipefail
if [[ $# -lt 2 ]]; then echo "usage: ./install.sh <minecraft-version> <fabric|forge|neoforge> [mods-dir]"; exit 2; fi
v=$1; loader=$2; mods=${3:-${MINECRAFT_MODS_DIR:-$HOME/.minecraft/mods}}
./build-matrix.sh "$v" "$loader"
mkdir -p "$mods"
version=$(sed -n 's/^version=//p' "targets/$v/gradle.properties")
jar="targets/$v/$loader/build/libs/multihotbar-$loader-$v-$version.jar"
[[ -f "$jar" ]] || { echo "missing build artifact: $jar" >&2; exit 1; }
cp "$jar" "$mods/"
echo "installed: $jar -> $mods"
