# MultiHotbar 1.21.5 — architecture

## Inventory contract

For hotbar index `h` (`0..8`) the controller is the inventory slot `27 + h`, i.e. the slot visually directly above it in the player's bottom inventory row.

```text
inventory bottom row: [27][28][29][30][31][32][33][34][35]
                       |   |   |   |   |   |   |   |   |
hotbar:               [ 0][ 1][ 2][ 3][ 4][ 5][ 6][ 7][ 8]
```

If that upper slot is not a `HotbarBundleItem`, MultiHotbar does nothing and vanilla keeps the key press.

## Logical group / hole invariant

Each Hotbar Bundle has 9 logical positions. Position 0 is the normal hotbar item; positions 1..8 are alternates. The currently active logical item physically lives in the paired vanilla hotbar slot and its corresponding container position is an empty **hole**.

Example while logical index 2 is active:

```text
logical:   [ sword ][ pickaxe ][ axe ][ shovel ]
storage:   [ sword ][ pickaxe ][  EMPTY ][ shovel ]
hotbar:                           axe
active=2
```

Switch 2 -> 1 is an atomic server operation:

1. current hotbar stack -> storage[2]
2. storage[1] -> hotbar
3. storage[1] -> EMPTY
4. active -> 1

This preserves a stable order without proxy items or fake copies.

## Input

`Minecraft#handleKeybinds` is mixed into at HEAD on the client. Only mounted groups consume the vanilla hotbar key's queued press. Unmounted slots are untouched.

- first press: index 0
- next press of the same hotbar key within 200 ms: index + 1
- every successful press restarts the 200 ms window
- index clamps at the group's configured depth
- movement does not cancel the chain
- opening any `Screen` resets the chain and requests server normalization
- while a screen is open, MultiHotbar does not consume 1..9, so vanilla inventory `SWAP` behavior remains available

## Networking / authorization

The client sends only `{ action, hotbarSlot, index }`. It never sends an ItemStack and cannot invent an item.

The server:

1. validates `hotbarSlot` and `index`
2. resolves controller from the player's real inventory
3. verifies it is a Hotbar Bundle
4. verifies the target logical slot actually contains an item
5. performs the swap against the authoritative player inventory
6. broadcasts inventory changes

There is no account/auth layer. Multiplayer authorization is Minecraft's existing authenticated player connection plus server-authoritative inventory validation.

## Loaders

`common/` contains item state, server swap logic, client preview state and renderer, and the client mixin. `fabric/` and `neoforge/` contain registration, HUD hooks, and network transport glue.

## Known v0.1 tradeoff

The custom Hotbar Bundle stores up to eight alternate stacks. Unlike a vanilla Bundle, that permits multiple non-stackable tools, which means it also increases effective inventory capacity. A strict no-extra-storage mode must instead reference items stored elsewhere in the inventory or accept vanilla Bundle capacity rules.
