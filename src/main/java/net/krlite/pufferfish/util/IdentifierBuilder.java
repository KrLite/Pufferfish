package net.krlite.pufferfish.util;

import net.krlite.pufferfish.PuffMod;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.stream.Collectors;

public class IdentifierBuilder {

    public static Identifier texture(String... pathsAndName) {
        return new Identifier(
                PuffMod.MOD_ID,
                "textures"
                        + Arrays.stream(pathsAndName).map(p -> "/" + p).collect(Collectors.joining())
                        + ".png"
        );
    }

    public static Text translatableText(String category, String... keys) {
        return Text.translatable(translationKey(category, keys));
    }

    public static String translationKey(String category, String... keys) {
        return category + "." + PuffMod.MOD_ID + Arrays.stream(keys).map(k -> "." + k).collect(Collectors.joining());
    }
}
