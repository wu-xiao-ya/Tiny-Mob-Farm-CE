package cn.davidma.tinymobfarm.common.container;

import cn.davidma.tinymobfarm.common.registry.ModContainers;
import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ContainerMobFarm extends Container {
    private final TileEntityMobFarm tileEntityMobFarm;
    private final IWorldPosCallable access;
    private final IntReferenceHolder progressData;

    public ContainerMobFarm(int windowId, PlayerInventory playerInventory, TileEntityMobFarm tileEntityMobFarm, IWorldPosCallable access) {
        super(ModContainers.MOB_FARM.get(), windowId);
        this.tileEntityMobFarm = tileEntityMobFarm;
        this.access = access;
        boolean clientSide = playerInventory.player.level.isClientSide;
        this.progressData = tileEntityMobFarm != null ? new IntReferenceHolder() {
            private int syncedProgressPixels;

            @Override
            public int get() {
                return clientSide ? this.syncedProgressPixels : tileEntityMobFarm.getProgressPixels(80);
            }

            @Override
            public void set(int value) {
                this.syncedProgressPixels = value;
            }
        } : new IntReferenceHolder() {
            private int syncedProgressPixels;

            @Override
            public int get() {
                return this.syncedProgressPixels;
            }

            @Override
            public void set(int value) {
                this.syncedProgressPixels = value;
            }
        };
        this.addDataSlot(this.progressData);

        IItemHandler itemHandler = tileEntityMobFarm != null ? tileEntityMobFarm.getInventory() : new ItemStackHandler(1);
        this.addSlot(new SlotLassoOnly(itemHandler, 0, 80, 25));

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    public TileEntityMobFarm getTileEntityMobFarm() {
        return this.tileEntityMobFarm;
    }

    public int getSyncProgressPixels() {
        return this.progressData.get();
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.tileEntityMobFarm != null && Container.stillValid(this.access, player, this.tileEntityMobFarm.getBlockState().getBlock());
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            int containerSlots = 1;
            int playerInventoryStart = containerSlots;
            int playerInventoryEnd = this.slots.size();
            if (index < containerSlots) {
                if (!this.moveItemStackTo(itemstack1, playerInventoryStart, playerInventoryEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }
}
