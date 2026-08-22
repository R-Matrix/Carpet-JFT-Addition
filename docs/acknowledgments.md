# 致谢与来源说明

Carpet-JFT-Addition 的部分实现和工程配置参考了以下开源项目。

## Fallen-Breath / Yamlang

语言翻译资源采用 YAML 文件组织，并使用 Yamlang 进行构建处理：

- `src/main/resources/assets/carpetjftaddition/lang/en_us.yml`
- `src/main/resources/assets/carpetjftaddition/lang/zh_cn.yml`

相关实现参考了 Fallen-Breath 维护的 [Yamlang](https://github.com/Fallen-Breath/yamlang) 以及使用该方案的 [Carpet TIS Addition](https://github.com/TISUnion/Carpet-TIS-Addition)。

## Carpet TIS Addition 翻译辅助实现

以下文件基于 Carpet TIS Addition 的翻译辅助实现改写和移植：

| Carpet-JFT-Addition | 参考实现 |
| --- | --- |
| `src/main/java/xyz/water/rmatrix/mod/carpetjftaddition/translations/JFTTranslations.java` | `TISAdditionTranslations.java` |
| `src/main/java/xyz/water/rmatrix/mod/carpetjftaddition/translations/JFTTranslationConstants.java` | `TranslationConstants.java` |

上述改写和移植代码遵循 Carpet TIS Addition 的 LGPL-3.0 许可证，并保留相关版权声明要求。许可证文本见 [LICENSE-LGPL-3.0.txt](../LICENSES/LICENSE-LGPL-3.0.txt)。

## Carpet-Igny-Addition

GitHub Actions CI、版本矩阵生成和多版本发布流程参考并改写了 [Carpet-Igny-Addition](https://github.com/liuyuexiaoyu1/Carpet-Igny-Addition) 的相关工作流和脚本。
