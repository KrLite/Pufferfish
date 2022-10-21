package net.krlite.pufferfish.util;

import net.krlite.pufferfish.PuffMod;
import net.minecraft.text.TranslatableText;

public class IdentifierBuilder {
    public static TranslatableText translatableText(String category, String... keys) {
        return new TranslatableText(translationKey(category, keys));
    }

    public static String translationKey(String category, String... keys) {
        StringBuilder finalKey = new StringBuilder();
        for( String key : keys ) {
            finalKey.append(".").append(key);
        }

        return category + "." + PuffMod.MOD_ID + finalKey;
    }
}
