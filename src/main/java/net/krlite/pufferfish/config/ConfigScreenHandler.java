package net.krlite.pufferfish.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.krlite.pufferfish.util.ChatUtil;
import net.krlite.pufferfish.util.ColorUtil;
import net.krlite.pufferfish.util.IdentifierBuilder;
import net.minecraft.client.MinecraftClient;
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

        // Category Chat
        ConfigCategory chat = builder.getOrCreateCategory(IdentifierBuilder.translatableText("config", "category", "chat"));

        // Enable Chat Animation
        chat.addEntry(
                entryBuilder
                        .startBooleanToggle(
                                IdentifierBuilder.translatableText("config", "chat", "enable_animation"),
                                enableChatAnimation
                        )
                        .setDefaultValue(DEFAULT_ENABLE_CHAT_ANIMATION)
                        .setTooltip(IdentifierBuilder.translatableText("config", "chat", "enable_animation", "tooltip"))
                        .setSaveConsumer(value -> enableChatAnimation = value)
                        .build()
        );

        // Enable Chat Text Shadow
        chat.addEntry(
                entryBuilder
                        .startBooleanToggle(
                                IdentifierBuilder.translatableText("config", "chat", "enable_text_shadow"),
                                enableChatTextShadow
                        )
                        .setDefaultValue(DEFAULT_ENABLE_CHAT_TEXT_SHADOW)
                        .setTooltip(IdentifierBuilder.translatableText("config", "chat", "enable_text_shadow", "tooltip"))
                        .setSaveConsumer(value -> enableChatTextShadow = value)
                        .build()
        );

        // Chat Text Color
        chat.addEntry(
                entryBuilder
                        .startColorField(
                                IdentifierBuilder.translatableText("config", "chat", "text_color"),
                                ChatUtil.chatTextColor.getRGB() & 0x00FFFFFF
                        )
                        .setDefaultValue(DEFAULT_CHAT_TEXT_COLOR.getRGB() & 0x00FFFFFF)
                        //.setTooltip(IdentifierBuilder.translatableText("config", "colors", "chat", "text_color"))
                        .setSaveConsumer(value -> ChatUtil.chatTextColor = new Color(value, false))
                        .build()
        );

        // Chat Opacity (Vanilla)
        chat.addEntry(
                entryBuilder
                        .startIntSlider(
                                IdentifierBuilder.translatableText("config", "vanilla", "chat_opacity"),
                                (int) (MinecraftClient.getInstance().options.chatOpacity * 100.0), 0, 100
                        )
                        .setDefaultValue(100)
                        .setTooltip(IdentifierBuilder.translatableText("config", "vanilla", "chat_opacity", "tooltip"))
                        .setSaveConsumer(value -> MinecraftClient.getInstance().options.chatOpacity = value / 100.0)
                        .build()
        );

        // Text Background Opacity (Vanilla)
        chat.addEntry(
                entryBuilder
                        .startIntSlider(
                                IdentifierBuilder.translatableText("config", "vanilla", "text_background_opacity"),
                                (int) (MinecraftClient.getInstance().options.textBackgroundOpacity * 100.0), 0, 100
                        )
                        .setDefaultValue(50)
                        .setTooltip(IdentifierBuilder.translatableText("config", "vanilla", "text_background_opacity", "tooltip"))
                        .setSaveConsumer(value -> MinecraftClient.getInstance().options.textBackgroundOpacity = value / 100.0)
                        .build()
        );

        // Line Spacing (Vanilla)
        chat.addEntry(
                entryBuilder
                        .startIntSlider(
                                IdentifierBuilder.translatableText("config", "vanilla", "chat_line_spacing"),
                                (int) (MinecraftClient.getInstance().options.chatLineSpacing * 100.0), 0, 100
                        )
                        .setDefaultValue(0)
                        .setTooltip(IdentifierBuilder.translatableText("config", "vanilla", "chat_line_spacing", "tooltip"))
                        .setSaveConsumer(value -> MinecraftClient.getInstance().options.chatLineSpacing = value / 100.0)
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
