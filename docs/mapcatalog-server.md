# MapCatalog 服务端同步

## 功能

MapCatalog 是独立的 Fabric 服务端模组，负责扫描服务器保存的地图、缓存地图元数据，并通过同步协议向客户端提供地图目录。

MapCatalog 同时有两种发布形式：

- 独立的 `mapcatalog-server` JAR，适用于不安装 Carpet JFT Addition 的服务器；
- 嵌套在 Carpet JFT Addition 内的 MapCatalog JAR，安装 Carpet JFT Addition 后无需再次安装独立版。

两种形式使用相同的模组 ID `mapcatalog`，因此不要同时安装独立版和内嵌版。

服务端只发送以下元数据：

- 地图编号、维度、中心坐标、缩放等级和锁定状态；
- 是否存在探索类标记；
- 旗帜标记的位置、颜色和名称。

服务端不发送地图颜色数组、完整 `.dat` 文件或 Xaero 地图显示元素。首次同步发送全量目录，后续同步根据客户端已知的最大地图编号发送增量数据。地图状态在服务器启动时扫描，之后通过原版 `putMapState` 入口更新缓存。

安装独立版或包含内嵌版的 Carpet JFT Addition 后，配置文件为：

```text
config/carpet-jft-addition/mapcatalog.json
```

管理命令见 [`command.md`](command.md)。

## 配置字段

```json
{
  "mapSyncEnabled": true,
  "allowAllPlayers": true,
  "maxMapsPerSync": 128,
  "allowAllDimensions": true,
  "requestCooldownTicks": 40
}
```

### `mapSyncEnabled`

是否启用地图同步协议。关闭时，服务端返回 `DENIED`，客户端应静默停用同步功能。

默认值：`true`。

### `allowAllPlayers`

是否允许所有玩家请求同步。设为 `false` 时，仅权限等级 2 及以上的玩家可以请求。

默认值：`true`。

### `maxMapsPerSync`

单个同步批次最多包含的地图条目数，范围为 `1`–`256`。这只限制批次大小，不限制一次同步的总地图数量。

默认值：`128`。

### `allowAllDimensions`

是否发送所有维度的地图。设为 `false` 时，只发送玩家当前维度的地图。

默认值：`true`。

### `requestCooldownTicks`

同一玩家两次同步请求之间的最小间隔，单位为游戏刻，范围为 `0`–`72000`。设为 `0` 表示不限制请求间隔。

默认值：`40`。

## Carpet Rule 联动

当使用包含内嵌 MapCatalog 的 Carpet JFT Addition 发行版时，Carpet JFT Addition 的规则
`jftMapSyncProtocol` 是地图同步协议的额外总开关：

- 规则为 `true` 且 `mapSyncEnabled` 为 `true` 时，允许客户端请求同步；
- 任一开关为 `false` 时，服务端返回 `DENIED`；
- 关闭规则不会删除地图文件，也不会清空服务端缓存；
- 单独安装 MapCatalog 时不会读取该规则，MapCatalog 仅受自身配置控制，配置文件中也不会出现 `jftMapSyncProtocol` 字段。

规则默认值为 `false`，可使用 Carpet 的规则命令修改。
