package cn.davidma.tinymobfarm.core.drop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;

public class MobFarmDropRule {

	private final String mobRegistryName;
	private final List<WeightedDrop> drops;

	public MobFarmDropRule(String mobRegistryName, List<WeightedDrop> drops) {
		this.mobRegistryName = mobRegistryName;
		this.drops = new ArrayList<WeightedDrop>(drops);
	}

	public boolean matchesMob(String mobRegistryName) {
		return this.mobRegistryName.equals(mobRegistryName);
	}

	public List<ItemStack> generateDrops(Random random) {
		List<ItemStack> result = new ArrayList<ItemStack>();

		for (WeightedDrop drop: this.drops) {
			if (drop.shouldDrop(random)) {
				result.add(drop.createStack(random));
			}
		}

		return result;
	}
}
