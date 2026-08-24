# MapCatalog 客户端对接

客户端模组负责注册客户端 Payload、发起同步请求、校验同步事务并缓存地图元数据。服务端模组不包含客户端代码，也不负责 Xaero 的绘制和分组。

## Payload ID

使用以下固定 ID：

```text
mapcatalog:sync_request
mapcatalog:sync_start
mapcatalog:sync_batch
mapcatalog:sync_end
```

客户端和服务端必须使用相同的协议版本 `2`、字段顺序和枚举顺序。协议 v2 与 v1 的字段结构不兼容，客户端和服务端应同步升级；对于能够成功解码但版本号不匹配的请求，服务端返回 `DENIED`。

## 请求字段

`sync_request` 为客户端发送到服务端的 C2S Payload：

```text
protocolVersion: VarInt
worldSessionId: UUID
knownCatalogRevision: Long (固定 8 字节)
forceFullSync: Boolean
```

首次连接、世界会话改变、主动刷新或怀疑缓存不完整时，将 `forceFullSync` 设为 `true`。正常增量同步使用当前缓存的 `catalogRevision`。

## 服务端响应

`sync_start`：

```text
syncMode: FULL | DELTA | NO_CHANGE | DENIED
worldSessionId: UUID
catalogRevision: Long (固定 8 字节)
highestMapId: VarInt
entryCount: VarInt
```

当使用包含内嵌 MapCatalog 的 Carpet JFT Addition 发行版且 `mapSyncEnabled` 配置关闭，或 Carpet Rule `jftMapSyncProtocol` 关闭时，服务端返回 `DENIED`。单独安装 MapCatalog 时不会读取 Carpet Rule。客户端应静默停用本次同步并记录日志，不应连续重试。

`sync_batch` 包含一个地图元数据列表，每批最多 256 条；`sync_end` 携带 `worldSessionId`、`catalogRevision` 和 `highestMapId`。客户端只有收到 `sync_end` 后，才应提交本次全量或增量缓存。

增量同步包含以下两类数据：

- 新产生的地图；
- 已有地图的元数据或旗帜发生变化的地图。

客户端应按 `mapId` 覆盖本地旧条目，而不是仅追加新条目。旗帜的新增、删除、颜色变化和名称变化都会使对应地图进入下一次增量同步。

每个地图条目包含：

```text
mapId, dimension, centerX, centerZ, scale, locked,
hasExplorationMarker, banners[]
```

其中每个旗帜包含世界坐标、染料颜色和可选名称。
