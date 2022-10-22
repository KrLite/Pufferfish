package net.krlite.pufferfish.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.krlite.pufferfish.util.ColorUtil;
import net.krlite.pufferfish.util.IdentifierBuilder;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;
import java.io.IOException;

import static net.krlite.pufferfish.config.PuffConfigs.*;
import static net.krlite.pufferfish.util.Default.*;

public class ConfigScreenHandler {
    public static Screen buildConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder
                .create()
                .setParentScreen(parent)
                .setTitle(IdentifierBuilder.translatableText("mod", "name"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Category Crosshair
        ConfigCategory crosshair = builder.getOrCreateCategory(IdentifierBuilder.translatableText("config", "category", "crosshair"));

        // Crosshair Size
        crosshair.addEntry(
                entryBuilder
                        .startIntSlider(
                                IdentifierBuilder.translatableText("config", "crosshair", "size"),
                                (int) (crosshairSize * 100), 0, 300
                        )
                        .setDefaultValue((int) (DEFAULT_CROSSHAIR_SIZE * 100))
                        .setTooltip(IdentifierBuilder.translatableText("config", "crosshair", "size", "tooltip"))
                        .setSaveConsumer(value -> crosshairSize = value / 100.0)
                        .build()
        );

        // Crosshair Puff
        crosshair.addEntry(
                entryBuilder
                        .startIntSlider(
                                IdentifierBuilder.translatableText("config", "crosshair", "puff"),
                                (int) (crosshairPuff * 100), 0, 100
                        )
                        .setDefaultValue((int) (DEFAULT_CROSSHAIR_PUFF * 100))
                        .setTooltip(IdentifierBuilder.translatableText("config", "crosshair", "puff", "tooltip"))
                        .setSaveConsumer(value -> crosshairPuff = value / 100.0)
                        .build()
        );

        // Crosshair Style
        crosshair.addEntry(
                entryBuilder
                        .startEnumSelector(
                                IdentifierBuilder.translatableText("config", "crosshair", "style"),
                                CrosshairStyle.class, corsshairStyle
                        )
                        .setDefaultValue(CrosshairStyle.VANILLA)
                        .setTooltip(IdentifierBuilder.translatableText("config", "crosshair", "style", "tooltip"))
                        .setSaveConsumer(value -> corsshairStyle = value)
                        .build()
        );



        // Category Keys
        ConfigCategory keys = builder.getOrCreateCategory(IdentifierBuilder.translatableText("config", "category", "keys"));

        // Key Linger Ticks
        keys.addEntry(
                entryBuilder
                        .startIntField(
                                IdentifierBuilder.translatableText("config", "keys", "linger_ticks"),
                                keyLingerTicks
                        )
                        .setDefaultValue(DEFAULT_KEY_LINGER_TICKS)
                        .setTooltip(IdentifierBuilder.translatableText("config", "keys", "linger_ticks", "tooltip"))
                        .setSaveConsumer(value -> keyLingerTicks = value)
                        .build()
        );



        // Category Colors
        ConfigCategory colors = builder.getOrCreateCategory(IdentifierBuilder.translatableText("config", "category", "colors"));

        // Pitch
        colors.addEntry(
                entryBuilder
                        .startColorField(
                                IdentifierBuilder.translatableText("config", "colors", "pitch"),
                                ColorUtil.pitchColor.getRGB() & 0x00FFFFFF
                        )
                        .setDefaultValue(DEFAULT_PITCH_COLOR.getRGB() & 0x00FFFFFF)
                        //.setTooltip(IdentifierBuilder.translatableText("config", "colors", "pitch", "tooltip"))
                        .setSaveConsumer(value -> ColorUtil.pitchColor = new Color(value, false))
                        .build()
        );

        // Yaw
        colors.addEntry(
                entryBuilder
                        .startColorField(
                                IdentifierBuilder.translatableText("config", "colors", "yaw"),
                                ColorUtil.yawColor.getRGB() & 0x00FFFFFF
                        )
                        .setDefaultValue(DEFAULT_YAW_COLOR.getRGB() & 0x00FFFFFF)
                        //.setTooltip(IdentifierBuilder.translatableText("config", "colors", "yaw", "tooltip"))
                        .setSaveConsumer(value -> ColorUtil.yawColor = new Color(value, false))
                        .build()
        );



        builder.setSavingRunnable(() -> {
            try {
                PuffConfigs.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return builder.build();
    }
}
