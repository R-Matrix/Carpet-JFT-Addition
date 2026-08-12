package xyz.water.rmatrix.mod.carpetjftaddition;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;
import java.util.Set;

public class JFTMixinConfigPlugin extends RestrictiveMixinConfigPlugin {

	private static final String MIXIN_PREFIX = "xyz.water.rmatrix.mod.carpetjftaddition.mixin.";
	// 1.21.2+ 附魔数据驱动后, impaling 上下文由 EnchantmentMixin 的 modifyValue 钩子捕获
	private static final String MIXIN_IMPALING_HELPER = MIXIN_PREFIX + "rules.impalingWaterContact.EnchantmentHelperMixin";

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
		boolean atLeast1212 = isAtLeast(mcVersion(), 1, 21, 2);
		if (mixinClassName.equals(MIXIN_IMPALING_HELPER) && atLeast1212) {
			return false;
		}
		return super.shouldApplyMixin(targetClassName, mixinClassName);
	}

	private static String mcVersion() {
		return FabricLoader.getInstance().getModContainer("minecraft")
				.orElseThrow().getMetadata().getVersion().getFriendlyString();
	}

	private static boolean isAtLeast(String version, int major, int minor, int patch) {
		String[] parts = version.split("\\.");
		if (parts.length < 2) {
			return false;
		}
		int mj = Integer.parseInt(parts[0]);
		int mn = Integer.parseInt(parts[1]);
		int pt = parts.length >= 3 ? Integer.parseInt(parts[2].split("[-+]")[0]) : 0;
		if (mj != major) {
			return mj > major;
		}
		if (mn != minor) {
			return mn > minor;
		}
		return pt >= patch;
	}
}
