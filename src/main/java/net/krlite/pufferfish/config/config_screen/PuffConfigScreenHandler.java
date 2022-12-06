package net.krlite.pufferfish.config.config_screen;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.krlite.plumeconfig.option.core.Option;
import net.krlite.pufferfish.config_deprecated.PuffConfigs;
import net.krlite.pufferfish.util.ColorUtil;
import net.krlite.pufferfish.util.IdentifierBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.Arm;

import java.awt.*;

import static net.krlite.pufferfish.config.Default.*;
import static net.krlite.pufferfish.config.PuffConfig.*;

public class PuffConfigScreenHandler {
    private static <T> void setVanilla(SimpleOption<T> simpleOption, T value) {
        if ( value != null ) {
            simpleOption.setValue(value);
            MinecraftClient.getInstance().options.write();
        }
    }
    
    private static <T> void set(Option<T> option, T value) {
        if ( value != null ) {
            option.set(value);
            writeConfig();
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
                                ENABLE_TITLE_ANIMATION.getValue()
                        )
                        .setDefaultValue(DEFAULT_ENABLE_TITLE_ANIMATION)
                        .setTooltip(IdentifierBuilder.translatableText("config", "general", "enable_title_animation", "tooltip"))
                        .setSaveConsumer(value -> set(ENABLE_CHAT_ANIMATION, value))
                        .build()
        );

        // Hotbar Position
        general.addEntry(
                entryBuilder
                        .startEnumSelector(
                                IdentifierBuilder.translatableText("config", "general", "hotbar_position"),
                                HotbarPosition.class, HOTBAR_POSITION.getValue()
                        )
                        .setDefaultValue(DEFAULT_HOTBAR_POSITION)
                        .setTooltip(IdentifierBuilder.translatableText("config", "general", "hotbar_position", "tooltip"))
                        .setSaveConsumer(value -> set(HOTBAR_POSITION, value))
                        .build()
        );

        // Main Hand Position (vanilla)
        general.addEntry(
                entryBuilder
                        .startEnumSelector(
                                IdentifierBuilder.translatableText("config", "vanilla", "main_hand"),
                                Arm.class, options.getMainArm().getValue()
                        )
                        .setDefaultValue(Arm.RIGHT)
                        .setTooltip(IdentifierBuilder.translatableText("config", "vanilla", "main_hand", "tooltip"))
                        .setSaveConsumer(value -> setVanilla(options.getMainArm(), value))
                        .build()
        );

        // Category Crosshair
        ConfigCategory crosshair = builder.getOrCreateCategory(IdentifierBuilder.translatableText("config", "category", "crosshair"));

        // Crosshair Size
        crosshair.addEntry(
                entryBuilder
                        .startIntSlider(
                                IdentifierBuilder.translatableText("config", "crosshair", "size"),
                                (int) (CROSSHAIR_SIZE.getValue() * 100), 0, 300
                        )
                        .setDefaultValue((int) (DEFAULT_CROSSHAIR_SIZE * 100))
                        .setTooltip(IdentifierBuilder.translatableText("config", "crosshair", "size", "tooltip"))
                        .setSaveConsumer(value -> set(CROSSHAIR_SIZE, value / 100.0))
                        .build()
        );

        // Crosshair Puff
        crosshair.addEntry(
                entryBuilder
                        .startIntSlider(
                                IdentifierBuilder.translatableText("config", "crosshair", "puff"),
                                (int) (CROSSHAIR_EXPAND_INDEX.getValue() * 100), 0, 100
                        )
                        .setDefaultValue((int) (DEFAULT_CROSSHAIR_EXPAND_INDEX * 100))
                        .setTooltip(IdentifierBuilder.translatableText("config", "crosshair", "puff", "tooltip"))
                        .setSaveConsumer(value -> set(CROSSHAIR_EXPAND_INDEX, value / 100.0))
                        .build()
        );

        // Crosshair Render Style
        crosshair.addEntry(
                entryBuilder
                        .startEnumSelector(
                                IdentifierBuilder.translatableText("config", "crosshair", "render_style"),
                                CrosshairRenderStyle.class, CROSSHAIR_RENDER_STYLE.getValue()
                        )
                        .setDefaultValue(CrosshairRenderStyle.VANILLA)
                        .setTooltip(IdentifierBuilder.translatableText("config", "crosshair", "render_style", "tooltip"))
                        .setSaveConsumer(value -> set(CROSSHAIR_RENDER_STYLE, value))
                        .build()
        );

        // Crosshair Style
        crosshair.addEntry(
                entryBuilder
                        .startEnumSelector(
                                IdentifierBuilder.translatableText("config", "crosshair", "style"),
                                CrosshairStyle.class, CROSSHAIR_STYLE.getValue()
                        )
                        .setDefaultValue(DEFAULT_CROSSHAIR_STYLE)
                        .setTooltip(IdentifierBuilder.translatableText("config", "crosshair", "style", "tooltip"))
                        .setSaveConsumer(value -> set(CROSSHAIR_STYLE, value))
                        .build()
        );

        // Category Chat
        ConfigCategory chat = builder.getOrCreateCategory(IdentifierBuilder.translatableText("config", "category", "chat"));

        // Enable Chat Animation
        chat.addEntry(
                entryBuilder
                        .startBooleanToggle(
                                IdentifierBuilder.translatableText("config", "chat", "enable_animation"),
                                ENABLE_CHAT_ANIMATION.getValue()
                        )
                        .setDefaultValue(DEFAULT_ENABLE_CHAT_ANIMATION)
                        .setTooltip(IdentifierBuilder.translatableText("config", "chat", "enable_animation", "tooltip"))
                        .setSaveConsumer(value -> set(ENABLE_CHAT_ANIMATION, value))
                        .build()
        );

        // Enable Chat Text Shadow
        chat.addEntry(
                entryBuilder
                        .startBooleanToggle(
                                IdentifierBuilder.translatableText("config", "chat", "enable_text_shadow"),
                                ENABLE_CHAT_TEXT_SHADOW.getValue()
                        )
                        .setDefaultValue(DEFAULT_ENABLE_CHAT_TEXT_SHADOW)
                        .setTooltip(IdentifierBuilder.translatableText("config", "chat", "enable_text_shadow", "tooltip"))
                        .setSaveConsumer(value -> set(ENABLE_CHAT_TEXT_SHADOW, value))
                        .build()
        );

        /*
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
         */

        // Chat Text Color
        chat.addEntry(
                entryBuilder
                        .startColorField(
                                IdentifierBuilder.translatableText("config", "chat", "text_color"),
                                ColorUtil.clearAlpha(CHAT_TEXT_COLOR.getValue()).getRGB()
                        )
                        .setDefaultValue(ColorUtil.clearAlpha(DEFAULT_CHAT_TEXT_COLOR).getRGB())
                        //.setTooltip(IdentifierBuilder.translatableText("config", "colors", "chat", "text_color"))
                        .setSaveConsumer(value -> set(CHAT_TEXT_COLOR, new Color(value, true)))
                        .build()
        );

        // Chat Background Color
        chat.addEntry(
                entryBuilder
                        .startColorField(
                                IdentifierBuilder.translatableText("config", "chat", "background_color"),
                                ColorUtil.clearAlpha(CHAT_BACKGROUND_COLOR.getValue()).getRGB()
                        )
                        .setDefaultValue(ColorUtil.clearAlpha(DEFAULT_CHAT_BACKGROUND_COLOR).getRGB())
                        //.setTooltip(IdentifierBuilder.translatableText("config", "colors", "chat", "background_color"))
                        .setSaveConsumer(value -> set(CHAT_BACKGROUND_COLOR, new Color(value, true)))
                        .build()
        );

        // Chat Opacity (vanilla)
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

        // Text Background Opacity (vanilla)
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

        // Line Spacing (vanilla)
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
                        .startLongField(
                                IdentifierBuilder.translatableText("config", "keys", "linger_ticks"),
                                KEY_LINGER_TICKS.getValue()
                        )
                        .setDefaultValue(DEFAULT_KEY_LINGER_TICKS)
                        .setTooltip(IdentifierBuilder.translatableText("config", "keys", "linger_ticks", "tooltip"))
                        .setSaveConsumer(value -> set(KEY_LINGER_TICKS, value))
                        .build()
        );



        // Category Colors
        ConfigCategory colors = builder.getOrCreateCategory(IdentifierBuilder.translatableText("config", "category", "colors"));

        // Pitch
        colors.addEntry(
                entryBuilder
                        .startColorField(
                                IdentifierBuilder.translatableText("config", "colors", "pitch"),
                                ColorUtil.clearAlpha(PITCH_COLOR.getValue()).getRGB()
                        )
                        .setDefaultValue(ColorUtil.clearAlpha(DEFAULT_PITCH_COLOR).getRGB())
                        .setTooltip(IdentifierBuilder.translatableText("config", "colors", "pitch", "tooltip"))
                        .setSaveConsumer(value -> set(PITCH_COLOR, new Color(value, true)))
                        .build()
        );

        // Yaw
        colors.addEntry(
                entryBuilder
                        .startColorField(
                                IdentifierBuilder.translatableText("config", "colors", "yaw"),
                                ColorUtil.clearAlpha(YAW_COLOR.getValue()).getRGB()
                        )
                        .setDefaultValue(ColorUtil.clearAlpha(DEFAULT_YAW_COLOR).getRGB())
                        .setTooltip(IdentifierBuilder.translatableText("config", "colors", "yaw", "tooltip"))
                        .setSaveConsumer(value -> set(YAW_COLOR, new Color(value, true)))
                        .build()
        );

        return builder.build();
    }
}
