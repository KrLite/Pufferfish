package net.krlite.pufferfish.util;

import net.krlite.pufferfish.PuffMod;

public class PuffIdentifier {
    public static String translationKey(String category, String... keys) {
        StringBuilder finalKey = new StringBuilder();
        for( String key : keys ) {
            finalKey.append(".").append(key);
        }

        return category + "." + PuffMod.MOD_ID + finalKey;
    }
}
