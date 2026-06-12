package cn.davidma.tinymobfarm.core;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;

@Config(modid = Reference.MOD_ID, name = Reference.MOD_ID)
public class ConfigTinyMobFarm {

	public static void syncConfig() {
		ConfigManager.sync(Reference.MOD_ID, Type.INSTANCE);
	}

	@Name("Lasso Durability")
	@Comment({"套索的耐久度。", "Durability of the lasso."})
	public static int LASSO_DURABILITY = 256;
	
	@Name("Mob Farm Rate")
	@Comment({"每个等级农场生成一次掉落所需的秒数。", "Seconds required for each farm tier to generate loot once."})
	public static double[] MOB_FARM_SPEED = {50.0, 40.0, 30.0, 20.0, 10.0, 5.0, 2.5, 0.5};

	@Comment({"是否在客户端渲染农场内部的生物模型；当视野内有大量农场时可关闭以提升帧率。", "Whether to render captured mob models inside mob farms on the client; disable this to improve FPS when many farms are visible."})
	@Name("Render Farm Mob Model")
	public static boolean RENDER_FARM_MOB_MODEL = true;

	@Comment({"启用后，如果本轮掉落无法完整放入相邻物品容器，农场会暂停在完成前；不会扣除套索耐久，也不会把产物掉到世界中。", "When enabled, farms pause before completing if the generated drops cannot fully fit into adjacent item handlers; no lasso durability is consumed and no items are dropped into the world."})
	@Name("Pause When Output Full")
	public static boolean PAUSE_WHEN_OUTPUT_FULL = true;
	
	@Comment({"禁止被套索捕捉的生物注册名列表，例如 minecraft:cow。", "Blacklist of mobs that cannot be captured, e.g. minecraft:cow."})
	@Name("Mob Blacklist")
	public static String[] MOB_BLACKLIST = {};
}
