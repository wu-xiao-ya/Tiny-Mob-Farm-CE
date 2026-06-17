package cn.davidma.tinymobfarm.compat.crafttweaker;

import cn.davidma.tinymobfarm.core.drop.MobFarmDropManager;
import cn.davidma.tinymobfarm.core.drop.MobFarmDropRule;
import cn.davidma.tinymobfarm.core.drop.WeightedDrop;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.logger.ILogger;
import net.minecraft.item.ItemStack;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.tinymobfarm.MobDrops")
public final class CraftTweakerMobFarmDrops {
    private CraftTweakerMobFarmDrops() {
    }

    @ZenCodeType.Method
    public static void add(String mobRegistryName, IItemStack[] drops) {
        CraftTweakerAPI.apply(new AddDropsAction(mobRegistryName, drops));
    }

    @ZenCodeType.Method
    public static void add(String mobRegistryName, IItemStack drop) {
        CraftTweakerAPI.apply(new AddOneDropAction(mobRegistryName, drop, 100));
    }

    @ZenCodeType.Method
    public static void add(String mobRegistryName, IItemStack drop, int chance) {
        CraftTweakerAPI.apply(new AddOneDropAction(mobRegistryName, drop, chance));
    }

    @ZenCodeType.Method
    public static void add(String mobRegistryName, IItemStack drop, int minChance, int maxChance) {
        CraftTweakerAPI.apply(new AddOneDropAction(mobRegistryName, drop, minChance, maxChance));
    }

    @ZenCodeType.Method
    public static void add(String mobRegistryName, IItemStack drop, int minAmount, int maxAmount, int chance) {
        CraftTweakerAPI.apply(new AddOneDropAction(mobRegistryName, drop, minAmount, maxAmount, chance));
    }

    @ZenCodeType.Method
    public static void addChance(String mobRegistryName, IItemStack[] drops, int minAmount, int maxAmount, int chance) {
        CraftTweakerAPI.apply(new AddDropsAction(mobRegistryName, drops, minAmount, maxAmount, chance, chance));
    }

    @ZenCodeType.Method
    public static void remove(String mobRegistryName) {
        CraftTweakerAPI.apply(new RemoveDropsAction(mobRegistryName));
    }

    @ZenCodeType.Method
    public static void clear() {
        CraftTweakerAPI.apply(new ClearDropsAction());
    }

    private static List<WeightedDrop> convertDrops(IItemStack[] drops, int chance) {
        return convertDropsUsingStackAmount(drops, chance, chance);
    }

    private static List<WeightedDrop> convertDropsUsingStackAmount(IItemStack[] drops, int minChance, int maxChance) {
        return convertDrops(drops, 1, 1, minChance, maxChance, true);
    }

    private static List<WeightedDrop> convertDrops(IItemStack[] drops, int minAmount, int maxAmount, int minChance, int maxChance) {
        return convertDrops(drops, minAmount, maxAmount, minChance, maxChance, false);
    }

    private static List<WeightedDrop> convertDrops(IItemStack[] drops, int minAmount, int maxAmount,
                                                   int minChance, int maxChance, boolean useStackAmount) {
        List<WeightedDrop> result = new ArrayList<>();
        if (drops == null) {
            return result;
        }

        for (IItemStack drop : drops) {
            if (drop == null || drop.isEmpty()) {
                continue;
            }

            ItemStack stack = drop.getInternal();
            if (stack.isEmpty()) {
                continue;
            }

            if (useStackAmount) {
                result.add(new WeightedDrop(stack, stack.getCount(), stack.getCount(), minChance, maxChance));
            } else {
                result.add(new WeightedDrop(stack, minAmount, maxAmount, minChance, maxChance));
            }
        }
        return result;
    }

    private static final class AddDropsAction implements IAction {
        private final String mobRegistryName;
        private final IItemStack[] drops;
        private final int minAmount;
        private final int maxAmount;
        private final int minChance;
        private final int maxChance;
        private final boolean useStackAmount;

        private AddDropsAction(String mobRegistryName, IItemStack[] drops) {
            this(mobRegistryName, drops, 1, 1, 100, 100, true);
        }

        private AddDropsAction(String mobRegistryName, IItemStack[] drops,
                               int minAmount, int maxAmount, int minChance, int maxChance) {
            this(mobRegistryName, drops, minAmount, maxAmount, minChance, maxChance, false);
        }

        private AddDropsAction(String mobRegistryName, IItemStack[] drops, int minAmount, int maxAmount,
                               int minChance, int maxChance, boolean useStackAmount) {
            this.mobRegistryName = mobRegistryName;
            this.drops = drops;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.minChance = minChance;
            this.maxChance = maxChance;
            this.useStackAmount = useStackAmount;
        }

        @Override
        public void apply() {
            MobFarmDropManager.addRule(new MobFarmDropRule(this.mobRegistryName, this.convertDrops()));
        }

        @Override
        public String describe() {
            return "Adding Tiny Mob Farm drops for " + this.mobRegistryName;
        }

        @Override
        public boolean validate(ILogger logger) {
            boolean valid = isRuleValid(this.mobRegistryName, this.minAmount, this.maxAmount, this.minChance, this.maxChance)
                    && !this.convertDrops().isEmpty();
            if (!valid) {
                logger.error(this.describeInvalid());
            }
            return valid;
        }

        public String describeInvalid() {
            return "Invalid Tiny Mob Farm drops for " + this.mobRegistryName;
        }

        private List<WeightedDrop> convertDrops() {
            return this.useStackAmount
                    ? CraftTweakerMobFarmDrops.convertDrops(this.drops, this.minChance)
                    : CraftTweakerMobFarmDrops.convertDrops(this.drops, this.minAmount, this.maxAmount, this.minChance, this.maxChance);
        }
    }

    private static final class AddOneDropAction implements IAction {
        private final String mobRegistryName;
        private final IItemStack drop;
        private final int minAmount;
        private final int maxAmount;
        private final int minChance;
        private final int maxChance;
        private final boolean useStackAmount;

        private AddOneDropAction(String mobRegistryName, IItemStack drop, int chance) {
            this(mobRegistryName, drop, chance, chance);
        }

        private AddOneDropAction(String mobRegistryName, IItemStack drop, int minChance, int maxChance) {
            this.mobRegistryName = mobRegistryName;
            this.drop = drop;
            this.minAmount = 1;
            this.maxAmount = 1;
            this.minChance = minChance;
            this.maxChance = maxChance;
            this.useStackAmount = true;
        }

        private AddOneDropAction(String mobRegistryName, IItemStack drop, int minAmount, int maxAmount, int chance) {
            this.mobRegistryName = mobRegistryName;
            this.drop = drop;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.minChance = chance;
            this.maxChance = chance;
            this.useStackAmount = false;
        }

        @Override
        public void apply() {
            MobFarmDropManager.addRule(new MobFarmDropRule(this.mobRegistryName, this.convertDrops()));
        }

        @Override
        public String describe() {
            return "Adding Tiny Mob Farm drop for " + this.mobRegistryName;
        }

        @Override
        public boolean validate(ILogger logger) {
            boolean valid = isRuleValid(this.mobRegistryName, this.minAmount, this.maxAmount, this.minChance, this.maxChance)
                    && this.drop != null
                    && !this.drop.isEmpty();
            if (!valid) {
                logger.error(this.describeInvalid());
            }
            return valid;
        }

        public String describeInvalid() {
            return "Invalid Tiny Mob Farm drop for " + this.mobRegistryName;
        }

        private List<WeightedDrop> convertDrops() {
            IItemStack[] drops = this.drop == null ? new IItemStack[0] : new IItemStack[] {this.drop};
            return this.useStackAmount
                    ? CraftTweakerMobFarmDrops.convertDropsUsingStackAmount(drops, this.minChance, this.maxChance)
                    : CraftTweakerMobFarmDrops.convertDrops(drops, this.minAmount, this.maxAmount, this.minChance, this.maxChance);
        }
    }

    private static final class RemoveDropsAction implements IAction {
        private final String mobRegistryName;

        private RemoveDropsAction(String mobRegistryName) {
            this.mobRegistryName = mobRegistryName;
        }

        @Override
        public void apply() {
            MobFarmDropManager.removeRules(this.mobRegistryName);
        }

        @Override
        public String describe() {
            return "Removing Tiny Mob Farm drops for " + this.mobRegistryName;
        }

        @Override
        public boolean validate(ILogger logger) {
            boolean valid = this.mobRegistryName != null && !this.mobRegistryName.isEmpty();
            if (!valid) {
                logger.error("Invalid Tiny Mob Farm remove target");
            }
            return valid;
        }
    }

    private static final class ClearDropsAction implements IAction {
        @Override
        public void apply() {
            MobFarmDropManager.clearRules();
        }

        @Override
        public String describe() {
            return "Clearing Tiny Mob Farm drops";
        }
    }

    private static boolean isRuleValid(String mobRegistryName, int minAmount, int maxAmount, int minChance, int maxChance) {
        return mobRegistryName != null
                && !mobRegistryName.isEmpty()
                && minAmount > 0
                && maxAmount >= minAmount
                && minChance > 0
                && maxChance >= minChance
                && maxChance <= 100;
    }
}
