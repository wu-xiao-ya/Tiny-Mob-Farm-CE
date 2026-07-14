## 中文

本次 `1.0.9-beta` 发布优化了外部配置热重载。配置文件检查间隔由 20 tick 调整为 200 tick（正常 TPS 下约 10 秒），并将两次文件属性查询合并为一次 NIO 属性读取，显著减少主线程文件系统调用。

同时修复了单人游戏中客户端 Tick 与集成服务端 Tick 共用计数器引起的重复检查和跨线程竞争。客户端与独立服务端现在各自只使用符合物理端的一条轮询路径。配置界面修改仍会立即同步，外部修改配置文件后仍会自动重载。

## English

This `1.0.9-beta` release optimizes external config hot reloads. File polling now runs every 200 ticks instead of every 20 ticks, approximately once every 10 seconds at normal TPS, and two file metadata calls have been consolidated into one NIO attribute read to reduce main-thread filesystem access.

It also fixes duplicate polling and a shared-counter race between client ticks and integrated-server ticks. Physical clients and dedicated servers now use only their appropriate polling path. Config-screen changes still synchronize immediately, and external file edits are still reloaded automatically.

