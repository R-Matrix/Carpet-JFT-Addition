# 规则

**提示：可以使用`Ctrl+F`快速查找自己想要的规则**


## 控制引雷附魔天气要求 (channelingWeather)

控制三叉戟引雷附魔的天气要求

或许可以用在成就获取?远古守卫者农场?头颅农场?

规则设置为 VANILLA  为原版表现  
规则设置为 RAINING  为下雨即可引雷(同基岩版表现)  
规则设置为 ANY      为无视天气引雷  
规则设置为 DISABLED 为禁用引雷附魔引雷

- 类型: `string`
- 默认值: `VANILLA`
- 参考选项: `VANILLA`, `RAINING`, `ANY`, `DISABLED`
- 分类: `JFT`, `SURVIVAL`


## 控制引雷附魔露天判断要求 (channelingCanSeeSky)

控制三叉戟引雷附魔的露天判断要求

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `true`, `false`
- 分类: `JFT`, `SURVIVAL`


## 控制激流是否要求玩家接触水 (riptideTouchingWater)

控制激流使用是否要求玩家接触水

激流史诗级加强(bushi

***貌似需要客户端和服务端均安装该拓展才可正常使用***

规则设置为 VANILLA      为原版表现, 即玩家必须接触水(触碰水或淋雨)  
规则设置为 WATERRorLAVA 为碰水或碰岩浆均可使用  
规则设置为 ANY          为无视任何条件均可使用激流  
规则设置为 DISABLED     为禁止玩家使用激流三叉戟  

- 类型: `string`
- 默认值: `VANILLA`
- 参考选项: `VANILLA`, `WATERRorLAVA`, `ANY`, `DISABLED`
- 分类: `JFT`, `SURVIVAL`


## 修改使用三叉戟需求蓄力时间 (changeUseTridentTimeTicks)

修改使用三叉戟需求的蓄力时间, 单位为游戏刻(gt), 默认为10gt(0.5s)

对可投掷的三叉戟和不可投掷的三叉戟(激流)均生效

由于三叉戟蓄力动画为10gt, 因此当设置时间超过10时, 动画结束后再蓄力一段时间才可使用

配合规则 `riptideTouchingWater` 有神奇的效果

如果使用激流三叉戟配合绗棉的规则建议不要设置得太短, 否则速度过快容易创死或加载不出地形, 导致服务器大量卡顿

不要利用此大范围跑图, 容易崩(

- 类型: `int`
- 默认值: `10`
- 参考选项: `10`, `5`, `0`
- 分类: `JFT`, `SURVIVAL`


## 溺尸进行僵尸增援时生成僵尸而非溺尸 (drownReinforcementCanSpawnZombie)

设置溺尸进行僵尸增援时生成僵尸还是非溺尸

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `JFT`, `SURVIVAL`


## 溺尸进行僵尸增援时生成僵尸而非溺尸 (drownReinforcementCanSpawnZombie)

设置溺尸进行僵尸增援时生成僵尸还是非溺尸

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `JFT`, `SURVIVAL`


## 修改溺尸生成时携带装备的概率 (drownSpawnHasEquipments)

修改溺尸生成时携带装备的概率

溺尸可能生成时携带装备, 三叉戟或钓鱼竿

可修改的取值为 0-1, 1 代表一定生成, -1 代表原版表现

- 类型: `double`
- 默认值: `-1.0`
- 参考选项: `-1.0`, `0`, `0.5`, `1.0`
- 分类: `JFT`, `SURVIVAL`


## 修改僵尸类生物生成时的初始增援能力 (reinforcementAttributeInit)

修改僵尸类生物生成时的初始增援能力(属性)

修改后仅影响新生成的僵尸类生物

可修改的取值为 0-1, -1 代表原版表现

- 类型: `double`
- 默认值: `-1.0`
- 参考选项: `-1.0`, `0`, `0.5`, `1.0`
- 分类: `JFT`, `SURVIVAL`


## 控制玩家是否可以捡起箭矢类物品(草船借箭) (canPlayerPickUpLikeArrows)

控制玩家是否可以捡起来自(骷髅, 溺尸)的箭矢类物品(箭矢, 三叉戟 etc.)

mc 版的草船借箭(

由于某些原因, 由溺尸扔出的三叉戟只有其溺尸死亡后方可被玩家捡起.

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `JFT`, `SURVIVAL`, `FEATURE`


## 允许三叉戟多段攻击 (tridentMultipleDamage)

重新引入25w41a - 25w43a 的三叉戟 "帧伤" 和 "群伤"

参考 [全新BUG！群伤+帧伤三叉戟 Minecraft 25w41a JAVA版](https://www.bilibili.com/video/BV1ri4JzCEqs/?share_source=copy_web&vd_source=2944450cffb6364cc71d92cc4ae74ad5)

JAVI玩家再也不用羡慕BE玩家的三叉戟搅拌机了!(滑稽)

居然还能重新引入修复的==恶性bug==的(bushi

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `JFT`, `SURVIVAL`, `FEATURE`


## 穿刺接触水即可生效 (impalingWaterContact)

修改穿刺附魔的效果触发条件，像基岩版那样, 从"仅对水生生物生效"改为"任何生物接触水即生效"

当目标实体接触水时，穿刺附魔即可造成额外伤害，使该附魔在水下战斗中更加实用. 穿刺V可打出21.5的伤害!

此规则改变了原版Java版中穿刺附魔仅对特定水生生物生效的限制，使其行为更接近某些自定义战斗场景的需求

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `JFT`, `SURVIVAL`