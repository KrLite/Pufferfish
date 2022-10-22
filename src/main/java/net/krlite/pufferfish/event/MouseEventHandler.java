package net.krlite.pufferfish.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;

public class MouseEventHandler {
    public static double mouseX;
    public static double mouseY;

    public static boolean mouseClickedLeft(double xBegin, double yBegin, double xEnd, double yEnd) {
        Mouse mouse = MinecraftClient.getInstance().mouse;
        if ( xBegin >= xEnd ) {
            if ( xBegin == xEnd ) return false;
            double t = xEnd;
            xEnd = xBegin;
            xBegin = t;
        }

        if ( yBegin >= yEnd ) {
            if ( yBegin == yEnd ) return false;
            double t = yEnd;
            yEnd = yBegin;
            yBegin = t;
        }

        if ( mouse.wasLeftButtonClicked() ) {
            return xBegin <= mouseX && xEnd >= mouseX && yBegin <= mouseY && yEnd >= mouseY;
        }

        return false;
    }

    public static boolean mouseClickedRight(double xBegin, double yBegin, double xEnd, double yEnd) {
        Mouse mouse = MinecraftClient.getInstance().mouse;
        if ( xBegin >= xEnd ) {
            if ( xBegin == xEnd ) return false;
            double t = xEnd;
            xEnd = xBegin;
            xBegin = t;
        }

        if ( yBegin >= yEnd ) {
            if ( yBegin == yEnd ) return false;
            double t = yEnd;
            yEnd = yBegin;
            yBegin = t;
        }

        if ( mouse.wasRightButtonClicked() ) {
            return xBegin <= mouseX && xEnd >= mouseX && yBegin <= mouseY && yEnd >= mouseY;
        }

        return false;
    }

    private static void registerMouseEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            mouseX = MinecraftClient.getInstance().mouse.getX();
            mouseY = MinecraftClient.getInstance().mouse.getY();
        });
    }

    public static void init() {
        registerMouseEvents();
    }
}
