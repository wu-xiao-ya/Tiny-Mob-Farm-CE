package cn.davidma.tinymobfarm.core;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;

public enum MobFarmTier {
    WOOD("wood_farm", Material.WOOD, 1.0F, false, new int[] {2, 3, 3}),
    STONE("stone_farm", Material.STONE, 1.5F, false, new int[] {1, 2, 3}),
    IRON("iron_farm", Material.METAL, 5.0F, true, new int[] {1, 2}),
    GOLD("gold_farm", Material.METAL, 5.0F, true, new int[] {1, 1, 2}),
    DIAMOND("diamond_farm", Material.METAL, 5.0F, true, new int[] {1}),
    EMERALD("emerald_farm", Material.METAL, 5.0F, true, new int[] {0, 1, 1}),
    INFERNO("inferno_farm", Material.STONE, 50.0F, true, new int[] {0, 0, 1}),
    ULTIMATE("ultimate_farm", Material.STONE, 75.0F, true, new int[] {0});

    private final String registryName;
    private final Material material;
    private final float hardness;
    private final boolean canFarmHostile;
    private final int[] damageChance;

    MobFarmTier(String registryName, Material material, float hardness, boolean canFarmHostile, int[] damageChance) {
        this.registryName = registryName;
        this.material = material;
        this.hardness = hardness;
        this.canFarmHostile = canFarmHostile;
        this.damageChance = damageChance;
    }

    public String getRegistryName() {
        return this.registryName;
    }

    public String getRegistryName(boolean mechanical) {
        return mechanical ? this.registryName + "_machine" : this.registryName;
    }

    public boolean canFarmHostile() {
        return this.canFarmHostile;
    }

    public int[] getDamageChance() {
        return this.damageChance.clone();
    }

    public AbstractBlock.Properties createBlockProperties() {
        return AbstractBlock.Properties.of(this.material).strength(this.hardness, 300.0F).noOcclusion();
    }
}
