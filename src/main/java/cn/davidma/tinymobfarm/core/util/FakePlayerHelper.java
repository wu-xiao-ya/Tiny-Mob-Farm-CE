package cn.davidma.tinymobfarm.core.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.UUID;

public final class FakePlayerHelper {
    private static final GameProfile GAME_PROFILE =
            new GameProfile(UUID.nameUUIDFromBytes("TinyMobFarm_DanielTheEgg".getBytes()),
                    "[TinyMobFarm_DanielTheEgg]");

    private FakePlayerHelper() {
    }

    public static FakePlayer getPlayer(ServerWorld world) {
        return FakePlayerFactory.get(world, GAME_PROFILE);
    }
}
