# CraftTweaker Integration / CraftTweaker 集成

Tiny Mob Farm CE exposes a CraftTweaker API for replacing farm drops by mob registry name.

中文：Tiny Mob Farm CE 提供 CraftTweaker API，可按生物注册名替换刷怪场产物。

## Import / 导入

```zenscript
import mods.tinymobfarm.MobDrops;
```

## Behavior / 行为

- Rules are matched by mob registry name, for example `minecraft:zombie`.
- If one or more rules match a mob, the CraftTweaker result replaces the normal loot table result.
- If no rule matches, the farm uses the original loot table behavior.
- Multiple rules for the same mob are allowed.
- Each rule is rolled independently.
- All matching rules run in registration order and their drops are combined. There is no priority value and no first-match-only mode.
- Pending drops are stored after generation. Output retries do not reroll chances.

中文：

- 规则按生物注册名匹配，例如 `minecraft:zombie`。
- 只要某个生物存在 CraftTweaker 规则，就使用 CraftTweaker 产物替代原本战利品表产物。
- 没有规则命中时，仍使用原本战利品表逻辑。
- 同一生物允许写多条规则。
- 每条规则独立判定。
- 同一生物的全部匹配规则会按注册顺序执行并合并产物；没有优先级参数，也不会只执行第一条。
- 产物生成后会保存待输出产物；输出重试不会重新判定概率。

## API / 接口

### Add Fixed Drops / 添加固定产物

```zenscript
MobDrops.add("minecraft:zombie", <item:minecraft:rotten_flesh> * 16);
```

Equivalent to 100% chance.

等价于 100% 概率。

### Add Fixed Chance / 添加固定概率

```zenscript
MobDrops.add("minecraft:zombie", <item:minecraft:iron_ingot>, 5);
```

This rolls one 5% chance for one iron ingot.

这会对一个铁锭进行一次 5% 概率判定。

### Add Chance Range / 添加概率范围

```zenscript
MobDrops.add("minecraft:zombie", <item:minecraft:rotten_flesh> * 16, 60, 80);
```

Each production cycle first picks a chance from 60 to 80, then rolls with that chance.

每次生产时先在 60 到 80 之间随机取一个概率，再按该概率判定是否产出。

### Add Amount Range With Fixed Chance / 数量范围 + 固定概率

```zenscript
MobDrops.add("minecraft:zombie", <item:minecraft:rotten_flesh>, 1, 3, 60);
```

If the 60% chance succeeds, the amount is randomly selected from 1 to 3.

如果 60% 概率成功，则数量在 1 到 3 之间随机。

### Multiple Independent Rolls / 多次独立判定

```zenscript
MobDrops.add("minecraft:zombie", <item:minecraft:rotten_flesh> * 16, 100);
MobDrops.add("minecraft:zombie", <item:minecraft:rotten_flesh> * 1, 20);
MobDrops.add("minecraft:zombie", <item:minecraft:rotten_flesh> * 1, 30);
```

Result:

- Always produces 16 rotten flesh.
- Has one independent 20% roll for 1 extra rotten flesh.
- Has one independent 30% roll for 1 extra rotten flesh.
- Minimum: 16.
- Maximum: 18.

结果：

- 固定产出 16 个腐肉。
- 额外进行一次 20% 的 1 个腐肉判定。
- 额外进行一次 30% 的 1 个腐肉判定。
- 最少 16 个。
- 最多 18 个。

### Item Arrays / 物品数组

```zenscript
MobDrops.add("minecraft:skeleton", [
    <item:minecraft:bone> * 2,
    <item:minecraft:arrow> * 2
]);
```

### Item Arrays With Shared Amount Range / 物品数组 + 共享数量范围

```zenscript
MobDrops.addChance("minecraft:skeleton", [
    <item:minecraft:bone>,
    <item:minecraft:arrow>
], 1, 3, 50);
```

Each item in the array gets one independent 50% roll. If the roll succeeds, that item's amount is randomly selected from 1 to 3.

数组中的每个物品都会进行一次独立的 50% 判定。判定成功时，该物品数量会在 1 到 3 之间随机。

### Remove Rules / 移除规则

```zenscript
MobDrops.remove("minecraft:zombie");
```

Removes all custom rules for the mob.

移除该生物的全部自定义规则。

### Clear All Rules / 清空全部规则

```zenscript
MobDrops.clear();
```

## Practical Notes / 实用说明

- Use `<item> * amount` when the single-roll amount should be fixed.
- Use multiple `add` calls when you need multiple independent rolls.
- Use amount ranges only when random stack size is intentional.
- Avoid combining too many random dimensions in one rule; separate rules are easier to reason about.
