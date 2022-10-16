package net.krlite.pufferfish;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.krlite.pufferfish.util.PuffIdentifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

import static net.krlite.pufferfish.util.AxisLocker.*;

public class PuffKeys {
    public static Map<Axis, Integer> lingerKeyAxis = new HashMap<>();
    public static Map<Axis, Boolean> availableKeyAxis = new HashMap<>();

    // Axis X & Z
    public static final KeyBinding LOCK_PITCH = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            PuffIdentifier.translationKey("key", "coordinate", "lock_pitch"),
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            PuffIdentifier.translationKey("category", "coordinate")
    ));

    // Axis Y
    public static final KeyBinding LOCK_YAW = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            PuffIdentifier.translationKey("key", "coordinate", "lock_yaw"),
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            PuffIdentifier.translationKey("category", "coordinate")
    ));

    // Prefix Key
    public static final KeyBinding FLIP_PREFIX = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            PuffIdentifier.translationKey("key", "coordinate", "flip_prefix"),
            InputUtil.Type.KEYSYM,
            MinecraftClient.IS_SYSTEM_MAC
                    ? GLFW.GLFW_KEY_LEFT_SUPER
                    : GLFW.GLFW_KEY_LEFT_ALT,
            PuffIdentifier.translationKey("category", "coordinate")
    ));

    private static void tick() {
        lingerKeyAxis.replace(
                Axis.PITCH,
                lingerKeyAxis.get(Axis.PITCH) - (lingerKeyAxis.get(Axis.PITCH) == 0 ? 0 : 1)
        );

        lingerKeyAxis.replace(
                Axis.YAW,
                lingerKeyAxis.get(Axis.YAW) - (lingerKeyAxis.get(Axis.YAW) == 0 ? 0 : 1)
        );
    }

    public static void update() {
        tick();
    }



    private static void registerKeyMaps() {
        lingerKeyAxis.putAll(ImmutableMap.of(
                Axis.PITCH, 0,
                Axis.YAW, 0
        ));

        availableKeyAxis.putAll(ImmutableMap.of(
                Axis.PITCH, true,
                Axis.YAW, true
        ));
    }

    public static void registerKeys() {
        registerKeyMaps();
    }
}
