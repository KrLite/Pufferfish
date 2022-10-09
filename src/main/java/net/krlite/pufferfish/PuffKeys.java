package net.krlite.pufferfish;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class PuffKeys {
    public static int lingerXZ = 0;
    public static int lingerY = 0;

    public static boolean avaliableXZ = true;
    public static boolean avaliableY = true;

    public static void lerp() {
        lingerXZ -= lingerXZ == 0 ? 0 : 1;
        lingerY -= lingerY == 0 ? 0 : 1;
    }

    // Axis X & Z
    public static final KeyBinding LOCK_XZ = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.puff.lock_surface_xz",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.puff.locks"
    ));

    // Axis Y
    public static final KeyBinding LOCK_Y = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.puff.lock_surface_y",
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



    public static void registerKeys() {
    }
}
