package cn.davidma.tinymobfarm.common.tileentity;

import cn.davidma.tinymobfarm.common.registry.ModItems;
import cn.davidma.tinymobfarm.common.registry.ModTileEntities;
import cn.davidma.tinymobfarm.common.container.ContainerMobFarm;
import cn.davidma.tinymobfarm.core.ConfigTinyMobFarm;
import cn.davidma.tinymobfarm.core.MobFarmTier;
import cn.davidma.tinymobfarm.core.util.NBTHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityMobFarm extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityMobFarm.this.currProgress = 0;
            TileEntityMobFarm.this.setChangedAndSync();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return TileEntityMobFarm.this.isLassoValid(stack);
        }
    };
    private final LazyOptional<IItemHandler> inventoryCapability = LazyOptional.of(() -> this.inventory);

    private MobFarmTier mobFarmTier;
    private int currProgress;
    private boolean mechanical;
    private boolean powered;

    public TileEntityMobFarm() {
        super(ModTileEntities.MOB_FARM.get());
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }

        boolean previousPowered = this.powered;
        this.updateRedstone();
        if (previousPowered != this.powered) {
            this.setChangedAndSync();
        }

        if (this.isWorking()) {
            int maxProgress = this.getMaxProgress();
            if (this.currProgress + 1 >= maxProgress) {
                this.currProgress = 0;
                this.setChangedAndSync();
            } else {
                this.currProgress++;
                this.setChanged();
            }
        } else if (this.currProgress != 0) {
            this.currProgress = 0;
            this.setChangedAndSync();
        }
    }

    public boolean isWorking() {
        ItemStack lasso = this.getLasso();
        return this.mobFarmTier != null
                && !this.powered
                && !lasso.isEmpty()
                && NBTHelper.hasMob(lasso)
                && (this.mobFarmTier.canFarmHostile() || !NBTHelper.hasHostileMob(lasso));
    }

    public void updateRedstone() {
        if (this.level != null) {
            this.powered = this.level.hasNeighborSignal(this.worldPosition);
        }
    }

    public ItemStack getLasso() {
        return this.inventory.getStackInSlot(0);
    }

    public void setLasso(ItemStack lasso) {
        this.inventory.setStackInSlot(0, lasso);
    }

    public void setMobFarmTier(MobFarmTier mobFarmTier) {
        this.mobFarmTier = mobFarmTier;
        this.setChangedAndSync();
    }

    public void setMechanical(boolean mechanical) {
        this.mechanical = mechanical;
        this.setChangedAndSync();
    }

    public boolean isMechanical() {
        return this.mechanical;
    }

    public boolean isPowered() {
        return this.powered;
    }

    public double getScaledProgress() {
        return this.mobFarmTier == null ? 0.0D : this.currProgress / (double) this.getMaxProgress();
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ContainerMobFarm(windowId, playerInventory, this, IWorldPosCallable.create(this.level, this.worldPosition));
    }

    public void dropLasso() {
        if (this.level == null || this.level.isClientSide || this.getLasso().isEmpty()) {
            return;
        }

        ItemStack lasso = this.getLasso().copy();
        this.setLasso(ItemStack.EMPTY);
        this.level.addFreshEntity(new ItemEntity(this.level,
                this.worldPosition.getX() + 0.5D,
                this.worldPosition.getY() + 0.3D,
                this.worldPosition.getZ() + 0.5D,
                lasso));
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        if (nbt.contains(NBTHelper.MOB_FARM_DATA)) {
            int ordinal = nbt.getInt(NBTHelper.MOB_FARM_DATA);
            MobFarmTier[] values = MobFarmTier.values();
            this.mobFarmTier = ordinal >= 0 && ordinal < values.length ? values[ordinal] : null;
        }
        this.currProgress = nbt.getInt(NBTHelper.CURR_PROGRESS);
        this.mechanical = nbt.getBoolean("Mechanical");
        this.powered = nbt.getBoolean("Powered");
        this.inventory.deserializeNBT(nbt.getCompound(NBTHelper.INVENTORY));
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        if (this.mobFarmTier != null) {
            nbt.putInt(NBTHelper.MOB_FARM_DATA, this.mobFarmTier.ordinal());
        }
        nbt.putInt(NBTHelper.CURR_PROGRESS, this.currProgress);
        nbt.putBoolean("Mechanical", this.mechanical);
        nbt.putBoolean("Powered", this.powered);
        nbt.put(NBTHelper.INVENTORY, this.inventory.serializeNBT());
        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.load(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (this.mechanical && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.inventoryCapability.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.inventoryCapability.invalidate();
    }

    private int getMaxProgress() {
        return Math.max(1, ConfigTinyMobFarm.getFarmRateTicks(this.mobFarmTier));
    }

    private boolean isLassoValid(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == ModItems.LASSO.get() && NBTHelper.hasMob(stack);
    }

    private void setChangedAndSync() {
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }
}
