package cn.davidma.tinymobfarm.core.drop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.davidma.tinymobfarm.core.util.EntityHelper;
import cn.davidma.tinymobfarm.core.util.NBTHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MobFarmDropManager {

	private static final List<MobFarmDropRule> CUSTOM_RULES = new ArrayList<MobFarmDropRule>();

	public static void addRule(MobFarmDropRule rule) {
		if (rule != null) {
			CUSTOM_RULES.add(rule);
			DropSourceRegistry.incrementVersion();
		}
	}

	public static void removeRules(String mobRegistryName) {
		boolean removed = false;
		for (int i = CUSTOM_RULES.size() - 1; i >= 0; i--) {
			if (CUSTOM_RULES.get(i).matchesMob(mobRegistryName)) {
				CUSTOM_RULES.remove(i);
				removed = true;
			}
		}
		if (removed) {
			DropSourceRegistry.incrementVersion();
		}
	}

	public static void clearRules() {
		if (!CUSTOM_RULES.isEmpty()) {
			CUSTOM_RULES.clear();
			DropSourceRegistry.incrementVersion();
		}
	}

	public static List<ItemStack> generateDrops(ItemStack lasso, World world) {
		return getDropSource(lasso).generateDrops(world);
	}

	public static DropSource getDropSource(ItemStack lasso) {
		if (lasso.isEmpty() || !NBTHelper.hasMob(lasso)) return EmptyDropSource.INSTANCE;

		NBTTagCompound mobTag = NBTHelper.getBaseTag(lasso);
		String mobRegistryName = getMobRegistryName(mobTag);
		List<MobFarmDropRule> customRules = getCustomRules(mobRegistryName);
		if (!customRules.isEmpty()) {
			return new CustomDropSource(customRules);
		}

		String lootTableLocation = mobTag.getString(NBTHelper.MOB_LOOTTABLE_LOCATION);
		if (lootTableLocation.isEmpty()) return EmptyDropSource.INSTANCE;
		return new LootTableDropSource(new ResourceLocation(lootTableLocation));
	}

	public static int getDropSourceVersion() {
		return DropSourceRegistry.getVersion();
	}

	private static List<MobFarmDropRule> getCustomRules(String mobRegistryName) {
		List<MobFarmDropRule> rules = new ArrayList<MobFarmDropRule>();
		if (mobRegistryName.isEmpty()) return rules;
		for (int i = CUSTOM_RULES.size() - 1; i >= 0; i--) {
			MobFarmDropRule rule = CUSTOM_RULES.get(i);
			if (rule.matchesMob(mobRegistryName)) {
				rules.add(0, rule);
			}
		}
		return rules;
	}

	private static String getMobRegistryName(NBTTagCompound mobTag) {
		NBTTagCompound mobData = mobTag.getCompoundTag(NBTHelper.MOB_DATA);
		return mobData.getString("id");
	}

	private static List<ItemStack> copyDrops(List<ItemStack> drops) {
		List<ItemStack> copy = new ArrayList<ItemStack>();
		for (ItemStack stack: drops) {
			if (!stack.isEmpty()) {
				copy.add(stack.copy());
			}
		}
		return copy;
	}

	public interface DropSource {
		List<ItemStack> generateDrops(World world);
	}

	private static class EmptyDropSource implements DropSource {
		private static final EmptyDropSource INSTANCE = new EmptyDropSource();

		@Override
		public List<ItemStack> generateDrops(World world) {
			return new ArrayList<ItemStack>();
		}
	}

	private static class CustomDropSource implements DropSource {
		private final List<MobFarmDropRule> rules;

		private CustomDropSource(List<MobFarmDropRule> rules) {
			this.rules = new ArrayList<MobFarmDropRule>(rules);
		}

		@Override
		public List<ItemStack> generateDrops(World world) {
			List<ItemStack> drops = new ArrayList<ItemStack>();
			Random random = world.rand;
			for (MobFarmDropRule rule: this.rules) {
				drops.addAll(rule.generateDrops(random));
			}
			return drops;
		}
	}

	private static class LootTableDropSource implements DropSource {
		private final ResourceLocation lootTableLocation;

		private LootTableDropSource(ResourceLocation lootTableLocation) {
			this.lootTableLocation = lootTableLocation;
		}

		@Override
		public List<ItemStack> generateDrops(World world) {
			return copyDrops(EntityHelper.generateLoot(this.lootTableLocation, world));
		}
	}

	private static class DropSourceRegistry {
		private static int version;

		private static int getVersion() {
			return version;
		}

		private static void incrementVersion() {
			version++;
		}
	}
}
