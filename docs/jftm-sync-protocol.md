# JFTM 地图同步

## 功能

JFTM 是一个服务端地图元数据同步功能，负责：

- 扫描并读取服务器保存的地图；
- 缓存地图编号、维度、中心坐标、缩放等级和锁定状态；
- 提供地图元数据同步；
- 在新地图产生后提供增量数据。

地图分类信息包括：

- 是否存在探索类标记；
- 旗帜标记及其位置、颜色和名称。

首次同步会获取完整地图列表，后续同步只获取新增地图。客户端可以在打开地图或需要刷新时发起同步请求。

Carpet 规则 `jftMapSyncProtocol` 用于控制功能总开关，默认关闭。管理员可以使用 [`/jft-sync`](command.md) 命令查看和修改同步配置。

## 配置文件

配置文件路径：

```text
config/carpet-jft-addition/carpetjftaddition-jftm.json
```

首次启用服务端并启动世界时，如果配置文件不存在，模组会自动创建默认配置：

```json
{
  "allowAllPlayers": true,
  "maxMapsPerSync": 128,
  "allowAllDimensions": true,
  "requestCooldownTicks": 40
}
```

### `allowAllPlayers`

是否允许所有玩家请求地图同步。

- `true`：所有玩家都可以请求同步；
- `false`：只允许权限等级 2 及以上的玩家请求同步；
- 默认值：`true`。

### `maxMapsPerSync`

单个同步批次发送的最大地图数量。地图较多时会拆分为多个批次。

- 范围：`1`–`256`；
- 默认值：`128`。

### `allowAllDimensions`

是否允许同步当前玩家所在维度之外的地图。

- `true`：允许同步所有维度的地图；
- `false`：只同步玩家当前维度的地图；
- 默认值：`true`。

### `requestCooldownTicks`

同一玩家两次同步请求之间的最小间隔，单位为游戏刻（tick）。

- 范围：`0`–`72000`；
- 默认值：`40`，约 2 秒；
- `0`：不设置请求冷却时间。

配置文件中的数值超出允许范围时，会恢复为对应的默认值。
