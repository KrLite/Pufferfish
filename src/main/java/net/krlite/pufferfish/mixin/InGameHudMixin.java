package net.krlite.pufferfish.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.krlite.pufferfish.PuffMod.LOGGER;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper implements InGameHudInvoker, MinecraftClientAccessor {
    @Shadow @Final private MinecraftClient client;
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void injected(MatrixStack matrices, CallbackInfo ci) {
        int itemUseCooldown = ((MinecraftClientAccessor) client).getItemUseCooldown();
        int attackCooldown = ((MinecraftClientAccessor) client).getAttackCooldown();
        boolean shouldRenderSpectatorCrosshair = ((InGameHudInvoker) new InGameHud(client)).invokeShouldRenderSpectatorCrosshair(client.crosshairTarget);

        GameOptions gameOptions = client.options;
        if ( gameOptions.getPerspective().isFirstPerson() ) {
            if ( client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR || shouldRenderSpectatorCrosshair ) {
                if ( gameOptions.debugEnabled && !gameOptions.hudHidden && !this.client.player.hasReducedDebugInfo() && !gameOptions.reducedDebugInfo ) {
                    Camera camera = client.gameRenderer.getCamera();
                    MatrixStack matrixStack = RenderSystem.getModelViewStack();
                    matrixStack.push();
                    matrixStack.translate((double) (this.scaledWidth / 2), (double) (this.scaledHeight / 2), (double) this.getZOffset());
                    matrixStack.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(camera.getPitch()));
                    matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw()));
                    matrixStack.scale(-1.0F, -1.0F, -1.0F);
                    RenderSystem.applyModelViewMatrix();
                    RenderSystem.renderCrosshair(7 + (int) (3 / this.client.player.getAttackCooldownProgress(0.0F)));
                    matrixStack.pop();
                    RenderSystem.applyModelViewMatrix();
                }

                else {
                    RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
                    this.drawTexture(matrices, (this.scaledWidth - 15) / 2, (this.scaledHeight - 15) / 2, 0, 0, 15, 15);
                    float f = this.client.player.getAttackCooldownProgress(0.0F);
                    if ( f < 1.0F ) {
                        this.drawTexture(matrices, (this.scaledWidth - 15) / 2 + (int) (Math.abs (f - 1) * 64.0F), (this.scaledHeight - 15) / 2 + 7, 0, 7, 15, 1);
                        this.drawTexture(matrices, (this.scaledWidth - 15) / 2 - (int) (Math.abs (f - 1) * 64.0F), (this.scaledHeight - 15) / 2 + 7, 0, 7, 15, 1);
                    }
                    if ( this.client.options.attackIndicator == AttackIndicator.CROSSHAIR ) {
                        boolean bl = false;
                        if ( this.client.targetedEntity != null && this.client.targetedEntity instanceof LivingEntity && f >= 1.0F ) {
                            bl = this.client.player.getAttackCooldownProgressPerTick() > 5.0F;
                            bl &= this.client.targetedEntity.isAlive();
                        }

                        int j = this.scaledHeight / 2 - 7 + 16;
                        int k = this.scaledWidth / 2 - 8;
                        if ( bl ) {
                            this.drawTexture(matrices, k, j, 68, 94, 16, 16);
                        }

                        else if ( f < 1.0F ) {
                            int l = (int) (f * 17.0F);
                            this.drawTexture(matrices, k, j, 36, 94, 16, 4);
                            this.drawTexture(matrices, k, j, 52, 94, l, 4);
                        }
                    }
                }

            }
        }
        ci.cancel();
    }

    @Override
    public int getItemUseCooldown() {
        return 0;
    }

    @Override
    public int getAttackCooldown() {
        return 0;
    }

    @Override
    public boolean invokeShouldRenderSpectatorCrosshair(HitResult hitResult) {
        return false;
    }
}
