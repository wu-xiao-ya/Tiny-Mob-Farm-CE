package cn.davidma.tinymobfarm.core;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public final class ConfigTinyMobFarm {
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ForgeConfigSpec.IntValue LASSO_DURABILITY;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOB_BLACKLIST;

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
}
