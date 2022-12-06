package net.krlite.pufferfish.config;

import com.google.gson.JsonObject;
import net.krlite.plumeconfig.config.PlumeConfig;
import net.krlite.plumeconfig.config.api.PlumeConfigApi;
import net.krlite.plumeconfig.option.*;
import net.krlite.plumeconfig.option.core.ILocalizable;

import static net.krlite.pufferfish.config.Default.*;

import java.awt.*;

public class PuffConfig implements PlumeConfigApi {
	public static final PlumeConfig config = new PlumeConfig("pufferfish", "config");

	// Enum
	public static final OptionEnum<HotbarPosition> HOTBAR_POSITION = new OptionEnum<>("hotbar_position", DEFAULT_HOTBAR_POSITION);
	public static final OptionEnum<CrosshairRenderStyle> CROSSHAIR_RENDER_STYLE = new OptionEnum<>("crosshair_render_style", DEFAULT_CROSSHAIR_RENDER_STYLE);
	public static final OptionEnum<CrosshairStyle> CROSSHAIR_STYLE = new OptionEnum<>("crosshair_style", DEFAULT_CROSSHAIR_STYLE);

	// Boolean
	public static final OptionBoolean ENABLE_TITLE_ANIMATION = new OptionBoolean("enable_title_animation", DEFAULT_ENABLE_TITLE_ANIMATION);
	public static final OptionBoolean ENABLE_CHAT_ANIMATION = new OptionBoolean("enable_chat_animation", DEFAULT_ENABLE_CHAT_ANIMATION);
	public static final OptionBoolean ENABLE_CHAT_TEXT_SHADOW = new OptionBoolean("enable_chat_text_shadow", DEFAULT_ENABLE_CHAT_TEXT_SHADOW);
	public static final OptionBoolean CHAT_SELF_HIGHLIGHTING = new OptionBoolean("chat_self_highlighting", DEFAULT_CHAT_SELF_HIGHLIGHTING);

	// Long
	public static final OptionLong KEY_LINGER_TICKS = new OptionLong("key_linger_ticks", DEFAULT_KEY_LINGER_TICKS);

	// Double
	public static final OptionDouble CROSSHAIR_SIZE = new OptionDouble("crosshair_size", DEFAULT_CROSSHAIR_SIZE);
	public static final OptionDouble CROSSHAIR_EXPAND_INDEX = new OptionDouble("crosshair_expand_index", DEFAULT_CROSSHAIR_EXPAND_INDEX);

	// Color
	public static final OptionColor CHAT_TEXT_COLOR = new OptionColor("chat_text_color", DEFAULT_CHAT_TEXT_COLOR);
	public static final OptionColor CHAT_BACKGROUND_COLOR = new OptionColor("chat_background_color", DEFAULT_CHAT_BACKGROUND_COLOR);
	public static final OptionColor PITCH_COLOR = new OptionColor("pitch_color", DEFAULT_PITCH_COLOR);
	public static final OptionColor YAW_COLOR = new OptionColor("yaw_color", DEFAULT_YAW_COLOR);


	public static void readConfig(JsonObject configs) {
		// Enum
		HOTBAR_POSITION.parse(configs);
		CROSSHAIR_RENDER_STYLE.parse(configs);
		CROSSHAIR_STYLE.parse(configs);

		// Boolean
		ENABLE_TITLE_ANIMATION.parse(configs);
		ENABLE_CHAT_ANIMATION.parse(configs);
		ENABLE_CHAT_TEXT_SHADOW.parse(configs);
		CHAT_SELF_HIGHLIGHTING.parse(configs);

		// Long
		KEY_LINGER_TICKS.parse(configs);

		// Double
		CROSSHAIR_SIZE.parse(configs);
		CROSSHAIR_EXPAND_INDEX.parse(configs);

		// Color
		CHAT_TEXT_COLOR.parse(configs);
		CHAT_BACKGROUND_COLOR.parse(configs);
		PITCH_COLOR.parse(configs);
		YAW_COLOR.parse(configs);
	}

	public static void writeConfig() {
		config.write(
				// General
				ENABLE_TITLE_ANIMATION,
				HOTBAR_POSITION,

				// Crosshair
				CROSSHAIR_SIZE,
				CROSSHAIR_EXPAND_INDEX,
				CROSSHAIR_RENDER_STYLE,
				CROSSHAIR_STYLE,

				// Chat
				ENABLE_CHAT_ANIMATION,
				ENABLE_CHAT_TEXT_SHADOW,
				CHAT_SELF_HIGHLIGHTING,
				CHAT_TEXT_COLOR,
				CHAT_BACKGROUND_COLOR,

				// Color
				PITCH_COLOR,
				YAW_COLOR,

				// Misc
				KEY_LINGER_TICKS
		);
	}

	@Override
	public void onInitialize() {
		registerDefaultValues();

		readConfig(config.read());
		writeConfig();
	}

	// Enum Classes
	// Hotbar Position
	public enum HotbarPosition implements ILocalizable {
		CENTER("Center", false),
		LEFT("Left", true);

		private final String name;
		private final boolean left;

		HotbarPosition(String name, boolean left) {
			this.name = name;
			this.left = left;
		}

		public boolean isLeft() {
			return this.left;
		}

		@Override
		public String getLocalizedName() {
			return this.name;
		}
	}

	// Crosshair Render Style
	public enum CrosshairRenderStyle implements ILocalizable {
		VANILLA("Vanilla"),
		PUFFERFISH("Pufferfish"),
		OPAQUE("Opaque");

		private final String style;

		CrosshairRenderStyle(String style) {
			this.style = style;
		}

		@Override
		public String getLocalizedName() {
			return this.style;
		}
	}

	// Crosshair Style
	public enum CrosshairStyle implements ILocalizable {
		EMPTY("Empty", 16),
		VANILLA("Vanilla", 0),
		CROSS("Cross", 1),
		SMALL_CROSS("Small Cross", 2),
		X_SHAPE("X-Shape", 3),
		SMALL_X_SHAPE("Small X-Shape", 4),
		AIM("Aim", 5),
		SMALL_AIM("Small Aim", 6),
		CIRCLE("Circle", 7),
		DOT("Dot", 8),
		SMALL_DOT("Small Dot", 9),
		HORIZONTAL("Horizontal", 10);

		private final String style;
		private final int index;

		CrosshairStyle(String style, int index) {
			this.style = style;
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		@Override
		public String getLocalizedName() {
			return this.style;
		}
	}
}
