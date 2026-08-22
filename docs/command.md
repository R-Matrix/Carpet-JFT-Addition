## 命令

**提示：JFTM 相关命令仅限管理员使用，需要权限等级 2。**

## JFTM 地图同步 (`/jft-sync`)

用于查看和修改 JFTM 地图元数据同步协议的服务端配置。

配置文件位于：

```text
config/carpet-jft-addition/carpetjftaddition-jftm.json
```

Carpet 规则 `jftMapSyncProtocol` 用于控制同步协议总开关；`/jft-sync` 命令用于管理配置文件中的选项。

### 语法

- `/jft-sync`
    - `status`
    - `reload`
    - `get`
        - `allowAllPlayers`
        - `maxMapsPerSync`
        - `allowAllDimensions`
        - `requestCooldownTicks`
    - `set`
        - `allowAllPlayers <true|false>`
        - `maxMapsPerSync <1-256>`
        - `allowAllDimensions <true|false>`
        - `requestCooldownTicks <0-72000>`

### 效果

- `/jft-sync status` 查看当前 JFTM 协议状态、地图缓存数量、最高地图编号、世界会话编号和同步配置。
- `/jft-sync reload` 重新读取 JFTM 配置文件，适合管理员手动修改 JSON 后使用。
- `/jft-sync get <key>` 查看指定配置项的当前值。
- `/jft-sync set <key> <value>` 修改指定配置项并立即保存到配置文件。

#### allowAllPlayers

是否允许所有玩家请求地图同步。

- `true`：所有玩家都可以请求同步；
- `false`：只允许权限等级 2 及以上的玩家请求同步；
- 默认值：`true`。

#### maxMapsPerSync

单个地图批次最多发送的地图条目数量。地图数量超过该值时会拆分成多个批次发送。

- 范围：`1`–`256`；
- 默认值：`128`；
- 该值限制的是每批数量，不是一次同步的总地图数量。

#### allowAllDimensions

是否允许同步当前玩家所在维度之外的地图。

- `true`：发送所有维度的地图；
- `false`：只发送玩家当前维度的地图；
- 默认值：`true`。

#### requestCooldownTicks

同一玩家两次同步请求之间的最小间隔，单位为游戏刻（tick）。

- 范围：`0`–`72000`；
- 默认值：`40`，约 2 秒；
- `0`：不设置请求冷却时间。
