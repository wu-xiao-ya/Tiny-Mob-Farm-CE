package cn.davidma.tinymobfarm.common.item;

import java.util.List;
import java.util.function.Consumer;

import cn.davidma.tinymobfarm.common.block.BlockMobFarm;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemBlockMobFarm extends ItemBlock {

	private Consumer<List<String>> tooltipBuilder;
	
	public ItemBlockMobFarm(BlockMobFarm block) {
		super(block);
		this.setRegistryName(block.getRegistryName().toString());
		this.setTranslationKey(block.getRegistryName().toString());
		this.tooltipBuilder = block.getTooltipBuilder();
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (GuiScreen.isShiftKeyDown()) this.tooltipBuilder.accept(tooltip);
		else tooltip.add(I18n.format("tinymobfarm.tooltip.hold_shift", TextFormatting.ITALIC, TextFormatting.GRAY));
		
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
