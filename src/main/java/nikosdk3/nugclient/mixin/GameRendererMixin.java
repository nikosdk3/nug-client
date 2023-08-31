package nikosdk3.nugclient.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import nikosdk3.nugclient.NugClient;
import nikosdk3.nugclient.events.EventStore;
import nikosdk3.nugclient.events.event.RenderEvent;
import nikosdk3.nugclient.modules.ModuleManager;
import nikosdk3.nugclient.modules.render.NoHurtCam;
import nikosdk3.nugclient.utils.Color;
import nikosdk3.nugclient.utils.RenderUtils;
import nikosdk3.nugclient.utils.Utils;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    MinecraftClient client;

    @Shadow
    @Final
    private Camera camera;

    @Unique
    Color color = new Color(160, 0, 255, 255);

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0), method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V")
    public void renderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info) {
        if (!Utils.canUpdate()) return;

        client.getProfiler().swap(NugClient.MOD_ID + "_render");
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        matrices.push();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderUtils.applyOffset(matrices);
        RenderUtils.beginLines();
        RenderUtils.beginQuads();

        RenderEvent event = EventStore.renderEvent(matrices);
        NugClient.eventBus.post(event);

        RenderUtils.endQuads();
        RenderUtils.endLines();
        matrices.pop();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onTiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        if(ModuleManager.get(NoHurtCam.class).isActive())
            info.cancel();
    }
}
