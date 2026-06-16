package cn.davidma.tinymobfarm.core.drop;

import net.minecraft.item.ItemStack;

import java.util.Random;

public class WeightedDrop {
    private final ItemStack stack;
    private final int minAmount;
    private final int maxAmount;
    private final int minChance;
    private final int maxChance;

    public WeightedDrop(ItemStack stack, int chance) {
        this(stack, stack.getCount(), stack.getCount(), chance);
    }

    public WeightedDrop(ItemStack stack, int minAmount, int maxAmount, int chance) {
        this(stack, minAmount, maxAmount, chance, chance);
    }

    public WeightedDrop(ItemStack stack, int minAmount, int maxAmount, int minChance, int maxChance) {
        this.stack = stack.copy();
        this.stack.setCount(1);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.minChance = minChance;
        this.maxChance = maxChance;
    }

    public boolean shouldDrop(Random random) {
        int chance = this.minChance;
        if (this.maxChance > this.minChance) {
            chance += random.nextInt(this.maxChance - this.minChance + 1);
        }
        return chance >= 100 || random.nextInt(100) < chance;
    }

    public ItemStack createStack(Random random) {
        ItemStack result = this.stack.copy();
        int amount = this.minAmount;
        if (this.maxAmount > this.minAmount) {
            amount += random.nextInt(this.maxAmount - this.minAmount + 1);
        }
        result.setCount(amount);
        return result;
    }
}
