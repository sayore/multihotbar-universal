# Architecture

## Layers

```text
shared/
  core/                  pure Java state machine + slot geometry

targets/<mc>/
  common/                Minecraft-facing inventory/item logic
  fabric/                registration + packet transport
  forge/                 Forge 1.20.1 registration + packet transport
  neoforge/              registration + packet transport on newer targets
```

## Interfaces

```text
press(hotbarSlot, now, depth) -> index
controllerSlot(hotbarSlot) -> inventory slot
select(player, hotbarSlot, targetIndex)
restoreAll(player)
```

Network endpoint semantics:

```text
C2S SELECT      { hotbarSlot:u8, index:u8 }
C2S RESTORE_ALL {}
```

The server is authoritative and rejects invalid slot/index/controller combinations.

## Client preview

`MultiHotbarClientState` owns the short-lived `PreviewState`. A valid local number-key selection captures the controller's ordered items and the paired hotbar stack. Further taps on that slot reuse the snapshot, so the slice advances immediately while the server inventory update is in flight. Opening a screen resets both the press chain and preview. Render hooks on each loader consume this state and draw only the icons; they never select items or send packets. The preview adds no network message.

## Version families

- `1.20.x`: NBT-era item persistence; Java 17.
- `1.21.x`: Data Components-era persistence; Java 21.
- `1.21.1`: canonical source target.

The interaction model must not diverge between families. Only storage, mappings, registration and packet glue may differ.
