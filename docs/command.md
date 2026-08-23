## 命令

MapCatalog 服务端模组注册 `/mapcatalog` 命令，仅权限等级 2 及以上的管理员可用。

### 语法

- `/mapcatalog status`
- `/mapcatalog reload`
- `/mapcatalog get <key>`
- `/mapcatalog set <key> <value>`

可用配置项：

- `mapSyncEnabled <true|false>`
- `allowAllPlayers <true|false>`
- `maxMapsPerSync <1-256>`
- `allowAllDimensions <true|false>`
- `requestCooldownTicks <0-72000>`

### 作用

- `status`：查看实际同步状态、`mapSyncEnabled`、Carpet Rule `jftMapSyncProtocol`、地图缓存数量、最高地图编号、世界会话编号和配置值。
- `reload`：重新读取 JSON 配置文件。
- `get`：查看指定配置项。
- `set`：修改配置项并立即保存。

配置文件位于：

```text
config/carpet-jft-addition/mapcatalog.json
```
