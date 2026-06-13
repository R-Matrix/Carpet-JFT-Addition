# 规则

**提示：可以使用`Ctrl+F`快速查找自己想要的规则**

---

## 控制引雷附魔天气要求 (channelingWeather)

控制三叉戟引雷附魔的天气要求

***没错, 这是Carpet JFT Addition 的第一条规则***

或许可以用在成就获取?远古守卫者农场?头颅农场?

规则设置为 VANILLA  为原版表现  
规则设置为 RAINING  为下雨即可引雷(同基岩版表现)  
规则设置为 ANY      为无视天气引雷  
规则设置为 DISABLED 为禁用引雷附魔引雷

- 类型: `string`
- 默认值: `VANILLA`
- 参考选项: `VANILLA`, `RAINING`, `ANY`, `DISABLED`
- 分类: `SURVIVAL`, `JFT`


## 控制引雷附魔露天判断要求 (channelingCanSeeSky)

控制三叉戟引雷附魔的露天判断要求

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `true`, `false`
- 分类: `SURVIVAL`, `JFT`


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
- 分类: `SURVIVAL`, `JFT`


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
- 分类: `SURVIVAL`, `JFT`


## 溺尸进行僵尸增援时生成僵尸而非溺尸 (drownedReinforcementCanSpawnZombie)

设置溺尸进行僵尸增援时生成僵尸还是非溺尸

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`


## 修改溺尸生成时携带装备的概率 (drownedSpawnHasEquipments)

修改溺尸生成时携带装备的概率

溺尸可能生成时携带装备, 三叉戟或钓鱼竿

可修改的取值为 0-1, 1 代表一定生成, -1 代表原版表现

- 类型: `double`
- 默认值: `-1.0`
- 参考选项: `-1.0`, `0`, `0.5`, `1.0`
- 分类: `SURVIVAL`, `JFT`


## 修改僵尸类生物生成时的初始增援能力 (reinforcementAttributeInit)

修改僵尸类生物生成时的初始增援能力(属性)

修改后仅影响新生成的僵尸类生物

可修改的取值为 0-1, -1 代表原版表现

- 类型: `double`
- 默认值: `-1.0`
- 参考选项: `-1.0`, `0`, `0.5`, `1.0`
- 分类: `SURVIVAL`, `JFT`


## 控制玩家是否可以捡起箭矢类物品(草船借箭) (canPlayerPickUpLikeArrows)

控制玩家是否可以捡起来自(骷髅, 溺尸)的箭矢类物品(箭矢, 三叉戟 etc.)

mc 版的草船借箭(

由于某些原因, 由溺尸扔出的三叉戟只有其溺尸死亡后方可被玩家捡起.

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`, `FEATURE`


## 允许三叉戟多段攻击 (tridentMultipleDamage)

重新引入25w41a - 25w43a 的三叉戟 "帧伤" 和 "群伤"

参考 [全新BUG！群伤+帧伤三叉戟 Minecraft 25w41a JAVA版](https://www.bilibili.com/video/BV1ri4JzCEqs/?share_source=copy_web&vd_source=2944450cffb6364cc71d92cc4ae74ad5)

JAVI玩家再也不用羡慕BE玩家的三叉戟搅拌机了!(滑稽)

~~居然还能重新引入修复的==恶性bug==的(bushi~~

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`, `FEATURE`


## 穿刺接触水即可生效 (impalingWaterContact)

修改穿刺附魔的效果触发条件，像基岩版那样, 从"仅对水生生物生效"改为"任何生物接触水即生效"

当目标实体接触水时，穿刺附魔即可造成额外伤害，使该附魔在水下战斗中更加实用. 穿刺V可打出21.5的伤害!

此规则改变了原版Java版中穿刺附魔仅对特定水生生物生效的限制，使其行为更接近某些自定义战斗场景的需求

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`


## 海龟海带喂食与胎生繁殖机制 (turtleKelpFeedingAndViviparousBreeding)

控制海龟是否可以通过喂食海带进行繁殖，并实现胎生机制而非产卵

海龟现在可以通过喂食海带来繁殖

繁殖后海龟会直接生下幼年海龟，而不是产下海龟蛋

同时, 在开启此规则后, 玩家手拿海带时海龟也能被吸引靠近, 包括小海龟, 但是小海龟只能喂食海草才能长大

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`


## 青蛙染料喂食与胎生繁殖机制 (frogDyeFeedingAndViviparousBreeding)

控制青蛙是否可以通过喂食染料进行繁殖，并实现胎生机制而非产卵

只能使用橙色, 绿色, 白色三种染料. 并且直接生出对应颜色的子蛙, 跳过产卵和蝌蚪阶段

开启规则后, 生成的青蛙在开启此规则后能被染料引诱靠近

:warning: : 由于注册了新的青蛙记忆任务(BrainTask)并且在青蛙初始化时就加入了青蛙的大脑 **(无论是否开启该规则)**, 所以如果在安装本模组后 **无论是否启用此规则, 青蛙表现异常或出现预期之外的现象**, 或是您有更好的实现方法, 欢迎[邮件交流](mailto:shudpaa@163.com)或者提issue, pr

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`


## 控制紫水晶母岩是否能在水中生长出紫水晶 (allowAmethystBudCanGrowAtWater)

控制紫水晶母岩是否能在水中生长出紫水晶

设置为 `false` 后, 紫水晶母岩仅能在表面为空气时长出紫水晶

可以防止在无侧沟世吞中由于紫水晶母岩生长的紫水晶破坏水墙

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`


## 紫水晶簇活塞表现正常 (amethystPistonBehaviorNormal)

紫水晶簇和小型紫水晶在被活塞推拉时活塞其行为表现为正常方块而不是被破坏

使得紫水晶簇和小型紫水晶回退到21w11a前紫水晶簇和紫水晶芽类方块的表现, 这也使得紫水晶复制bug再现

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `true`, `false`
- 分类: `SURVIVAL`, `JFT`


## 发光地衣可吞噬方块 (glowLichenCanShadowBlocks)

当活塞推动一个方块时，如果该方块的推动方向那一面附着有发光地衣, 发光地衣到位处的方块会消失.

发光地衣到位后仍会产生更新

只能是推动方向,  拉回方向发光地衣会被破坏

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`, `FEATURE`


## 告示牌放下后文字总发光 (signTextGlowingAlways)

放下的告示牌文字总是使用荧光墨囊那样高亮显示

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`


## 湿海绵在炎热群系蒸干 (wetSpongeDriesOnDesertAndBedLands)

湿海绵可以在沙漠或恶地(及其变种)群系蒸干, 就像在下界一样.

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`


## 干草块喂食得到尖啸山羊 (goatHayFeedingAndChildScreaming)

通过给山羊喂食干草捆可以让其生出尖啸山羊

在原版中, 当父母都不是尖啸山羊时, 子代只有0.2%的概率时尖啸山羊

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`


## 海龟蛋在干海带块上加速孵化 (turtleEggsDriedKelpBlockFaster)

海龟蛋放在干海带块上可以加速孵化, 时间大概缩短到10min.

原版中, 当海龟蛋放到沙子中时, 海龟蛋大概需要90min才能孵化.

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`


## 动物生成数量上限 (animalsSpanLimit)

修改动物自然生成的数量上限, 原版为10

- 类型: `int`
- 默认值: `10`
- 参考选项: `10`, `20`, `40`
- 分类: `SURVIVAL`, `JFT`


## 设置动物是否为稀有生成 (animalsRaleSetting)

设置动物的生成是否属于稀有生成

稀有生成会限制20s尝试生成一次, 非稀有为1gt尝试生成一次

- 类型: `boolean`
- 默认值: `true`
- 参考选项: `true`, `false`
- 分类: `SURVIVAL`, `JFT`


## 锄头在非空气方块下也能耕地 (canTillFarmlandBelowBlock)

当泥土类方块上方为冰块, 水, 或者基岩时, 锄头也能耕地

可能在制作西瓜南瓜机有用处

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`


## 潜影贝在缠怨藤中只进行 baseTick 活动 (shulkerBaseTickIfOnTwistingVine)

在 12800HX 原版中堆叠 2444 只潜影贝为 825 mspt, 开启后约为 3-4 mspt

开启后, 在缠怨藤中的潜影贝不会试图传送或索敌, 不会上下矿车.

开启后, 客户端在使用矿车运输潜影贝时, 潜影贝尝试下车到缠怨藤时会出现渲染错误. 退出重进即可正常, 不会影响潜影贝下车的位置.

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`, `FEATURE`


## 阻止打掉物品展示框 (interceptItemFrameDrop)

当物品展示框附着于灰色混凝土或干海带块时, 非创造模式的玩家无法破坏展示框, 除非他们主手拿着白名单物品.

白名单物品有物品展示框, 发光物品展示框, 镐子, 斧子

只影响破坏物品展示框, 从物品展示框中拿下物品不受影响

- 类型: `string`
- 默认值: `false`
- 参考选项: `false`, `true`, `allowEmptyHand`
- 分类: `SURVIVAL`, `JFT`


## 箭类弹射物是否伤害物品展示框 (canArrowDamageItemFrame)

当物品展示框附着于灰色混凝土或干海带块时, 如果物品展示框中有物品展示, 那么弹射物不会将物品展示框中的物品射下.

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `SURVIVAL`, `JFT`