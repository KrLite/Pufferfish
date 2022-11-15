package net.krlite.pufferfish.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.krlite.pufferfish.PuffKeys;
import net.krlite.pufferfish.config.ConfigScreenHandler;
import net.krlite.pufferfish.config.PuffConfigs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import static net.krlite.pufferfish.PuffKeys.*;
import static net.krlite.pufferfish.render.CrosshairPuffer.crosshairScaleTarget;
import static net.krlite.pufferfish.util.AxisUtil.*;

public class InputEventHandler {
    private static void registerInputEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            GameOptions options = MinecraftClient.getInstance().options;

            crosshairScaleTarget =
                    options.attackKey.isPressed()
                            ? !options.useKey.isPressed()
                                // Attacking
                                ? PuffConfigs.crosshairSize * (1 + PuffConfigs.crosshairPuff)
                                // Both
                                : PuffConfigs.crosshairSize
                            : options.useKey.isPressed()
                                // Using
                                ? PuffConfigs.crosshairSize * MathHelper.clamp(1.0 - PuffConfigs.crosshairPuff * 0.6, 0.3, 1.0)
                                // None
                                : PuffConfigs.crosshairSize;

            if ( CONFIG.wasPressed() ) {
                MinecraftClient.getInstance().setScreen(
                        ConfigScreenHandler.buildConfigScreen(MinecraftClient.getInstance().currentScreen)
                );
            }

            PlayerEntity player = MinecraftClient.getInstance().player;
            if ( player != null ) {
                playerUuid = player.getUuid();
                if ( FLIP_PREFIX.isPressed() ) {
                    if ( LOCK_PITCH.wasPressed() && Math.abs(player.getPitch()) >= 0.5F ) {
                        applyPitch(-player.getPitch());
                        flippingAxisPitch = true;
                    }

                    if ( LOCK_YAW.wasPressed() ) {
                        applyYaw(player.getYaw() + 90.0F * (options.sneakKey.isPressed() ? -1 : 1));
                        flippingAxisYaw = true;
                    }
                }

                else {
                    if ( LOCK_PITCH.wasPressed() && availableKeyAxis.get(Axis.PITCH) ) {
                        lingerKeyAxis.replace(Axis.YAW, 0);

                        if ( lingerKeyAxis.get(Axis.PITCH) == 0 ) {  // Trigger When Pitch Locked
                            lingerKeyAxis.replace(Axis.PITCH, PuffConfigs.keyLingerTicks);

                            lockPitch(player);

                            axisLock.replace(Axis.PITCH, !axisLock.get(Axis.PITCH));
                        }

                        else {    // Trigger When Pitch Applied
                            lingerKeyAxis.replace(Axis.PITCH, 0);

                            applyPitch(player);

                            axisLock.replace(Axis.PITCH, false);
                        }
                    }

                    if ( LOCK_YAW.wasPressed() && availableKeyAxis.get(Axis.YAW) ) {
                        lingerKeyAxis.replace(Axis.PITCH, 0);

                        if ( lingerKeyAxis.get(Axis.YAW) == 0 ) {   // Trigger When Yaw Locked
                            lingerKeyAxis.replace(Axis.YAW, PuffConfigs.keyLingerTicks);

                            lockYaw(player);

                            axisLock.replace(Axis.YAW, !axisLock.get(Axis.YAW));
                        }

                        else {    // Trigger When Yaw Applied
                            lingerKeyAxis.replace(Axis.YAW, 0);

                            applyYaw(player);

                            axisLock.replace(Axis.YAW, false);
                        }
                    }
                }
            }

            lingerKeyAxis.replace(
                    Axis.PITCH,
                    lingerKeyAxis.get(Axis.PITCH) - (lingerKeyAxis.get(Axis.PITCH) == 0 ? 0 : 1)
            );

            lingerKeyAxis.replace(
                    Axis.YAW,
                    lingerKeyAxis.get(Axis.YAW) - (lingerKeyAxis.get(Axis.YAW) == 0 ? 0 : 1)
            );

            availableKeyAxis.replace(Axis.PITCH, !LOCK_PITCH.isPressed());
            availableKeyAxis.replace(Axis.YAW, !LOCK_YAW.isPressed());
        });
    }

    public static void init() {
        PuffKeys.registerKeys();
        registerInputEvents();
    }
}
