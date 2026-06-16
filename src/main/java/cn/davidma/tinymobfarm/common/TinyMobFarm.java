package cn.davidma.tinymobfarm.common;

import cn.davidma.tinymobfarm.common.registry.ModBlocks;
import cn.davidma.tinymobfarm.common.registry.ModItems;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class TinyMobFarm {
    public static final TinyMobFarmItemGroup ITEM_GROUP = new TinyMobFarmItemGroup();

    public TinyMobFarm() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
