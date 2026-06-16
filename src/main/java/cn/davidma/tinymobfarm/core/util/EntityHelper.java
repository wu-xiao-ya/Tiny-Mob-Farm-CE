package cn.davidma.tinymobfarm.core.util;

import cn.davidma.tinymobfarm.core.ConfigTinyMobFarm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

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

    public static boolean canCapture(LivingEntity entity) {
        return entity instanceof MobEntity
                && !(entity instanceof WitherEntity)
                && !(entity instanceof EnderDragonEntity)
                && !entity.isDeadOrDying();
    }

    public static String getLootTableLocation(LivingEntity entity) {
        if (entity instanceof MobEntity) {
            return ((MobEntity) entity).getLootTable().toString();
        }
        return "";
    }
}
