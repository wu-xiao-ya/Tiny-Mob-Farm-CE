package cn.davidma.tinymobfarm.core;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public final class ConfigTinyMobFarm {
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ForgeConfigSpec.IntValue LASSO_DURABILITY;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOB_BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Number>> MOB_FARM_SPEED;
    public static final ForgeConfigSpec.IntValue OUTPUT_RETRY_INTERVAL_TICKS;
    public static final ForgeConfigSpec.BooleanValue RENDER_FARM_MOB_MODEL;
    public static final ForgeConfigSpec.BooleanValue PAUSE_WHEN_OUTPUT_FULL;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("lasso");
        LASSO_DURABILITY = builder
                .comment("Durability of the lasso.")
                .defineInRange("lassoDurability", 256, 1, Integer.MAX_VALUE);
        MOB_BLACKLIST = builder
                .comment("Blacklist of mobs that cannot be captured, e.g. minecraft:cow.")
                .defineList("mobBlacklist", Arrays.asList(), value -> value instanceof String);
        builder.pop();

        builder.push("farm");
        MOB_FARM_SPEED = builder
                .comment("Seconds required for each farm tier to generate loot once.")
                .defineList("mobFarmSpeed", Arrays.asList(50.0D, 40.0D, 30.0D, 20.0D, 10.0D, 5.0D, 2.5D, 0.5D),
                        value -> value instanceof Double || value instanceof Integer);
        OUTPUT_RETRY_INTERVAL_TICKS = builder
                .comment("Ticks between retry attempts when adjacent inventories cannot accept generated drops.")
                .defineInRange("outputRetryIntervalTicks", 20, 1, 3600);
        PAUSE_WHEN_OUTPUT_FULL = builder
                .comment("Pause before completing if generated drops cannot fully fit into adjacent inventories.")
                .define("pauseWhenOutputFull", true);
        RENDER_FARM_MOB_MODEL = builder
                .comment("Render the captured mob model inside mob farm blocks.")
                .define("renderFarmMobModel", true);
        builder.pop();

        COMMON_SPEC = builder.build();
    }

    private ConfigTinyMobFarm() {
    }

    public static int getLassoDurability() {
        return LASSO_DURABILITY.get();
    }

    public static boolean isMobBlacklisted(String registryName) {
        for (String name : MOB_BLACKLIST.get()) {
            if (registryName.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static int getFarmRateTicks(MobFarmTier tier) {
        if (tier == null) {
            return 20;
        }
        List<? extends Number> speeds = MOB_FARM_SPEED.get();
        int index = tier.ordinal();
        double seconds = index >= 0 && index < speeds.size() ? speeds.get(index).doubleValue() : 20.0D;
        return Math.max(1, (int) Math.round(seconds * 20.0D));
    }

    public static int getOutputRetryIntervalTicks() {
        return OUTPUT_RETRY_INTERVAL_TICKS.get();
    }

    public static boolean shouldRenderFarmMobModel() {
        return RENDER_FARM_MOB_MODEL.get();
    }

    public static boolean shouldPauseWhenOutputFull() {
        return PAUSE_WHEN_OUTPUT_FULL.get();
    }
}
