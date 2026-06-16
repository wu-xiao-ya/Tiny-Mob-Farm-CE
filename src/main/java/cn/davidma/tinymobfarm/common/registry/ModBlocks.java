package cn.davidma.tinymobfarm.common.registry;

import cn.davidma.tinymobfarm.common.block.BlockMobFarm;
import cn.davidma.tinymobfarm.core.MobFarmTier;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);

    private static final Map<String, RegistryObject<BlockMobFarm>> MOB_FARMS = new LinkedHashMap<>();

    static {
        for (MobFarmTier tier : MobFarmTier.values()) {
            registerFarm(tier, false);
            registerFarm(tier, true);
        }
    }

    private ModBlocks() {
    }

    private static void registerFarm(MobFarmTier tier, boolean mechanical) {
        String name = tier.getRegistryName(mechanical);
        MOB_FARMS.put(name, BLOCKS.register(name, () -> new BlockMobFarm(tier, mechanical)));
    }

    public static Collection<RegistryObject<BlockMobFarm>> getMobFarms() {
        return MOB_FARMS.values();
    }
}
