package net.krlite.pufferfish.util;

import net.krlite.pufferfish.PuffMod;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class IdentifierBuilder {

    public static Identifier texture(String... pathOrNames) {
        StringBuilder finalPath = new StringBuilder("textures");
        int index = 0;
        for ( String pathOrName : pathOrNames ) {
            finalPath.append("/").append(pathOrName);
            if ( ++index == pathOrNames.length ) {
                finalPath.append(".png");
            }
        }

        return new Identifier(PuffMod.MOD_ID, finalPath.toString());
    }

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
