# Changelog

## 0.2.2 — Quick Select

- Added a short-lived, client-side preview above the selected hotbar slot. The current item is emphasized, with the nearest configured items shown above and below it.
- The preview updates immediately on another number-key press, fades over 720 ms, and closes when a screen opens or the vanilla HUD is hidden.
- Kept the existing selection sequence, server authority, and network packet format unchanged. Inventory and container number keys retain vanilla behavior; Shift + number navigation is not included.
- Added Forge and Fabric builds for Minecraft 1.20.1 alongside Fabric and NeoForge builds for the six other declared releases.
- Fixed the NeoForge 1.20.4 payload namespace registration crash and version-specific build, resource, and API issues across the release matrix.

All 14 release JARs pass their build and package checks. Preview state tests pass for every target source variant. A NeoForge 1.20.4 dedicated server reached ready state. Interactive client rendering and multiplayer gameplay have not been visually tested; see [BUILD_REPORT.md](BUILD_REPORT.md).
