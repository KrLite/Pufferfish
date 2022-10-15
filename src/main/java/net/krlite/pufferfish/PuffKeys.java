package net.krlite.pufferfish;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PuffKeys {
    public static int lingerPitch = 0;
    public static int lingerYaw = 0;

    public static boolean avaliablePitch = true;
    public static boolean avaliableYaw = true;

    // Axis X & Z
    public static final KeyBinding LOCK_PITCH = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.puff.lock_coordinate_pitch",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.puff.locks"
    ));

    // Axis Y
    public static final KeyBinding LOCK_YAW = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.puff.lock_coordinate_yaw",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "category.puff.locks"
    ));

    // Prefix Key
    public static final KeyBinding LOCK_PREFIX = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.puff.lock_prefix",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,
            "category.puff.locks"
    ));

    private static void lerp() {
        lingerPitch -= lingerPitch == 0 ? 0 : 1;
        lingerYaw -= lingerYaw == 0 ? 0 : 1;
    }

    public static void update() {
        lerp();
    }



    public static void registerKeys() {
    }
}
