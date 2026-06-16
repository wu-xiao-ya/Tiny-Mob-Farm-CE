# Tiny Mob Farm CE

Tiny Mob Farm CE is a Minecraft 1.12.2 Forge mod that adds compact single-block mob farms. Captured mobs are stored in lassos, and farms passively generate drops based on the captured mob.

中文说明：Tiny Mob Farm CE 是一个 Minecraft 1.12.2 Forge 模组，提供单方块刷怪场。玩家使用套索捕捉生物后，刷怪场会根据套索中的生物被动生成产物。

## Requirements / 需求

- Minecraft 1.12.2
- Minecraft Forge for 1.12.2
- Java 8
- CraftTweaker 2 is optional and only needed for custom scripted drops.

## Main Features / 主要功能

- Multiple farm tiers with different production speeds and lasso durability costs.
- Mechanical farms support item pipe automation for the lasso slot.
- Captured mobs normally use their vanilla loot table as the production source.
- Optional CraftTweaker integration for custom mob-specific farm drops.
- Output-full protection can pause production without consuming lasso durability.
- Configurable retry interval for full-output situations.

中文：

- 多等级刷怪场，不同等级拥有不同产出速度和套索耐久消耗。
- 机械版刷怪场支持通过管道输入/输出套索槽。
- 默认情况下，被捕捉生物使用其原版战利品表作为产出来源。
- 可选 CraftTweaker 集成，可按生物注册名自定义农场产物。
- 输出容器已满时可暂停生产，避免吞物品或错误消耗套索耐久。
- 输出失败后的重试间隔可配置。

## CraftTweaker Custom Drops / CraftTweaker 自定义产物

Import:

```zenscript
import mods.tinymobfarm.MobDrops;
```

Fixed amount, fixed chance:

```zenscript
MobDrops.add("minecraft:zombie", <minecraft:rotten_flesh> * 16, 100);
```

Fixed amount, chance range:

```zenscript
MobDrops.add("minecraft:zombie", <minecraft:rotten_flesh> * 16, 60, 80);
```

Multiple independent rolls:

```zenscript
MobDrops.add("minecraft:zombie", <minecraft:rotten_flesh> * 16, 100);
MobDrops.add("minecraft:zombie", <minecraft:rotten_flesh> * 1, 20);
MobDrops.add("minecraft:zombie", <minecraft:rotten_flesh> * 1, 30);
```

The example above always produces 16 rotten flesh, then independently rolls a 20% extra drop and a 30% extra drop.

中文说明：

- 同一个生物可以注册多条规则。
- 每条规则独立判定。
- 只要 CraftTweaker 规则命中该生物，就会替代原本的战利品表产物。
- 如果没有 CraftTweaker 规则命中，则回退到原版战利品表逻辑。
- 输出失败后保存的是已经判定好的本轮产物，后续重试不会重新抽概率。

For full examples, see [docs/crafttweaker.md](docs/crafttweaker.md).

## Configuration / 配置

Important config values:

- `Mob Farm Rate`: production interval for each farm tier, in seconds.
- `Pause When Output Full`: when enabled, farms wait if generated drops cannot fully fit into adjacent inventories.
- `Output Retry Interval Ticks`: retry interval after output fails. Default: `20`. Range: `1` to `3600`.
- `Mob Blacklist`: mob registry names that cannot be captured.

## Builds / 构建

Local build:

```powershell
.\gradlew.bat build
```

GitHub Releases are built by GitHub Actions on version tags such as `v1.0.7`.

Latest release:

- https://github.com/wu-xiao-ya/Tiny-Mob-Farm-CE/releases/latest

## Notes / 注意事项

- This project is a community edition fork.
- CraftTweaker support is designed for modpack-side customization.
- Custom CraftTweaker drops are farm outputs, not entity kill events.
- Default loot-table behavior is preserved when no custom rule matches.

