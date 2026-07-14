package cn.davidma.tinymobfarm.core.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import cn.davidma.tinymobfarm.core.ConfigTinyMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ConfigReloadHandler {

	private static final int CHECK_INTERVAL_TICKS = 20 * 10;

	private final Path configPath;
	private final Side physicalSide;
	private long lastModified;
	private int tickCounter;

	public ConfigReloadHandler() {
		this.configPath = Loader.instance().getConfigDir().toPath().resolve(Reference.MOD_ID + ".cfg");
		this.physicalSide = FMLCommonHandler.instance().getSide();
		this.lastModified = this.getCurrentLastModified();
	}

	@SubscribeEvent
	public void onClientConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (Reference.MOD_ID.equals(event.getModID())) {
			ConfigTinyMobFarm.syncConfig();
			this.lastModified = this.getCurrentLastModified();
			this.tickCounter = 0;
		}
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (this.physicalSide.isClient() && event.phase == TickEvent.Phase.END) {
			this.checkForExternalConfigChange();
		}
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (this.physicalSide.isServer() && event.phase == TickEvent.Phase.END) {
			this.checkForExternalConfigChange();
		}
	}

	private void checkForExternalConfigChange() {
		this.tickCounter++;
		if (this.tickCounter < CHECK_INTERVAL_TICKS) {
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
		try {
			BasicFileAttributes attributes = Files.readAttributes(
					this.configPath, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
			return attributes.isRegularFile() ? attributes.lastModifiedTime().toMillis() : -1L;
		} catch (IOException ignored) {
			return -1L;
		}
	}
}
