# Port status: Minecraft 1.21.4

This target shares the MultiHotbar interaction contract with 1.21.1.

- Fabric API: `0.119.4+1.21.4`
- NeoForge selector: `21.4.+` (resolved online during build; lock before release)
- Java: `21`
- Input semantics: identical
- Inventory controller geometry: identical
- Network rule: client sends only slot/index; server validates and mutates inventory

## API family

`components`: Data Components era. Source is generated from the canonical 1.21.1 integration and must pass the target Gradle compile gate.
