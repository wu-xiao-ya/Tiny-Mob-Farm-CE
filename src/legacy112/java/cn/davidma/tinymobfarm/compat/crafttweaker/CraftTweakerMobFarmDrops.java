package cn.davidma.tinymobfarm.compat.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import cn.davidma.tinymobfarm.core.drop.MobFarmDropManager;
import cn.davidma.tinymobfarm.core.drop.MobFarmDropRule;
import cn.davidma.tinymobfarm.core.drop.WeightedDrop;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.tinymobfarm.MobDrops")
public class CraftTweakerMobFarmDrops {

	@ZenMethod
	public static void add(String mobRegistryName, IItemStack[] drops) {
		CraftTweakerAPI.apply(new AddDropsAction(mobRegistryName, drops));
	}

	@ZenMethod
	public static void add(String mobRegistryName, IItemStack drop) {
		CraftTweakerAPI.apply(new AddOneDropAction(mobRegistryName, drop, 100));
	}

	@ZenMethod
	public static void add(String mobRegistryName, IItemStack drop, int chance) {
		CraftTweakerAPI.apply(new AddOneDropAction(mobRegistryName, drop, chance));
	}

	@ZenMethod
	public static void add(String mobRegistryName, IItemStack drop, int minChance, int maxChance) {
		CraftTweakerAPI.apply(new AddOneDropAction(mobRegistryName, drop, minChance, maxChance));
	}

	@ZenMethod
	public static void add(String mobRegistryName, IItemStack drop, int minAmount, int maxAmount, int chance) {
		CraftTweakerAPI.apply(new AddOneDropAction(mobRegistryName, drop, minAmount, maxAmount, chance));
	}

	@ZenMethod
	public static void addChance(String mobRegistryName, IItemStack[] drops, int minAmount, int maxAmount, int chance) {
		CraftTweakerAPI.apply(new AddDropsAction(mobRegistryName, drops, minAmount, maxAmount, chance));
	}

	@ZenMethod
	public static void remove(String mobRegistryName) {
		CraftTweakerAPI.apply(new RemoveDropsAction(mobRegistryName));
	}

	@ZenMethod
	public static void clear() {
		CraftTweakerAPI.apply(new ClearDropsAction());
	}

	private static List<WeightedDrop> convertDrops(IItemStack[] drops, int chance) {
		List<WeightedDrop> result = new ArrayList<WeightedDrop>();
		if (drops == null) return result;

		for (IItemStack drop: drops) {
			if (drop == null || drop.isEmpty()) continue;
			ItemStack stack = CraftTweakerMC.getItemStack(drop);
			if (!stack.isEmpty()) {
				result.add(new WeightedDrop(stack, chance));
			}
		}

		return result;
	}

	private static List<WeightedDrop> convertDrops(IItemStack[] drops, int minAmount, int maxAmount, int chance) {
		List<WeightedDrop> result = new ArrayList<WeightedDrop>();
		if (drops == null) return result;

		for (IItemStack drop: drops) {
			if (drop == null || drop.isEmpty()) continue;
			ItemStack stack = CraftTweakerMC.getItemStack(drop);
			if (!stack.isEmpty()) {
				result.add(new WeightedDrop(stack, minAmount, maxAmount, chance));
			}
		}

		return result;
	}

	private static class AddDropsAction implements IAction {
		private final String mobRegistryName;
		private final IItemStack[] drops;
		private final int minAmount;
		private final int maxAmount;
		private final int minChance;
		private final int maxChance;
		private final boolean useStackAmount;

		private AddDropsAction(String mobRegistryName, IItemStack[] drops) {
			this.mobRegistryName = mobRegistryName;
			this.drops = drops;
			this.minAmount = 1;
			this.maxAmount = 1;
			this.minChance = 100;
			this.maxChance = 100;
			this.useStackAmount = true;
		}

		private AddDropsAction(String mobRegistryName, IItemStack[] drops, int minAmount, int maxAmount, int chance) {
			this.mobRegistryName = mobRegistryName;
			this.drops = drops;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
			this.minChance = chance;
			this.maxChance = chance;
			this.useStackAmount = false;
		}

		@Override
		public void apply() {
			MobFarmDropManager.addRule(new MobFarmDropRule(this.mobRegistryName, this.convertDrops()));
		}

		@Override
		public String describe() {
			return "Adding Tiny Mob Farm drops for " + this.mobRegistryName;
		}

		@Override
		public boolean validate() {
			return this.mobRegistryName != null && !this.mobRegistryName.isEmpty() && this.minAmount > 0 && this.maxAmount >= this.minAmount && this.minChance > 0 && this.maxChance >= this.minChance && this.maxChance <= 100 && !this.convertDrops().isEmpty();
		}

		@Override
		public String describeInvalid() {
			return "Invalid Tiny Mob Farm drops for " + this.mobRegistryName;
		}

		private List<WeightedDrop> convertDrops() {
			return this.useStackAmount ? CraftTweakerMobFarmDrops.convertDrops(this.drops, this.minChance) : CraftTweakerMobFarmDrops.convertDrops(this.drops, this.minAmount, this.maxAmount, this.minChance);
		}
	}

	private static class AddOneDropAction implements IAction {
		private final String mobRegistryName;
		private final IItemStack drop;
		private final int minAmount;
		private final int maxAmount;
		private final int minChance;
		private final int maxChance;
		private final boolean useStackAmount;

		private AddOneDropAction(String mobRegistryName, IItemStack drop, int chance) {
			this(mobRegistryName, drop, chance, chance);
		}

		private AddOneDropAction(String mobRegistryName, IItemStack drop, int minChance, int maxChance) {
			this.mobRegistryName = mobRegistryName;
			this.drop = drop;
			this.minAmount = 1;
			this.maxAmount = 1;
			this.minChance = minChance;
			this.maxChance = maxChance;
			this.useStackAmount = true;
		}

		private AddOneDropAction(String mobRegistryName, IItemStack drop, int minAmount, int maxAmount, int chance) {
			this.mobRegistryName = mobRegistryName;
			this.drop = drop;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
			this.minChance = chance;
			this.maxChance = chance;
			this.useStackAmount = false;
		}

		@Override
		public void apply() {
			MobFarmDropManager.addRule(new MobFarmDropRule(this.mobRegistryName, this.convertDrops()));
		}

		@Override
		public String describe() {
			return "Adding Tiny Mob Farm drop for " + this.mobRegistryName;
		}

		@Override
		public boolean validate() {
			return this.mobRegistryName != null && !this.mobRegistryName.isEmpty() && this.drop != null && !this.drop.isEmpty() && this.minAmount > 0 && this.maxAmount >= this.minAmount && this.minChance > 0 && this.maxChance >= this.minChance && this.maxChance <= 100;
		}

		@Override
		public String describeInvalid() {
			return "Invalid Tiny Mob Farm drop for " + this.mobRegistryName;
		}

		private List<WeightedDrop> convertDrops() {
			List<IItemStack> drops = new ArrayList<IItemStack>();
			if (this.drop != null) {
				drops.add(this.drop);
			}
			IItemStack[] dropArray = drops.toArray(new IItemStack[0]);
			return this.useStackAmount ? CraftTweakerMobFarmDrops.convertDrops(dropArray, this.minChance) : CraftTweakerMobFarmDrops.convertDrops(dropArray, this.minAmount, this.maxAmount, this.minChance);
		}
	}

	private static class RemoveDropsAction implements IAction {
		private final String mobRegistryName;

		private RemoveDropsAction(String mobRegistryName) {
			this.mobRegistryName = mobRegistryName;
		}

		@Override
		public void apply() {
			MobFarmDropManager.removeRules(this.mobRegistryName);
		}

		@Override
		public String describe() {
			return "Removing Tiny Mob Farm drops for " + this.mobRegistryName;
		}
	}

	private static class ClearDropsAction implements IAction {
		@Override
		public void apply() {
			MobFarmDropManager.clearRules();
		}

		@Override
		public String describe() {
			return "Clearing Tiny Mob Farm drops";
		}
	}
}
