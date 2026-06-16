package cn.davidma.tinymobfarm.common;

import cn.davidma.tinymobfarm.common.registry.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class TinyMobFarmItemGroup extends ItemGroup {
    public TinyMobFarmItemGroup() {
        super("tinymobfarm");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.LASSO.get());
    }
}
