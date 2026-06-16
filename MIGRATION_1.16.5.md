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

CraftTweaker support is not part of the first 1.16.5 baseline. The 1.12.2 integration depends on CraftTweaker 2 APIs. For 1.16.5, the preferred path is:

1. Port the internal drop rule system.
2. Add JSON or internal registration support.
3. Add a CraftTweaker/KubeJS compatibility layer after the core behavior is stable.

## High-Risk Porting Areas

- Block entity data storage and synchronization.
- `LazyOptional` capability exposure for item handlers.
- Loot table generation using 1.16.5 loot parameters.
- Menu/screen replacement for the old `IGuiHandler`.
- Deferred registration for blocks, items, menu types, and block entity types.
- Captured entity rendering.

