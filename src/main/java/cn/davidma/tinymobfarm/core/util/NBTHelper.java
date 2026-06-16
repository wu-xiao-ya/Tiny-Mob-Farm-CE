package cn.davidma.tinymobfarm.core.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public final class NBTHelper {
    public static final String MOB = "capturedMob";
    public static final String MOB_NAME = "mobName";
    public static final String MOB_DATA = "mobData";
    public static final String MOB_HEALTH = "mobHealth";
    public static final String MOB_MAX_HEALTH = "mobMaxHealth";
    public static final String MOB_HOSTILE = "mobHostile";
    public static final String MOB_LOOTTABLE_LOCATION = "mobLootTableLocation";

    public static final String MOB_FARM_DATA = "mobFarmData";
    public static final String CURR_PROGRESS = "currProgress";
    public static final String INVENTORY = "inventory";

    private NBTHelper() {
    }

    public static CompoundNBT getBaseTag(ItemStack stack) {
        return stack.getOrCreateTag().getCompound(MOB);
    }

    public static void setBaseTag(ItemStack stack, CompoundNBT nbt) {
        stack.getOrCreateTag().put(MOB, nbt);
    }

    public static boolean hasMob(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        return tag != null && tag.contains(MOB);
    }

    public static boolean hasHostileMob(ItemStack stack) {
        return hasMob(stack) && getBaseTag(stack).getBoolean(MOB_HOSTILE);
    }

    public static void clearMob(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            tag.remove(MOB);
        }
    }
}
