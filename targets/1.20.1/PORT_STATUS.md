# Port status: Minecraft 1.20.1

This target shares the MultiHotbar interaction contract with 1.21.1.

- Fabric API: `0.92.6+1.20.1`
- NeoForge selector: `47.1.+` (resolved online during build; lock before release)
- Java: `17`
- Input semantics: identical
- Inventory controller geometry: identical
- Network rule: client sends only slot/index; server validates and mutates inventory

## API family

`legacy-nbt`: storage/packet/item hooks need the 1.20 compatibility adapter before release builds.
