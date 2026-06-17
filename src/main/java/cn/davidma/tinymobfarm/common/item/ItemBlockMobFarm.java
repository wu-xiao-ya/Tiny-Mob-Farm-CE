package cn.davidma.tinymobfarm.common.item;

import cn.davidma.tinymobfarm.common.block.BlockMobFarm;
import cn.davidma.tinymobfarm.core.ConfigTinyMobFarm;
import cn.davidma.tinymobfarm.core.MobFarmTier;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBlockMobFarm extends BlockItem {
    private final MobFarmTier tier;
    private final boolean mechanical;

    public ItemBlockMobFarm(BlockMobFarm block, Properties properties) {
        super(block, properties);
        this.tier = block.getTier();
        this.mechanical = block.isMechanical();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.hold_shift",
                    TextFormatting.ITALIC, TextFormatting.GRAY));
            return;
        }

        if (this.mechanical) {
            tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.mechanical"));
        }
        if (!this.tier.canFarmHostile()) {
            tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.no_hostile").withStyle(TextFormatting.RED));
        }
        tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.farm_rate",
                ConfigTinyMobFarm.getFarmRateTicks(this.tier) / 20.0D));
        tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.durability_info"));
        this.addDurabilityTooltips(tooltip);
    }

    private void addDurabilityTooltips(List<ITextComponent> tooltip) {
        Map<Integer, Integer> normalizedChance = new HashMap<>();
        int[] damageChance = this.tier.getDamageChance();
        for (int damage : damageChance) {
            normalizedChance.put(damage, normalizedChance.getOrDefault(damage, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : normalizedChance.entrySet()) {
            int damage = entry.getKey();
            int chance = (int) (entry.getValue() * 100.0D / damageChance.length);
            if (damage == 0) {
                tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.no_durability", chance));
            } else {
                tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.default_durability", chance, damage));
            }
        }
    }
}
