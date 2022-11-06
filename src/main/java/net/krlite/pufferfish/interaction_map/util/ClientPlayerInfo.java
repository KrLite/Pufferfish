package net.krlite.pufferfish.interaction_map.util;

import net.krlite.pufferfish.PuffMod;
import net.minecraft.util.dynamic.GlobalPos;

import java.util.Optional;

public class ClientPlayerInfo {
    public static Optional<GlobalPos> lastDeathPos = Optional.empty();

    public static void setLastDeathPos(Optional<GlobalPos> lastDeathPos) {
        ClientPlayerInfo.lastDeathPos = lastDeathPos;
        PuffMod.LOGGER.warn(lastDeathPos.map(GlobalPos::toString).orElse("null"));
    }
}
