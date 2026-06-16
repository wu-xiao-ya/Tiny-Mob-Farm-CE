package cn.davidma.tinymobfarm.core.drop;

import cn.davidma.tinymobfarm.core.util.EntityHelper;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public final class MobFarmDropManager {
    private MobFarmDropManager() {
    }

    public static List<ItemStack> generateDrops(ItemStack lasso, ServerWorld world) {
        if (lasso.isEmpty() || !NBTHelper.hasMob(lasso)) {
            return new ArrayList<>();
        }

        CompoundNBT mobTag = NBTHelper.getBaseTag(lasso);
        String lootTableLocation = mobTag.getString(NBTHelper.MOB_LOOTTABLE_LOCATION);
        if (lootTableLocation.isEmpty()) {
            return new ArrayList<>();
        }

        return copyDrops(EntityHelper.generateLoot(new ResourceLocation(lootTableLocation),
                mobTag.getCompound(NBTHelper.MOB_DATA), world));
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
