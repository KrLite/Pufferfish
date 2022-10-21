package net.krlite.pufferfish.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.krlite.pufferfish.util.IdentifierBuilder;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;
import java.io.IOException;

import static net.krlite.pufferfish.config.PuffConfigs.*;

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
                        .setDefaultValue((int) (defaultCrosshairSize * 100))
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
                        .setDefaultValue((int) (defaultCrosshairPuff * 100))
                        .setTooltip(IdentifierBuilder.translatableText("config", "crosshair", "puff", "tooltip"))
                        .setSaveConsumer(value -> crosshairPuff = value / 100.0)
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
                        .setDefaultValue(defaultKeyLingerTicks)
                        .setTooltip(IdentifierBuilder.translatableText("config", "keys", "linger_ticks", "tooltip"))
                        .setSaveConsumer(value -> keyLingerTicks = value)
                        .build()
        );

        // Category Colors
        ConfigCategory colors = builder.getOrCreateCategory(IdentifierBuilder.translatableText("config", "category", "colors"));

        // Ocean
        colors.addEntry(
                entryBuilder
                        .startColorField(
                                IdentifierBuilder.translatableText("config", "colors", "ocean"),
                                ocean.getRGB() & 0x00FFFFFF
                        )
                        .setDefaultValue(defaultOcean.getRGB() & 0x00FFFFFF)
                        //.setTooltip(IdentifierBuilder.translatableText("config", "colors", "ocean", "tooltip"))
                        .setSaveConsumer(value -> ocean = new Color(value, false))
                        .build()
        );

        // Scarlet
        colors.addEntry(
                entryBuilder
                        .startColorField(
                                IdentifierBuilder.translatableText("config", "colors", "scarlet"),
                                scarlet.getRGB() & 0x00FFFFFF
                        )
                        .setDefaultValue(defaultScarlet.getRGB() & 0x00FFFFFF)
                        //.setTooltip(IdentifierBuilder.translatableText("config", "colors", "scarlet", "tooltip"))
                        .setSaveConsumer(value -> scarlet = new Color(value, false))
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
