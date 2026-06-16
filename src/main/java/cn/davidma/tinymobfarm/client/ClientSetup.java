package cn.davidma.tinymobfarm.client;

import cn.davidma.tinymobfarm.client.gui.GuiMobFarm;
import cn.davidma.tinymobfarm.common.registry.ModContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = cn.davidma.tinymobfarm.core.Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ScreenManager.register(ModContainers.MOB_FARM.get(), GuiMobFarm::new));
    }
}
