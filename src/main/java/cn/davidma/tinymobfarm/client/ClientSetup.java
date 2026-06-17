package cn.davidma.tinymobfarm.client;

import cn.davidma.tinymobfarm.client.gui.GuiMobFarm;
import cn.davidma.tinymobfarm.client.render.RenderMobFarm;
import cn.davidma.tinymobfarm.common.registry.ModContainers;
import cn.davidma.tinymobfarm.common.registry.ModBlocks;
import cn.davidma.tinymobfarm.common.registry.ModTileEntities;
import cn.davidma.tinymobfarm.core.util.ClientHooks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@Mod.EventBusSubscriber(modid = cn.davidma.tinymobfarm.core.Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientHooks.setShiftDown(ClientTooltipHelper::hasShiftDown);
        event.enqueueWork(() -> {
            ScreenManager.register(ModContainers.MOB_FARM.get(), GuiMobFarm::new);
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.MOB_FARM.get(), RenderMobFarm::new);
            ModBlocks.getMobFarms().forEach(block -> RenderTypeLookup.setRenderLayer(block.get(), RenderType.cutout()));
        });
    }
}
