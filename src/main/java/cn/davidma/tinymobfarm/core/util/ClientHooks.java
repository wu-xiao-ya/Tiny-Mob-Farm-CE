package cn.davidma.tinymobfarm.core.util;

import java.util.function.BooleanSupplier;

public final class ClientHooks {
    private static BooleanSupplier shiftDown = () -> false;

    private ClientHooks() {
    }

    public static boolean hasShiftDown() {
        return shiftDown.getAsBoolean();
    }

    public static void setShiftDown(BooleanSupplier shiftDown) {
        ClientHooks.shiftDown = shiftDown;
    }
}
