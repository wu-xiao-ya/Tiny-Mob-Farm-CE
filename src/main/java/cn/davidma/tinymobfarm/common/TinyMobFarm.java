package cn.davidma.tinymobfarm.common;

import cn.davidma.tinymobfarm.common.registry.ModBlocks;
import cn.davidma.tinymobfarm.common.registry.ModContainers;
import cn.davidma.tinymobfarm.common.registry.ModItems;
import cn.davidma.tinymobfarm.common.registry.ModTileEntities;
import cn.davidma.tinymobfarm.core.ConfigTinyMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class TinyMobFarm {
    public static final TinyMobFarmItemGroup ITEM_GROUP = new TinyMobFarmItemGroup();

    public TinyMobFarm() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigTinyMobFarm.COMMON_SPEC);
        ModBlocks.BLOCKS.register(modEventBus);
        ModContainers.CONTAINERS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModTileEntities.TILE_ENTITIES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
