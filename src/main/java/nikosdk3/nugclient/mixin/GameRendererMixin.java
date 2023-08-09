package nikosdk3.nugclient.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import nikosdk3.nugclient.NugClient;
import nikosdk3.nugclient.events.EventStore;
import nikosdk3.nugclient.events.event.RenderEvent;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.modules.render.NoHurtCam;
import nikosdk3.nugclient.utils.RenderUtils;
import nikosdk3.nugclient.utils.Utils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static nikosdk3.nugclient.utils.Utils.mc;
import static org.lwjgl.opengl.GL32C.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL32C.glDisable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    MinecraftClient client;

    @Shadow
    @Final
    private Camera camera;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = {"ldc=hand"}), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void renderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info) {
        if (!Utils.canUpdate()) return;

        client.getProfiler().push(NugClient.MOD_ID + "_render");

        RenderUtils.updateScreenCenter();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();

        matrixStack.multiplyPositionMatrix(matrices.peek().getPositionMatrix());
        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
        matrixStack.translate(0, -cameraPos.y, 0);

        RenderUtils.beginLines();
        RenderUtils.beginQuads();

        RenderEvent event = EventStore.renderEvent(tickDelta);
        NugClient.eventBus.post(event);

        RenderUtils.endQuads();
        RenderUtils.endLines();
        RenderSystem.getModelViewStack().pop();

        glDisable(GL_LINE_SMOOTH);
        GlStateManager._enableCull();
        GlStateManager._disableBlend();
        GlStateManager._enableDepthTest();

        RenderSystem.applyModelViewMatrix();
        client.getProfiler().pop();
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onTiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        if(ModuleManager.get(NoHurtCam.class).isActive())
            info.cancel();
    }
}
