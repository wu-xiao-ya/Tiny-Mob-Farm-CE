package cn.davidma.tinymobfarm.client.gui;

import cn.davidma.tinymobfarm.common.container.ContainerMobFarm;
import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;

public class GuiMobFarm extends ContainerScreen<ContainerMobFarm> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/farm_gui.png");
    private float displayedProgress;

    public GuiMobFarm(ContainerMobFarm screenContainer, PlayerInventory inv, ITextComponent title) {
        super(screenContainer, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(TEXTURE);
        int leftPos = this.leftPos;
        int topPos = this.topPos;
        this.blit(matrixStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        TileEntityMobFarm tile = this.menu.getTileEntityMobFarm();
        if (tile != null && tile.isWorking()) {
            float targetProgress = MathHelper.clamp(this.menu.getSyncProgressPixels(), 0, 80);
            if (this.displayedProgress <= targetProgress) {
                this.displayedProgress = Math.min(targetProgress, this.displayedProgress + 6.0F);
            } else {
                this.displayedProgress = targetProgress;
            }

            int progress = MathHelper.clamp((int) this.displayedProgress, 0, 80);
            this.blit(matrixStack, leftPos + 48, topPos + 60, 176, 5, 80, 5);
            this.blit(matrixStack, leftPos + 48, topPos + 60, 176, 0, progress, 5);
        } else {
            this.displayedProgress = 0.0F;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        this.drawTip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        TileEntityMobFarm tile = this.menu.getTileEntityMobFarm();
        if (tile != null) {
            this.font.draw(matrixStack, this.title.getString(), (float) ((this.imageWidth - this.font.width(this.title.getString())) / 2), 8.0F, 4210752);

            ITextComponent status;
            if (tile.isWorking()) {
                status = null;
            } else if (tile.getLasso().isEmpty()) {
                status = new TranslationTextComponent("tinymobfarm.gui.no_lasso");
            } else if (tile.isPowered()) {
                status = new TranslationTextComponent("tinymobfarm.gui.redstone_disable");
            } else {
                status = new TranslationTextComponent("tinymobfarm.gui.higher_tier");
            }

            if (status != null) {
                this.font.draw(matrixStack, status.getString(), (float) ((this.imageWidth - this.font.width(status.getString())) / 2), 59.0F, 16733525);
            }
        }
        this.font.draw(matrixStack, this.inventory.getDisplayName().getString(), 8.0F, 72.0F, 4210752);
    }

    private void drawTip(MatrixStack matrixStack, int mouseX, int mouseY) {
        int btnX = this.leftPos + 2;
        int btnY = this.topPos + 2;
        int btnWidth = 8;
        int btnHeight = 8;
        if (mouseX > btnX && mouseY > btnY && mouseX < btnX + btnWidth && mouseY < btnY + btnHeight) {
            this.renderTooltip(matrixStack, Arrays.asList(
                    new TranslationTextComponent("tinymobfarm.tip.redstone").getVisualOrderText(),
                    new TranslationTextComponent("tinymobfarm.tip.output_part_1").getVisualOrderText(),
                    new TranslationTextComponent("tinymobfarm.tip.output_part_2").getVisualOrderText()), mouseX, mouseY);
        }
    }
}
