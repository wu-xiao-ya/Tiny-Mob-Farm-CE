package cn.davidma.tinymobfarm.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemLasso extends Item {
    private static final String MOB_TAG = "Mob";

    public ItemLasso(Properties properties) {
        super(properties.stacksTo(1).durability(256));
    }

    public static boolean hasMob(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        return tag != null && tag.contains(MOB_TAG);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return hasMob(stack) || super.isFoil(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if (hasMob(stack)) {
            tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.release_mob"));
        } else {
            tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.capture"));
        }
    }
}
