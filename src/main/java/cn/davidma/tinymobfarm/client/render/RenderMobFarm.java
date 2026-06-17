package cn.davidma.tinymobfarm.client.render;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.ConfigTinyMobFarm;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;

public class RenderMobFarm extends TileEntityRenderer<TileEntityMobFarm> {
    public RenderMobFarm(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(TileEntityMobFarm tileEntity, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!ConfigTinyMobFarm.shouldRenderFarmMobModel()) {
            return;
        }

        Entity model = tileEntity.getRenderModel();
        if (model == null) {
            return;
        }

        AxisAlignedBB box = model.getBoundingBox();
        double length = Math.max(Math.max(box.getXsize(), box.getYsize()), box.getZsize());
        if (length <= 0.0D) {
            return;
        }

        Direction facing = tileEntity.getBlockState().hasProperty(HorizontalBlock.FACING)
                ? tileEntity.getBlockState().getValue(HorizontalBlock.FACING)
                : Direction.NORTH;
        float yaw = -facing.toYRot();
        double modelScale = 0.5D / length;

        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.125D, 0.5D);
        matrixStack.scale((float) modelScale, (float) modelScale, (float) modelScale);
        Minecraft.getInstance().getEntityRenderDispatcher()
                .render(model, 0.0D, 0.0D, 0.0D, yaw, partialTicks, matrixStack, buffer, combinedLight);
        matrixStack.popPose();
    }
}
