package cn.davidma.tinymobfarm.core.drop;

import cn.davidma.tinymobfarm.core.util.EntityHelper;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class MobFarmDropManager {
    private static final List<MobFarmDropRule> CUSTOM_RULES = new ArrayList<>();
    private static int dropSourceVersion;

    private MobFarmDropManager() {
    }

    public static void addRule(MobFarmDropRule rule) {
        if (rule != null) {
            CUSTOM_RULES.add(rule);
            dropSourceVersion++;
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
            dropSourceVersion++;
        }
    }

    public static void clearRules() {
        if (!CUSTOM_RULES.isEmpty()) {
            CUSTOM_RULES.clear();
            dropSourceVersion++;
        }
    }

    public static int getDropSourceVersion() {
        return dropSourceVersion;
    }

    public static List<ItemStack> generateDrops(ItemStack lasso, ServerWorld world) {
        if (lasso.isEmpty() || !NBTHelper.hasMob(lasso)) {
            return new ArrayList<>();
        }

        CompoundNBT mobTag = NBTHelper.getBaseTag(lasso);
        String mobRegistryName = getMobRegistryName(mobTag);
        List<MobFarmDropRule> customRules = getCustomRules(mobRegistryName);
        if (!customRules.isEmpty()) {
            return generateCustomDrops(customRules, world.random);
        }

        String lootTableLocation = mobTag.getString(NBTHelper.MOB_LOOTTABLE_LOCATION);
        if (lootTableLocation.isEmpty()) {
            return new ArrayList<>();
        }

        return copyDrops(EntityHelper.generateLoot(new ResourceLocation(lootTableLocation),
                mobTag.getCompound(NBTHelper.MOB_DATA), world));
    }

    private static List<ItemStack> generateCustomDrops(List<MobFarmDropRule> rules, Random random) {
        List<ItemStack> drops = new ArrayList<>();
        for (MobFarmDropRule rule : rules) {
            drops.addAll(rule.generateDrops(random));
        }
        return drops;
    }

    private static List<MobFarmDropRule> getCustomRules(String mobRegistryName) {
        List<MobFarmDropRule> rules = new ArrayList<>();
        if (mobRegistryName.isEmpty()) {
            return rules;
        }
        for (int i = CUSTOM_RULES.size() - 1; i >= 0; i--) {
            MobFarmDropRule rule = CUSTOM_RULES.get(i);
            if (rule.matchesMob(mobRegistryName)) {
                rules.add(0, rule);
            }
        }
        return rules;
    }

    private static String getMobRegistryName(CompoundNBT mobTag) {
        CompoundNBT mobData = mobTag.getCompound(NBTHelper.MOB_DATA);
        return mobData.getString("id");
    }

    private static List<ItemStack> copyDrops(List<ItemStack> drops) {
        List<ItemStack> copy = new ArrayList<>();
        for (ItemStack stack : drops) {
            if (!stack.isEmpty()) {
                copy.add(stack.copy());
            }
        }
        return copy;
    }
}
