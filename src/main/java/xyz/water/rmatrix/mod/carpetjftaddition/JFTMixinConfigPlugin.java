package xyz.water.rmatrix.mod.carpetjftaddition;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;
import java.util.Set;

public class JFTMixinConfigPlugin extends RestrictiveMixinConfigPlugin {

	private static final String JFT_MAP_SYNC_MIXIN =
			"xyz.water.rmatrix.mod.carpetjftaddition.mixin.sync.jftMapSync.ServerWorldMapStateMixin";

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.equals(JFT_MAP_SYNC_MIXIN) && !jft$isAtLeastMinecraft(1, 21, 4)) {
			return false;
		}
		return super.shouldApplyMixin(targetClassName, mixinClassName);
	}

	private static boolean jft$isAtLeastMinecraft(int major, int minor, int patch) {
		String version = FabricLoader.getInstance().getModContainer("minecraft")
				.orElseThrow()
				.getMetadata()
				.getVersion()
				.getFriendlyString();
		String[] parts = version.split("\\.");
		if (parts.length < 2) {
			return false;
		}

		int actualMajor = Integer.parseInt(parts[0]);
		int actualMinor = Integer.parseInt(parts[1]);
		int actualPatch = parts.length >= 3
				? Integer.parseInt(parts[2].split("[-+]")[0])
				: 0;
		if (actualMajor != major) {
			return actualMajor > major;
		}
		if (actualMinor != minor) {
			return actualMinor > minor;
		}
		return actualPatch >= patch;
	}
}
