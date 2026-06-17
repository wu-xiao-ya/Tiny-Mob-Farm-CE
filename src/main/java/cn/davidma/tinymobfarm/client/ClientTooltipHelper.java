package cn.davidma.tinymobfarm.client;

import net.minecraft.client.gui.screen.Screen;

public final class ClientTooltipHelper {
    private ClientTooltipHelper() {
    }

    public static boolean hasShiftDown() {
        return Screen.hasShiftDown();
    }
}
