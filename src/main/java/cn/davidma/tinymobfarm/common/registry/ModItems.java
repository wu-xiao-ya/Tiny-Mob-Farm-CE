package cn.davidma.tinymobfarm.common.registry;

import cn.davidma.tinymobfarm.common.TinyMobFarm;
import cn.davidma.tinymobfarm.common.item.ItemLasso;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<ItemLasso> LASSO = ITEMS.register("lasso",
            () -> new ItemLasso(new Item.Properties().tab(TinyMobFarm.ITEM_GROUP)));

    static {
        ModBlocks.getMobFarms().forEach(block -> ITEMS.register(block.getId().getPath(),
                () -> new BlockItem(block.get(), new Item.Properties().tab(TinyMobFarm.ITEM_GROUP))));
    }

    private ModItems() {
    }
}
