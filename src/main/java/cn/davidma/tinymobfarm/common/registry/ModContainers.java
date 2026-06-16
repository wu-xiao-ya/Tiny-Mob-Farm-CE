package cn.davidma.tinymobfarm.common.registry;

import cn.davidma.tinymobfarm.common.container.ContainerMobFarm;
import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

    public static final RegistryObject<ContainerType<ContainerMobFarm>> MOB_FARM = CONTAINERS.register(
            "mob_farm",
            () -> IForgeContainerType.create((windowId, playerInventory, data) -> {
                World level = playerInventory.player.level;
                if (level == null) {
                    return new ContainerMobFarm(windowId, playerInventory, null, IWorldPosCallable.NULL);
                }

                BlockPos pos = data.readBlockPos();
                TileEntityMobFarm tileEntity = level.getBlockEntity(pos) instanceof TileEntityMobFarm
                        ? (TileEntityMobFarm) level.getBlockEntity(pos)
                        : null;
                return new ContainerMobFarm(windowId, playerInventory, tileEntity, IWorldPosCallable.create(level, pos));
            }));

    private ModContainers() {
    }
}
