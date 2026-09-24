# MultiHotbar 0.2.2 build audit

Checked on 2026-09-24 against the seven targets in `support.json`.

| Minecraft | Fabric | Forge | NeoForge | Result |
|---|---|---|---|---|
| 1.20.1 | built | built | — | Legacy NBT/networking port compiled for Fabric and Forge. |
| 1.20.4 | built | — | built | NBT storage and 1.20.4 networking port compiled; NeoForge dedicated server reached ready state. |
| 1.21.1 | built | — | built | Canonical target compiled. |
| 1.21.4 | built | — | built | Compiled after ASM dependency isolation. |
| 1.21.5 | built | — | built | Compiled after API migration. |
| 1.21.8 | built | — | built | Compiled after API and client networking migration. |
| 1.21.11 | built | — | built | Compiled after identifier, API, and client networking migration. |

The 14 deliverable JARs are in `dist/`. Each contains compiled classes, a loader descriptor declaring version `0.2.2`, `pack.mcmeta`, the version-appropriate recipe, and an item model definition where required. Minecraft 1.20.1 uses Fabric and Forge; all other targets use Fabric and NeoForge. The matrix command is `./build-matrix.sh all both`.

## Quick-select preview

The client reconstructs a short-lived ordered item snapshot from the controller and paired hotbar slot. Repeated presses reuse that snapshot so the preview updates immediately, including before the server's inventory update arrives. Empty target positions do not create a preview. The preview is cleared on screen opening and never changes the selection packet or server swap logic. Fabric uses its HUD callback on older targets and a hotbar-adjacent HUD element on 1.21.8+; NeoForge and Forge use client HUD events. The rendering contains item icons only, with no persistent panel or new keybind.

The preview lasts 720 ms, with a 55 ms fade-in and a 240 ms fade-out. `./test-preview.sh` exercises selection order, adjacent items, an intervening inventory update, slot change, empty target, reset, and the fade timeline on all seven target source variants.

## Confirmed fixes

- P1: Replaced dynamic NeoForge versions with published exact versions so the NeoForm runtime can resolve userdev artifacts and the mod metadata contains valid version ranges.
- P1: Corrected the NeoForge `javafml` loader range to `[1,)` and moved the 1.20.4 descriptor to `META-INF/mods.toml`, as required by that loader generation.
- P1: Ported 1.20.4 controller storage to NBT and its Fabric and NeoForge packet registration to the APIs available in that release.
- P1: Replaced the invalid NeoForge 1.20.1 target with Forge 47.4.23, ported shared storage and packets to the 1.20.1 APIs, and built the Fabric and Forge JARs.
- P1: Fixed the NeoForge 1.20.4 payload registrar namespace. The earlier code used `"1"` as the namespace and crashed during mod loading; it now registers in `multihotbar` with protocol version `"1"`.
- P1: Updated source calls changed in 1.21.5, 1.21.8, and 1.21.11, including optional NBT reads, selected-slot access, tooltip callbacks, `Identifier`, and client packet distribution.
- P2: Corrected pack compatibility metadata, 1.20.x recipe paths and result fields, 1.21.4+ item model definitions, and Java 17 Mixin compatibility for 1.20.x.
- P1: The server now rejects select packets while a container menu is open; restoration packets remain accepted.
- P2: The matrix continues after a failed target, and artifact collection and `doctor.sh` identify only JARs for the declared project version.
- P2: Replaced stale target-local README instructions and corrected version-specific architecture and 1.20.1 port notes for the public release.

## Remaining findings

- **P2 CONFIRMED — duplicate sequence state machines.** `shared/src/main/java/.../core/PressChain.java` and each target's `client/PressChain.java` implement the same behavior. Both have tests, but production uses the target copies. Classification: `OVERLAPPING`. Consolidation should be deliberate so target builds remain standalone.
- **P2 CONFIRMED — discovery tools unavailable in this environment.** The required Aktenordner and Agentwerk CLI checks fail before execution because their local Node packages `@akte/aktenordner-core` and `@akte/core` are missing. No new platform capability was introduced by this mod repair.

## Verification and limits

- All seven target-local `test-core.sh` checks and the shared `test-core.sh` passed.
- `./test-preview.sh` passed for all seven target variants.
- All 14 packaged JARs were inspected for classes, loader metadata, release version, recipes, and model files. Forge 1.20.1 also includes its Mixin config, manifest entry, and refmap.
- The mapped Minecraft JARs for the five 1.21 targets contain both Mixin target methods, `handleKeybinds` and `setScreen`.
- The packaged Fabric Mixin classes use remapped method names in all six built Fabric JARs.
- NeoForge 1.20.4 reached dedicated server ready state with the preview code present. Fabric and Forge 1.20.1 development server runs reached the EULA gate; that gate stops before full world startup. Preview appearance on an interactive client and multiplayer swaps remain untested.

## Format references

- [Minecraft's 1.21.4 item model change](https://www.minecraft.net/de-de/article/minecraft-snapshot-24w45a)
- [Minecraft's pack metadata change for versioned formats](https://www.minecraft.net/en-us/article/minecraft-snapshot-25w31a)
- [NeoForge Maven release metadata](https://maven.neoforged.net/releases/net/neoforged/neoform/maven-metadata.xml)
- [NeoForge 1.20.4 mod descriptor requirements](https://docs.neoforged.net/docs/1.20.4/gettingstarted/modfiles/)
- [Forge 1.20.1 release line](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html)
- [Forge SimpleChannel networking](https://docs.minecraftforge.net/en/1.20.x/networking/simpleimpl/)
- [Sponge Mixin on Forge](https://github.com/SpongePowered/Mixin/wiki/Mixins-on-Minecraft-Forge)
