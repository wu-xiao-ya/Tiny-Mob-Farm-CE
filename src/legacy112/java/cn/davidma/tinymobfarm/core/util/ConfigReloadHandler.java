package cn.davidma.tinymobfarm.core.util;

import java.io.File;

import cn.davidma.tinymobfarm.core.ConfigTinyMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ConfigReloadHandler {

	private final File configFile;
	private long lastModified;
	private int tickCounter;

	public ConfigReloadHandler() {
		this.configFile = new File(Loader.instance().getConfigDir(), Reference.MOD_ID + ".cfg");
		this.lastModified = this.getCurrentLastModified();
	}

	@SubscribeEvent
	public void onClientConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (Reference.MOD_ID.equals(event.getModID())) {
			ConfigTinyMobFarm.syncConfig();
			this.lastModified = this.getCurrentLastModified();
		}
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			this.checkForExternalConfigChange();
		}
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			this.checkForExternalConfigChange();
		}
	}

	private void checkForExternalConfigChange() {
		this.tickCounter++;
		if (this.tickCounter < 20) {
			return;
		}

		this.tickCounter = 0;
		long currentLastModified = this.getCurrentLastModified();
		if (currentLastModified > 0L && currentLastModified != this.lastModified) {
			ConfigTinyMobFarm.syncConfig();
			this.lastModified = currentLastModified;
		}
	}

	private long getCurrentLastModified() {
		return this.configFile.isFile() ? this.configFile.lastModified() : -1L;
	}
}
