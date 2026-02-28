package com.jft.translations;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class JFTTranslations {
    /**
     * language -> (key -> content)
     */
    private static final Map<String, Map<String, String>> translationStorage = Maps.newLinkedHashMap();

    public static void loadTranslations() {
        // 先清空缓存
        translationStorage.clear();

        // 支持的语言列表
        List<String> languages = Arrays.asList("en_us", "zh_cn");

        for (String lang : languages) {
            Map<String, String> translations = loadTranslationFile(lang);
            translationStorage.put(lang, translations);
        }

    }

    private static Map<String, String> loadTranslationFile(String lang) {
        Map<String, String> translations = Maps.newLinkedHashMap();
        String jsonPath = String.format("assets/%s/lang/%s.json",
                JFTTranslationConstants.TRANSLATION_NAMESPACE, lang);


        try (InputStream input = JFTTranslations.class.getClassLoader().getResourceAsStream(jsonPath)) {
            if (input != null) {
                String jsonContent = IOUtils.toString(input, StandardCharsets.UTF_8);

                // 简单验证 JSON 格式
                if (jsonContent.trim().startsWith("{")) {
                    JsonObject jsonObject = new Gson().fromJson(jsonContent, JsonObject.class);

                    jsonObject.entrySet().forEach(entry -> {
                        String key = entry.getKey();
                        String value = entry.getValue().getAsString();
                        translations.put(key, value);
                    });

                } else {
                    System.out.println("[JFT] 警告: " + lang + " JSON 格式无效");
                }
            } else {
                System.out.println("[JFT] 警告: 找不到翻译文件 " + jsonPath);

                // 列出类路径下所有 assets 文件用于调试
                debugClasspathResources();
            }
        } catch (Exception e) {
            System.out.println("[JFT] 加载翻译失败 " + lang + ": " + e.getMessage());
            e.printStackTrace();
        }

        return translations;
    }

    private static void debugClasspathResources() {
        try {
            // 列出所有 assets 目录
            String[] paths = {
                    "assets/carpetjftaddition/lang/en_us.json",
                    "assets/carpetjftaddition/lang/zh_cn.json",
                    "assets/carpet-jft-addition/lang/en_us.json",
                    "assets/carpet-jft-addition/lang/zh_cn.json"
            };

            for (String path : paths) {
                InputStream test = JFTTranslations.class.getClassLoader().getResourceAsStream(path);
                if (test != null) {
                    test.close();
                } else {
                    System.out.println("  未找到: " + path);
                }
            }
        } catch (Exception e) {
            System.out.println("调试失败: " + e.getMessage());
        }
    }

    /**
     * @param lang the language
     * @return key -> translated content
     */
    public static Map<String, String> getTranslations(String lang) {
        // 如果缓存中没有，尝试加载
        if (!translationStorage.containsKey(lang)) {
            Map<String, String> translations = loadTranslationFile(lang);
            translationStorage.put(lang, translations);
        }

        return translationStorage.getOrDefault(lang, Collections.emptyMap());
    }

    /**
     * 获取所有支持的语言
     */
    public static Set<String> getLanguages() {
        return translationStorage.keySet();
    }

    /**
     * 清除翻译缓存
     */
    public static void clearCache() {
        translationStorage.clear();
    }
}