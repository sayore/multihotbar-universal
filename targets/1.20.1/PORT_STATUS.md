# Port status: Minecraft 1.20.1

This target shares the MultiHotbar interaction contract with 1.21.1.

- Fabric API: `0.92.6+1.20.1`
- Forge: `47.4.23`
- Java: `17`
- Input semantics: identical
- Inventory controller geometry: identical
- Network rule: client sends only slot/index; server validates and mutates inventory

## API family

`legacy-nbt`: the storage, packet, and item hooks use the 1.20.1 APIs. Both Fabric and Forge release JARs build at version `0.2.2`.
