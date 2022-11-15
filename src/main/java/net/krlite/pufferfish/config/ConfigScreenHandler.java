package net.krlite.pufferfish.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.krlite.pufferfish.util.ChatUtil;
import net.krlite.pufferfish.util.ColorUtil;
import net.krlite.pufferfish.util.IdentifierBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;

import java.awt.*;
import java.io.IOException;

import static net.krlite.pufferfish.config.PuffConfigs.*;
import static net.krlite.pufferfish.util.Default.*;

public class ConfigScreenHandler {
    private static <T> void setVanilla(SimpleOption<T> simpleOption, T value) {
        if ( value != null ) {
            simpleOption.setValue(value);
            MinecraftClient.getInstance().options.write();
        }
    }

    public static Screen buildConfigScreen(Screen parent) {
        GameOptions options = MinecraftClient.getInstance().options;

        ConfigBuilder builder = ConfigBuilder
                .create()
                .setParentScreen(parent)
                .setTitle(IdentifierBuilder.translatableText("mod", "name"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Category General
        ConfigCategory general = builder.getOrCreateCategory(IdentifierBuilder.translatableText("config", "category", "general"));

        // Enable Title Animation
        general.addEntry(
                entryBuilder
                        .startBooleanToggle(
                                IdentifierBuilder.translatableText("config", "general", "enable_title_animation"),
                                enableTitleAnimation
                        )
                        .setDefaultValue(DEFAULT_ENABLE_TITLE_ANIMATION)
                        .setTooltip(IdentifierBuilder.translatableText("config", "general", "enable_title_animation", "tooltip"))
                        .setSaveConsumer(value -> enableTitleAnimation = value)
                        .build()
        );

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

        // Crosshair Render Style
        crosshair.addEntry(
                entryBuilder
                        .startEnumSelector(
                                IdentifierBuilder.translatableText("config", "crosshair", "render_style"),
                                CrosshairRenderStyle.class, corsshairRenderStyle
                        )
                        .setDefaultValue(CrosshairRenderStyle.VANILLA)
                        .setTooltip(IdentifierBuilder.translatableText("config", "crosshair", "render_style", "tooltip"))
                        .setSaveConsumer(value -> corsshairRenderStyle = value)
                        .build()
        );

        // Crosshair Style
        crosshair.addEntry(
                entryBuilder
                        .startEnumSelector(
                                IdentifierBuilder.translatableText("config", "crosshair", "style"),
                                CrosshairStyle.class, crosshairStyle
                        )
                        .setDefaultValue(CrosshairStyle.VANILLA)
                        .setTooltip(IdentifierBuilder.translatableText("config", "crosshair", "style", "tooltip"))
                        .setSaveConsumer(value -> crosshairStyle = value)
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

        // Chat Self Highlighting
        chat.addEntry(
                entryBuilder
                        .startBooleanToggle(
                                IdentifierBuilder.translatableText("config", "chat", "self_highlighting"),
                                chatSelfHighlighting
                        )
                        .setDefaultValue(DEFAULT_CHAT_SELF_HIGHLIGHTING)
                        .setTooltip(IdentifierBuilder.translatableText("config", "chat", "self_highlighting", "tooltip"))
                        .setSaveConsumer(value -> chatSelfHighlighting = value)
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

        // Chat Background Color
        chat.addEntry(
                entryBuilder
                        .startColorField(
                                IdentifierBuilder.translatableText("config", "chat", "background_color"),
                                ChatUtil.chatBackgroundColor.getRGB() & 0x00FFFFFF
                        )
                        .setDefaultValue(DEFAULT_CHAT_BACKGROUND_COLOR.getRGB() & 0x00FFFFFF)
                        //.setTooltip(IdentifierBuilder.translatableText("config", "colors", "chat", "background_color"))
                        .setSaveConsumer(value -> ChatUtil.chatBackgroundColor = new Color(value, false))
                        .build()
        );

        // Chat Opacity (Vanilla)
        chat.addEntry(
                entryBuilder
                        .startIntSlider(
                                IdentifierBuilder.translatableText("config", "vanilla", "chat_opacity"),
                                (int) (options.getChatOpacity().getValue() * 100.0), 0, 100
                        )
                        .setDefaultValue(100)
                        .setTooltip(IdentifierBuilder.translatableText("config", "vanilla", "chat_opacity", "tooltip"))
                        .setSaveConsumer(value -> setVanilla(options.getChatOpacity(), value / 100.0))
                        .build()
        );

        // Text Background Opacity (Vanilla)
        chat.addEntry(
                entryBuilder
                        .startIntSlider(
                                IdentifierBuilder.translatableText("config", "vanilla", "text_background_opacity"),
                                (int) (options.getTextBackgroundOpacity().getValue() * 100.0), 0, 100
                        )
                        .setDefaultValue(50)
                        .setTooltip(IdentifierBuilder.translatableText("config", "vanilla", "text_background_opacity", "tooltip"))
                        .setSaveConsumer(value -> setVanilla(options.getTextBackgroundOpacity(), value / 100.0))
                        .build()
        );

        // Line Spacing (Vanilla)
        chat.addEntry(
                entryBuilder
                        .startIntSlider(
                                IdentifierBuilder.translatableText("config", "vanilla", "chat_line_spacing"),
                                (int) (options.getChatLineSpacing().getValue() * 100.0), 0, 100
                        )
                        .setDefaultValue(0)
                        .setTooltip(IdentifierBuilder.translatableText("config", "vanilla", "chat_line_spacing", "tooltip"))
                        .setSaveConsumer(value -> setVanilla(options.getChatLineSpacing(), value / 100.0))
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
