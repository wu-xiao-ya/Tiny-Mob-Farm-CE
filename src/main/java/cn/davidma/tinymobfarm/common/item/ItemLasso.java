package cn.davidma.tinymobfarm.common.item;

import cn.davidma.tinymobfarm.core.util.EntityHelper;
import cn.davidma.tinymobfarm.core.util.NBTHelper;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ItemLasso extends Item {
    public ItemLasso(Properties properties) {
        super(properties);
    }

    public static boolean hasMob(ItemStack stack) {
        return NBTHelper.hasMob(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return hasMob(stack) || super.isFoil(stack);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (hasMob(stack)) {
            return ActionResultType.PASS;
        }

        if (EntityHelper.isBoss(target)) {
            if (!player.level.isClientSide) {
                player.displayClientMessage(new TranslationTextComponent("tinymobfarm.error.cannot_capture_boss"), true);
            }
            return ActionResultType.sidedSuccess(player.level.isClientSide);
        }

        if (!EntityHelper.canCapture(target)) {
            return ActionResultType.PASS;
        }

        if (EntityHelper.isMobBlacklisted(target)) {
            if (!player.level.isClientSide) {
                player.displayClientMessage(new TranslationTextComponent("tinymobfarm.error.blacklisted_mob"), true);
            }
            return ActionResultType.sidedSuccess(player.level.isClientSide);
        }

        if (!player.level.isClientSide) {
            captureMob(stack, player, target);
        }

        return ActionResultType.sidedSuccess(player.level.isClientSide);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        ItemStack stack = context.getItemInHand();
        if (!hasMob(stack)) {
            return ActionResultType.PASS;
        }

        PlayerEntity player = context.getPlayer();
        if (player != null && !player.mayUseItemAt(context.getClickedPos(), context.getClickedFace(), stack)) {
            return ActionResultType.FAIL;
        }

        if (!context.getLevel().isClientSide) {
            releaseMob(context);
        }

        return ActionResultType.sidedSuccess(context.getLevel().isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if (hasMob(stack)) {
            CompoundNBT nbt = NBTHelper.getBaseTag(stack);
            tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.release_mob"));
            tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.mob_name", nbt.getString(NBTHelper.MOB_NAME)));
            tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.health",
                    nbt.getDouble(NBTHelper.MOB_HEALTH), nbt.getDouble(NBTHelper.MOB_MAX_HEALTH)));
            if (nbt.getBoolean(NBTHelper.MOB_HOSTILE)) {
                tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.hostile"));
            }
        } else {
            tooltip.add(new TranslationTextComponent("tinymobfarm.tooltip.capture"));
        }
    }

    private static void captureMob(ItemStack stack, PlayerEntity player, LivingEntity target) {
        CompoundNBT mobData = new CompoundNBT();
        target.save(mobData);
        mobData.remove("Rotation");

        CompoundNBT nbt = new CompoundNBT();
        nbt.put(NBTHelper.MOB_DATA, mobData);
        nbt.putString(NBTHelper.MOB_NAME, target.getName().getString());
        nbt.putString(NBTHelper.MOB_LOOTTABLE_LOCATION, EntityHelper.getLootTableLocation(target));
        nbt.putDouble(NBTHelper.MOB_HEALTH, Math.round(target.getHealth() * 10.0F) / 10.0D);
        nbt.putDouble(NBTHelper.MOB_MAX_HEALTH, target.getMaxHealth());
        nbt.putBoolean(NBTHelper.MOB_HOSTILE, EntityHelper.isHostile(target));
        NBTHelper.setBaseTag(stack, nbt);

        if (player.isCreative()) {
            ItemStack newLasso = new ItemStack(stack.getItem());
            NBTHelper.setBaseTag(newLasso, nbt.copy());
            player.inventory.add(newLasso);
        }
        target.remove();

        player.inventory.setChanged();
        player.inventoryMenu.broadcastChanges();
    }

    private static void releaseMob(ItemUseContext context) {
        ItemStack stack = context.getItemInHand();
        CompoundNBT nbt = NBTHelper.getBaseTag(stack);
        CompoundNBT mobData = nbt.getCompound(NBTHelper.MOB_DATA).copy();
        BlockPos releasePos = context.getClickedPos().relative(context.getClickedFace());
        putPosition(mobData, releasePos);

        Entity entity = EntityType.loadEntityRecursive(mobData, context.getLevel(), loadedEntity -> {
            loadedEntity.setPos(releasePos.getX() + 0.5D, releasePos.getY(), releasePos.getZ() + 0.5D);
            return loadedEntity;
        });

        PlayerEntity player = context.getPlayer();
        boolean added = entity != null && context.getLevel().addFreshEntity(entity);

        if (added) {
            NBTHelper.clearMob(stack);
            if (player != null) {
                stack.hurtAndBreak(1, player, brokenPlayer -> brokenPlayer.broadcastBreakEvent(context.getHand()));
                player.inventory.setChanged();
                player.inventoryMenu.broadcastChanges();
            }
        } else if (player != null && !player.level.isClientSide) {
            player.displayClientMessage(new TranslationTextComponent("tinymobfarm.error.release_failed"), true);
        }
    }

    private static void putPosition(CompoundNBT mobData, BlockPos pos) {
        ListNBT position = new ListNBT();
        position.add(DoubleNBT.valueOf(pos.getX() + 0.5D));
        position.add(DoubleNBT.valueOf(pos.getY()));
        position.add(DoubleNBT.valueOf(pos.getZ() + 0.5D));
        mobData.put("Pos", position);
        mobData.remove("Passengers");
        mobData.remove("Leash");
        mobData.putFloat("FallDistance", 0.0F);
        if (!mobData.contains("id", Constants.NBT.TAG_STRING)) {
            mobData.putString("id", "");
        }
    }
}
