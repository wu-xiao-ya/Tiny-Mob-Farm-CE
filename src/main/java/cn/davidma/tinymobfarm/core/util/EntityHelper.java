package cn.davidma.tinymobfarm.core.util;

import cn.davidma.tinymobfarm.core.ConfigTinyMobFarm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public final class EntityHelper {
    private EntityHelper() {
    }

    public static String getRegistryName(Entity entity) {
        ResourceLocation id = ForgeRegistries.ENTITIES.getKey(entity.getType());
        return id == null ? "" : id.toString();
    }

    public static boolean isMobBlacklisted(Entity entity) {
        return ConfigTinyMobFarm.isMobBlacklisted(getRegistryName(entity));
    }

    public static boolean isHostile(LivingEntity entity) {
        return entity instanceof MonsterEntity;
    }

    public static boolean isBoss(LivingEntity entity) {
        return entity instanceof WitherEntity
                || entity instanceof EnderDragonEntity
                || !entity.canChangeDimensions();
    }

    public static boolean canCapture(LivingEntity entity) {
        return !isBoss(entity)
                && !entity.isDeadOrDying();
    }

    public static String getLootTableLocation(LivingEntity entity) {
        return entity.getType().getDefaultLootTable().toString();
    }

    public static List<ItemStack> generateLoot(ResourceLocation lootTableLocation, CompoundNBT mobData, ServerWorld world) {
        if (lootTableLocation == null) {
            return new ArrayList<>();
        }

        LootTable lootTable = world.getServer().getLootTables().get(lootTableLocation);
        Entity entity = EntityType.loadEntityRecursive(mobData.copy(), world, loadedEntity -> loadedEntity);
        if (entity == null) {
            return new ArrayList<>();
        }

        FakePlayer fakePlayer = FakePlayerHelper.getPlayer(world);
        LootContext context = new LootContext.Builder(world)
                .withParameter(LootParameters.THIS_ENTITY, entity)
                .withParameter(LootParameters.LAST_DAMAGE_PLAYER, fakePlayer)
                .withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.playerAttack(fakePlayer))
                .withParameter(LootParameters.KILLER_ENTITY, fakePlayer)
                .withParameter(LootParameters.DIRECT_KILLER_ENTITY, fakePlayer)
                .withParameter(LootParameters.ORIGIN, Vector3d.ZERO)
                .create(LootParameterSets.ENTITY);
        return lootTable.getRandomItems(context);
    }
}
