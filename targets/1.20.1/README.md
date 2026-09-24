# MultiHotbar

Minecraft **1.20.1**, Java 17, **Forge + Fabric**.

A Hotbar Bundle placed directly above a hotbar slot turns that slot into an ordered group. No new keybinds:

```text
1       -> normal item in hotbar slot 1
1 1     -> alternate 1
1 1 1   -> alternate 2
1 1 1 1 -> alternate 3
```

The chain window is 200 ms between presses. Each press restarts the window.

## Use

1. Craft a Hotbar Bundle from string over leather.
2. In the inventory, left-click items onto the Hotbar Bundle to append alternates.
3. Right-click the bundle with an empty cursor to remove the last alternate.
4. Put the bundle directly above the hotbar slot it should control.
5. Put the primary item in that hotbar slot.
6. Close the inventory and tap the corresponding number repeatedly.

Opening a screen normalizes mounted groups back to their primary item. While an inventory/container is open, MultiHotbar leaves number keys 1–9 untouched so vanilla hotbar item movement continues to work.

## One-command build + install

Forge:

```sh
./install.sh forge
```

Fabric:

```sh
./install.sh fabric
```

Both:

```sh
./build-all.sh
```

By default jars are copied to `~/.minecraft/mods`. Override with:

```sh
MINECRAFT_MODS_DIR=/path/to/instance/mods ./install.sh forge
```

`./gradlew` builds Fabric; `forge/gradlew` uses Gradle 8.8. A Java 17 JDK is required for Forge.

## Test

The loader-independent sequence state machine can be tested without Gradle:

```sh
./test-core.sh
```

Full build:

```sh
./build-all.sh
```

## Project structure

```text
common/
  client/    sequence state + network bridge
  core/      controller storage + authoritative swap engine
  item/      Hotbar Bundle inventory interaction + tooltip
  mixin/     vanilla hotbar input interception / screen normalization
  net/       compact C2S action payload
fabric/      Fabric registration + networking
forge/       Forge registration + networking
```

See `ARCHITECTURE.md` for invariants and the multiplayer trust boundary.

## v0.1 note

This is the full tools-capable variant. Because vanilla 1.21.1 Bundles cannot practically hold multiple non-stackable tools, this project adds a dedicated Hotbar Bundle and therefore needs the mod on server and client. The item currently stores up to eight alternate stacks, so it is a convenience/storage mechanic, not a strictly vanilla-capacity client-only mod.

Both loaders build in this repository. Install the JAR for the loader used by your Minecraft 1.20.1 instance.
