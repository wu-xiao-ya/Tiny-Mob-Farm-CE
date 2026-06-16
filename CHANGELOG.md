# Changelog

## 1.0.7

### 中文

- 新增 CraftTweaker 自定义产物系统。
- 支持按生物注册名添加、移除、清空农场产物规则。
- 支持固定概率、概率范围、固定数量、数量范围和多条独立判定。
- CraftTweaker 规则命中时替代原本战利品表产物；未命中时保留原逻辑。
- 新增输出失败重试间隔配置：`Output Retry Interval Ticks`，默认 20 tick，范围 1 到 3600 tick。
- 输出满时保留待输出产物，重试时不会重新抽概率。
- 产物输出前会合并可堆叠物品，减少相邻容器模拟和插入压力。
- 缓存掉落来源，套索槽和 CraftTweaker 规则未变化时不重复解析。
- 新增 GitHub Actions 发布流程。
- 升级 RetroFuturaGradle 插件到 1.4.1，保证 GitHub Actions 可解析构建依赖。

### English

- Added CraftTweaker custom farm drops.
- Added mob-registry-name based add, remove, and clear rules.
- Added support for fixed chances, chance ranges, fixed amounts, amount ranges, and multiple independent rolls.
- CraftTweaker rules replace loot table output when matched; unmatched mobs preserve the original behavior.
- Added `Output Retry Interval Ticks`, defaulting to 20 ticks with a valid range of 1 to 3600 ticks.
- Full-output retries preserve pending drops and do not reroll chances.
- Drops are compacted before output to reduce adjacent inventory simulation and insertion overhead.
- Drop sources are cached when the lasso slot and CraftTweaker rules are unchanged.
- Added GitHub Actions release workflow.
- Updated RetroFuturaGradle to 1.4.1 so GitHub Actions can resolve the build plugin.

