package xyz.water.rmatrix.mod.carpetjftaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.common.collect.Maps;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync.JftMapSyncService;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.frogDyeFeedingAndViviparousBreeding.FrogMemoryModuleType;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.frogDyeFeedingAndViviparousBreeding.FrogViviparousActivity;
import xyz.water.rmatrix.mod.carpetjftaddition.translations.JFTTranslationConstants;
import xyz.water.rmatrix.mod.carpetjftaddition.translations.JFTTranslations;

import java.util.Map;

public class CarpetJFTAddition implements ModInitializer, CarpetExtension {
	public static final String MOD_ID = "carpetjftaddition";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	// Read from fabric.mod.json, whose version is expanded by Gradle from gradle.properties (mod_version).
	public static final String MOD_VERSION = FabricLoader.getInstance()
			.getModContainer(MOD_ID)
			.map(container -> container.getMetadata().getVersion().getFriendlyString())
			.orElse("unknown");

//	This is the original code{
//	private static final SettingsManager mySettingManager =
//			new SettingsManager(MOD_VERSION, MOD_ID, "CarpetJFTAddition");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		FrogViviparousActivity.init();
		FrogMemoryModuleType.init();

		LOGGER.info("Carpet JFT Addition v" + MOD_VERSION + " Has Loaded!");

		JFTTranslations.loadTranslations();

		CarpetServer.manageExtension(this);

		//#if MC >= 12104
		JftMapSyncService.jft$init();
		//#endif

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
