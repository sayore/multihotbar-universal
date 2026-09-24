#!/usr/bin/env bash
set -euo pipefail
TARGET=${1:-all}
LOADER=${2:-both}
VERSIONS=(1.20.1 1.20.4 1.21.1 1.21.4 1.21.5 1.21.8 1.21.11)
[[ "$TARGET" == all ]] || VERSIONS=("$TARGET")
case "$LOADER" in fabric|forge|neoforge|both) ;; *) echo "loader must be fabric|forge|neoforge|both" >&2; exit 2 ;; esac
failed=0
for v in "${VERSIONS[@]}"; do
  dir="targets/$v"
  [[ -d "$dir" ]] || { echo "unknown target: $v" >&2; exit 2; }
  echo "== $v / $LOADER =="
  if [[ "$v" == 1.20.1 ]]; then
    if [[ "$LOADER" == fabric || "$LOADER" == both ]]; then
      if ! (cd "$dir" && ./gradlew :fabric:build); then echo "FAILED: $v / fabric" >&2; failed=1; fi
    fi
    if [[ "$LOADER" == forge || "$LOADER" == both ]]; then
      if ! (
        cd "$dir/forge"
        if [[ -x "$HOME/.gradle/jdks/eclipse_adoptium-17-amd64-linux.2/bin/java" ]]; then
          export JAVA_HOME="$HOME/.gradle/jdks/eclipse_adoptium-17-amd64-linux.2"
        fi
        ./gradlew build
      ); then echo "FAILED: $v / forge" >&2; failed=1; fi
    fi
    [[ "$LOADER" != neoforge ]] || { echo "UNSUPPORTED: $v / neoforge" >&2; failed=1; }
    continue
  fi
  case "$LOADER" in
    fabric) tasks=(:fabric:build) ;;
    neoforge) tasks=(:neoforge:build) ;;
    forge) echo "UNSUPPORTED: $v / forge" >&2; failed=1; continue ;;
    both) tasks=(:fabric:build :neoforge:build) ;;
  esac
  if ! (cd "$dir" && ./gradlew "${tasks[@]}"); then
    echo "FAILED: $v / $LOADER" >&2
    failed=1
  fi
done
exit "$failed"
