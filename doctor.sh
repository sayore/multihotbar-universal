#!/usr/bin/env bash
set -u
printf '%-10s %-10s %-10s %-10s %-12s\n' VERSION FABRIC FORGE NEOFORGE SOURCE
for v in 1.20.1 1.20.4 1.21.1 1.21.4 1.21.5 1.21.8 1.21.11; do
  f=no; g=no; n=no
  version=$(sed -n 's/^version=//p' "targets/$v/gradle.properties")
  [[ -f "targets/$v/fabric/build/libs/multihotbar-fabric-$v-$version.jar" ]] && f=yes
  [[ -f "targets/$v/forge/build/libs/multihotbar-forge-$v-$version.jar" ]] && g=yes
  [[ -f "targets/$v/neoforge/build/libs/multihotbar-neoforge-$v-$version.jar" ]] && n=yes
  src=generated
  [[ "$v" == 1.21.1 ]] && src=canonical
  printf '%-10s %-10s %-10s %-10s %-12s\n' "$v" "$f" "$g" "$n" "$src"
done
