# JFTM-同步协议（服务端协议）

本协议首版针对 Minecraft 1.21.4，服务端只发送地图元数据，不发送颜色数组或完整地图文件。

## 开关与配置

Carpet 规则：

```text
jftMapSyncProtocol
```

配置文件：

```text
config/carpet-jft-addition/carpetjftaddition-jftm.json
```

默认配置：

```json
{
  "allowAllPlayers": true,
  "maxMapsPerSync": 128,
  "allowAllDimensions": true,
  "requestCooldownTicks": 40
}
```

只有管理员（权限等级 2）可以使用：

```text
/jft-sync status
/jft-sync reload
/jft-sync get <key>
/jft-sync set <key> <value>
```

## Payload ID

源码中的内部字段和方法使用 `jft$` 前缀；Minecraft `Identifier` 不允许 `$`，因此网络路径使用 `jftm_` 前缀。

```text
carpetjftaddition:jftm_sync_request
carpetjftaddition:jftm_sync_start
carpetjftaddition:jftm_sync_batch
carpetjftaddition:jftm_sync_end
```

## 客户端请求

`MapSyncRequestC2S`：

```text
protocolVersion : VarInt
worldSessionId  : UUID（两个 Long）
knownMaxMapId   : VarInt
forceFullSync   : Boolean
```

首次连接没有 session 时，客户端发送全零 UUID 和 `knownMaxMapId = -1` 即可。

## 服务端响应

`MapSyncStartS2C`：

```text
syncMode       : VarInt
worldSessionId : UUID（两个 Long）
highestMapId   : VarInt
entryCount     : VarInt
```

`syncMode` 枚举顺序：

```text
0 FULL
1 DELTA
2 NO_CHANGE
3 DENIED
```

`MapSyncBatchS2C` 每批最多发送 `maxMapsPerSync` 条地图，协议硬上限为 256 条。

`MapSyncEndS2C`：

```text
worldSessionId : UUID（两个 Long）
highestMapId   : VarInt
```

客户端收到结束包之前不得替换当前缓存。

## ServerMapInfo

```text
mapId         : VarInt
dimension     : Identifier
centerX       : Int
centerZ       : Int
scale         : Byte
locked        : Boolean
classification
```

`MapClassification` 只有两个字段：

```text
hasExplorationMarker : Boolean
banners              : List<BannerMarker>
```

`BannerMarker`：

```text
worldX : Int
worldZ : Int
color  : DyeColor 协议值
name   : Optional<Text>
```

`hasExplorationMarker` 只表示当前 `MapState` 是否存在探索类装饰物，不表示地图的真实创建来源。

## 同步行为

- session 不一致、强制刷新或首次请求：发送 `FULL`；
- session 一致且不是强制刷新：只发送 `mapId > knownMaxMapId` 的地图；
- 没有新地图：发送 `NO_CHANGE` 和结束包；
- Carpet 规则关闭、权限不足、协议版本不支持或请求过频：发送 `DENIED`；
- `allowAllDimensions=false` 时只发送玩家当前维度的地图；
- 服务端不主动推送新地图，客户端在打开地图时发起请求；
- 客户端可以按 `dimension + centerX + centerZ + scale` 分组显示。

