package cn.davidma.tinymobfarm.common.registry;

import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<TileEntityType<TileEntityMobFarm>> MOB_FARM = TILE_ENTITIES.register(
            "mob_farm_tile_entity",
            () -> TileEntityType.Builder.of(TileEntityMobFarm::new,
                    ModBlocks.getMobFarms().stream().map(RegistryObject::get).toArray(net.minecraft.block.Block[]::new))
                    .build(null));

    private ModTileEntities() {
    }
}
