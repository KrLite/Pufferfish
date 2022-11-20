package net.krlite.pufferfish.util;

import net.krlite.pufferfish.math.IdentifierSprite;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class HotBarUtil {
    public static float hotbarOffsetTarget = 0;
    public static float hotbarOffset = hotbarOffsetTarget;

    public static Identifier WIDGETS_TEXTURE = new Identifier("minecraft", "textures/gui/widgets.png");

    public static IdentifierSprite VANILLA_HOTBAR = new IdentifierSprite(
            WIDGETS_TEXTURE, 256, 0, 0, 182, 22
    );

    public static IdentifierSprite VANILLA_SELECTED_SLOT = new IdentifierSprite(
            WIDGETS_TEXTURE, 256, 0, 22, 24, 24
    );

    public static IdentifierSprite VANILLA_OFFHAND_SLOT_LEFT = new IdentifierSprite(
            WIDGETS_TEXTURE, 256, 24, 22, 29, 24
    );

    public static IdentifierSprite VANILLA_OFFHAND_SLOT_RIGHT= new IdentifierSprite(
            WIDGETS_TEXTURE, 256, 53, 22, 29, 24
    );

    public static void updateHotbarOffset(PlayerEntity player) {
        GameOptions options = MinecraftClient.getInstance().options;

        hotbarOffset = MathHelper.lerp(
                0.27F, hotbarOffset,
                hotbarOffsetTarget =
                        player.getOffHandStack().isEmpty()
                                ? 0
                                : options.getMainArm().getValue() == Arm.RIGHT
                                ? -1
                                : 1
        );
    }
}
