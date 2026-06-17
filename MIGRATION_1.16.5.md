# Tiny Mob Farm CE 1.16.5 Migration

This branch contains the Minecraft Forge 1.16.5 porting work.

## Scope

The 1.16.5 version should preserve the 1.12.2 feature set where practical:

- Lasso capture and release.
- Eight mob farm tiers, with normal and mechanical variants.
- Captured-mob farm production.
- Vanilla loot table fallback.
- Custom drop rule system.
- Output to adjacent item handlers.
- Pause-on-full-output behavior and retry interval.
- Redstone disable behavior.
- Farm GUI and lasso-only slot.
- Captured mob rendering inside farms.
- English, Simplified Chinese, and Russian localization.

## Initial Porting Policy

The branch first establishes a Forge 1.16.5 buildable baseline:

- ForgeGradle 4.x.
- Minecraft Forge 1.16.5.
- `META-INF/mods.toml`.
- `pack_format` 6.
- JSON language files.

The 1.12.2 Java implementation is intentionally not compiled in this baseline because it depends on removed 1.12.2 APIs. Core gameplay code will be ported back in controlled slices.

## CraftTweaker

CraftTweaker support has been ported to the 1.16.5 CraftTweaker 7 API as an optional dependency. Scripts use:

```zenscript
import mods.tinymobfarm.MobDrops;
```

When a rule matches the captured mob registry name, the scripted drops replace the vanilla loot table result for that production cycle. If no rule matches, the farm falls back to the vanilla loot table behavior.

The 1.16.5 branch supports the same custom-drop concepts as the mature 1.12.2 line:

- fixed stack amounts using `<item> * amount`;
- fixed chances;
- chance ranges;
- amount ranges when explicitly requested;
- multiple independent rules for the same mob;
- `remove` and `clear` rule management.

See `docs/crafttweaker.md` for script examples.

## High-Risk Porting Areas

- Block entity data storage and synchronization.
- `LazyOptional` capability exposure for item handlers.
- Loot table generation using 1.16.5 loot parameters.
- Menu/screen replacement for the old `IGuiHandler`.
- Deferred registration for blocks, items, menu types, and block entity types.
- Captured entity rendering.
