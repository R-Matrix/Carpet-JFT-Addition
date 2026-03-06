package com.jft;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.common.collect.Maps;
import com.jft.translations.JFTTranslationConstants;
import com.jft.translations.JFTTranslations;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CarpetJFTAddition implements ModInitializer, CarpetExtension {
	public static final String MOD_ID = "carpetjftaddition";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final String MOD_VERSION = "0.0.5";

//	This is the original code{
//	private static final SettingsManager mySettingManager =
//			new SettingsManager(MOD_VERSION, MOD_ID, "CarpetJFTAddition");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Carpet JFT Addition v" + MOD_VERSION + " Has Loaded!");

		JFTTranslations.loadTranslations();

		CarpetServer.manageExtension(this);

	}

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(CarpetJFTSettings.class);
	}


	@Override
	public Map<String, String> canHasTranslations(String lang) {

		Map<String, String> trimmedTranslation = Maps.newHashMap();
		String prefix = JFTTranslationConstants.CARPET_TRANSLATIONS_KEY_PREFIX;

		// 获取该语言的所有翻译
		Map<String, String> allTranslations = JFTTranslations.getTranslations(lang);

		for (Map.Entry<String, String> entry : allTranslations.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			if (key.startsWith(prefix)) {
				String newKey = key.substring(prefix.length());
				newKey = "carpet." + newKey;

				trimmedTranslation.put(newKey, value);
			}
		}

		return trimmedTranslation;
	}
}