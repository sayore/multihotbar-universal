#!/usr/bin/env sh
set -eu
ROOT=$(CDPATH= cd -- "$(dirname -- "$0")/../.." && pwd)
cd "$ROOT"
./install.sh 1.20.1 "${1:-fabric}" "${2:-${MINECRAFT_MODS_DIR:-$HOME/.minecraft/mods}}"
