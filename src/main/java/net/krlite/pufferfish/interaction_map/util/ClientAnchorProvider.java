package net.krlite.pufferfish.interaction_map.util;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

import static net.krlite.pufferfish.interaction_map.render.AnchorRenderer.*;

public class ClientAnchorProvider {
    public static Map<AnchorCoordinate, Float> resolveSpawnPointPos(ServerPlayerEntity player) {
        return new HashMap<>();
    }
}
