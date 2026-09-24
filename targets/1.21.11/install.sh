#!/usr/bin/env sh
set -eu
ROOT=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
TARGET=${1:-neoforge}
MODS_DIR=${MINECRAFT_MODS_DIR:-"$HOME/.minecraft/mods"}
mkdir -p "$MODS_DIR"
case "$TARGET" in
  neoforge) TASKS=":neoforge:build"; MODULES="neoforge" ;;
  fabric) TASKS=":fabric:build"; MODULES="fabric" ;;
  both) TASKS=":neoforge:build :fabric:build"; MODULES="neoforge fabric" ;;
  *) echo "usage: ./install.sh [neoforge|fabric|both]" >&2; exit 2 ;;
esac
cd "$ROOT"
./gradlew $TASKS
for m in $MODULES; do
  JAR=$(find "$m/build/libs" -maxdepth 1 -type f -name '*.jar' ! -name '*-sources.jar' ! -name '*-javadoc.jar' | head -n 1)
  [ -n "$JAR" ] || { echo "No release jar found for $m" >&2; exit 3; }
  cp -f "$JAR" "$MODS_DIR/"
  echo "installed: $MODS_DIR/$(basename "$JAR")"
done
