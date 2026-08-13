package xyz.water.rmatrix.mod.carpetjftaddition.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;

/**
 * impalingWaterContact 规则的自动化测试:
 * 通过直接调用 EnchantmentHelper.getDamage 断言伤害数值, 覆盖水/干燥/雨/规则开关/非穿刺武器/伤害来源/回归
 */
public class ImpalingWaterContactGameTest implements FabricGameTest {

	private static final float BASE_DAMAGE = 5.0F;
	private static final float IMPALING_BONUS_1 = 2.5F; // 穿刺 I: 2.5
	private static final float SHARPNESS_BONUS_1 = 1.0F; // 锋利 I: 1.0

	@GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = "impaling_water")
	public void impalingInWaterGetsBonus(TestContext context) {
		CarpetJFTSettings.impalingWaterContact = true;
		ServerWorld world = context.getWorld();
		Entity target = spawnInWater(context, world, 1, 1);
		ItemStack trident = impalingTrident(world, 1);

		context.runAtTick(5, () -> {
			if (!target.isTouchingWater()) {
				BlockPos feet = target.getBlockPos();
				throw new RuntimeException("前置条件失败: 目标未判定为触水"
						+ " age=" + target.age
						+ " pos=" + target.getPos()
						+ " feet=" + feet
						+ " fluid=" + world.getFluidState(feet)
						+ " up=" + world.getFluidState(feet.up())
						+ " down=" + world.getFluidState(feet.down()));
			}
			float actual = EnchantmentHelper.getDamage(
					world, trident, target, world.getDamageSources().generic(), BASE_DAMAGE);
			CarpetJFTSettings.impalingWaterContact = false;
			assertDamage("目标在水中, 穿刺 I 应加伤", BASE_DAMAGE + IMPALING_BONUS_1, actual);
			context.complete();
		});
	}

	@GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = "impaling_dry")
	public void impalingDryTargetNoBonus(TestContext context) {
		CarpetJFTSettings.impalingWaterContact = true;
		ServerWorld world = context.getWorld();
		context.setBlockState(new BlockPos(1, 0, 1), Blocks.STONE);
		Entity target = context.spawnMob(EntityType.ZOMBIE, new BlockPos(1, 1, 1));

		float actual = EnchantmentHelper.getDamage(
				world, impalingTrident(world, 1), target, world.getDamageSources().generic(), BASE_DAMAGE);

		CarpetJFTSettings.impalingWaterContact = false;
		assertDamage("干燥目标不应加伤", BASE_DAMAGE, actual);
		context.complete();
	}

	@GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = "impaling_rain", tickLimit = 200)
	public void impalingInRainGetsBonus(TestContext context) {
		CarpetJFTSettings.impalingWaterContact = true;
		ServerWorld world = context.getWorld();
		context.useNightTime();
		world.setWeather(0, 200, true, false);
		context.setBlockState(new BlockPos(1, 0, 1), Blocks.STONE);
		BlockPos targetPos = context.getAbsolutePos(new BlockPos(1, 1, 1));
		clearSkyColumn(world, targetPos);
		Entity target = context.spawnMob(EntityType.ZOMBIE, new BlockPos(1, 1, 1));
		ItemStack trident = impalingTrident(world, 1);

		context.runAtTick(60, () -> {
			if (!target.isTouchingWaterOrRain()) {
				throw new RuntimeException("前置条件失败: 目标未淋到雨"
						+ " isRaining=" + world.isRaining()
						+ " rainGradient=" + world.getRainGradient(1.0F)
						+ " hasRain=" + world.hasRain(target.getBlockPos())
						+ " pos=" + target.getBlockPos());
			}
			float actual = EnchantmentHelper.getDamage(
					world, trident, target, world.getDamageSources().generic(), BASE_DAMAGE);
			CarpetJFTSettings.impalingWaterContact = false;
			assertDamage("雨天目标应加伤(OrRain)", BASE_DAMAGE + IMPALING_BONUS_1, actual);
			context.complete();
		});
	}

	@GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = "impaling_rule_disabled")
	public void impalingRuleDisabledNoBonus(TestContext context) {
		CarpetJFTSettings.impalingWaterContact = false;
		ServerWorld world = context.getWorld();
		context.setBlockState(new BlockPos(1, 0, 1), Blocks.STONE);
		context.setBlockState(new BlockPos(1, 1, 1), Blocks.WATER);
		Entity target = context.spawnMob(EntityType.ZOMBIE, new BlockPos(1, 1, 1));

		float actual = EnchantmentHelper.getDamage(
				world, impalingTrident(world, 1), target, world.getDamageSources().generic(), BASE_DAMAGE);

		assertDamage("规则关闭不应加伤", BASE_DAMAGE, actual);
		context.complete();
	}

	@GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = "impaling_non_impaling_weapon")
	public void nonImpalingWeaponUnaffected(TestContext context) {
		CarpetJFTSettings.impalingWaterContact = true;
		ServerWorld world = context.getWorld();
		context.setBlockState(new BlockPos(1, 0, 1), Blocks.STONE);
		context.setBlockState(new BlockPos(1, 1, 1), Blocks.WATER);
		Entity target = context.spawnMob(EntityType.ZOMBIE, new BlockPos(1, 1, 1));

		ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
		sword.addEnchantment(enchantmentEntry(world, Enchantments.SHARPNESS), 1);
		float actual = EnchantmentHelper.getDamage(
				world, sword, target, world.getDamageSources().generic(), BASE_DAMAGE);

		CarpetJFTSettings.impalingWaterContact = false;
		assertDamage("非穿刺武器只应有锋利加成", BASE_DAMAGE + SHARPNESS_BONUS_1, actual);
		context.complete();
	}

	@GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = "impaling_sources")
	public void meleeAndThrownSourcesBothGetBonus(TestContext context) {
		CarpetJFTSettings.impalingWaterContact = true;
		ServerWorld world = context.getWorld();
		Entity target = spawnInWater(context, world, 1, 1);
		ItemStack trident = impalingTrident(world, 1);

		MobEntity attacker = context.spawnMob(EntityType.ZOMBIE, new BlockPos(2, 1, 2));
		DamageSource melee = world.getDamageSources().mobAttack(attacker);
		DamageSource thrown = world.getDamageSources().trident(new TridentEntity(world, attacker, trident), attacker);

		context.runAtTick(5, () -> {
			if (!target.isTouchingWater()) {
				BlockPos feet = target.getBlockPos();
				throw new RuntimeException("前置条件失败: 目标未判定为触水"
						+ " age=" + target.age
						+ " pos=" + target.getPos()
						+ " feet=" + feet
						+ " fluid=" + world.getFluidState(feet)
						+ " up=" + world.getFluidState(feet.up())
						+ " down=" + world.getFluidState(feet.down()));
			}
			float meleeDamage = EnchantmentHelper.getDamage(world, trident, target, melee, BASE_DAMAGE);
			float thrownDamage = EnchantmentHelper.getDamage(world, trident, target, thrown, BASE_DAMAGE);
			CarpetJFTSettings.impalingWaterContact = false;
			assertDamage("近战伤害来源也应加伤", BASE_DAMAGE + IMPALING_BONUS_1, meleeDamage);
			assertDamage("投掷伤害来源也应加伤", BASE_DAMAGE + IMPALING_BONUS_1, thrownDamage);
			context.complete();
		});
	}

	@GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = "impaling_tick_regression")
	public void enchantedEquipmentTickDoesNotCrash(TestContext context) {
		// 回归: 原实现会在实体 tick 的附魔效果扫描(onTick -> applyEffects)时因 ThreadLocal 未初始化而 NPE
		CarpetJFTSettings.impalingWaterContact = true;
		ServerWorld world = context.getWorld();
		ZombieEntity zombie = context.spawnMob(EntityType.ZOMBIE, new BlockPos(1, 1, 1));

		ItemStack boots = new ItemStack(Items.IRON_BOOTS);
		boots.addEnchantment(enchantmentEntry(world, Enchantments.SOUL_SPEED), 1);
		zombie.equipStack(EquipmentSlot.FEET, boots);

		context.runAtTick(20, () -> {
			CarpetJFTSettings.impalingWaterContact = false;
			context.complete();
		});
	}

	private static Entity spawnInWater(TestContext context, ServerWorld world, int x, int z) {
		context.setBlockState(new BlockPos(x, 0, z), Blocks.STONE);
		context.setBlockState(new BlockPos(x, 1, z), Blocks.WATER);
		context.setBlockState(new BlockPos(x, 2, z), Blocks.WATER);
		return context.spawnMob(EntityType.ZOMBIE, new BlockPos(x, 1, z));
	}

	private static void clearSkyColumn(ServerWorld world, BlockPos targetPos) {
		int topY = world.getTopY(net.minecraft.world.Heightmap.Type.WORLD_SURFACE, targetPos.getX(), targetPos.getZ());
		for (int y = targetPos.getY() + 1; y < topY; y++) {
			world.setBlockState(new BlockPos(targetPos.getX(), y, targetPos.getZ()), Blocks.AIR.getDefaultState());
		}
	}

	private static ItemStack impalingTrident(ServerWorld world, int level) {
		ItemStack stack = new ItemStack(Items.TRIDENT);
		stack.addEnchantment(enchantmentEntry(world, Enchantments.IMPALING), level);
		return stack;
	}

	private static RegistryEntry<Enchantment> enchantmentEntry(ServerWorld world, RegistryKey<Enchantment> key) {
		//#if MC >= 12102
		return world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(key);
		//#else
		//$$ return world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(key).orElseThrow();
		//#endif
	}

	private static void assertDamage(String message, float expected, float actual) {
		if (Math.abs(expected - actual) > 0.001F) {
			throw new RuntimeException(message + ": 期望 " + expected + " 实际 " + actual);
		}
	}
}
