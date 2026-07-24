package com.danklin.playerevolutions.util;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.entities.BulletEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix3f;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class BulletRenderer extends EntityRenderer<BulletEntity> {

    private static final ResourceLocation BULLET_TEXTURE =
            new ResourceLocation(PlayerEvolutions.MOD_ID, "textures/items/bullet_texture.png");

    public BulletRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(BulletEntity entity) {
        return BULLET_TEXTURE;
    }

    @Override
    public void render(BulletEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();

        // 1. Orient bullet along its trajectory (Yaw & Pitch)
        float yaw = MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw);
        float pitch = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch);

        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(yaw - 90.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(pitch));

        // 2. Scale size so it's easy to spot in flight
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);

        // 3. Draw cross planes (front and back faces)
        IVertexBuilder builder = bufferIn.getBuffer(RenderType.getEntityCutout(this.getEntityTexture(entityIn)));
        MatrixStack.Entry entry = matrixStackIn.getLast();
        Matrix4f matrix4f = entry.getMatrix();
        Matrix3f matrix3f = entry.getNormal();

        float width = 0.15F;
        float length = 0.4F;

        // Plane 1: Front and Back
        drawQuad(matrix4f, matrix3f, builder, -length, -width, 0, length, width, 0, packedLightIn, false);
        drawQuad(matrix4f, matrix3f, builder, -length, -width, 0, length, width, 0, packedLightIn, true);

        // Plane 2: Vertical Cross (rotated)
        drawQuad(matrix4f, matrix3f, builder, -length, 0, -width, length, 0, width, packedLightIn, false);
        drawQuad(matrix4f, matrix3f, builder, -length, 0, -width, length, 0, width, packedLightIn, true);

        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private void drawQuad(Matrix4f m4, Matrix3f m3, IVertexBuilder builder, float x1, float y1, float z1, float x2, float y2, float z2, int light, boolean reverse) {
        if (!reverse) {
            builder.pos(m4, x1, y1, z1).color(255, 255, 255, 255).tex(0.0F, 0.0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(m3, 0, 1, 0).endVertex();
            builder.pos(m4, x2, y1, z1).color(255, 255, 255, 255).tex(1.0F, 0.0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(m3, 0, 1, 0).endVertex();
            builder.pos(m4, x2, y2, z2).color(255, 255, 255, 255).tex(1.0F, 1.0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(m3, 0, 1, 0).endVertex();
            builder.pos(m4, x1, y2, z2).color(255, 255, 255, 255).tex(0.0F, 1.0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(m3, 0, 1, 0).endVertex();
        } else {
            builder.pos(m4, x1, y2, z2).color(255, 255, 255, 255).tex(0.0F, 1.0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(m3, 0, -1, 0).endVertex();
            builder.pos(m4, x2, y2, z2).color(255, 255, 255, 255).tex(1.0F, 1.0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(m3, 0, -1, 0).endVertex();
            builder.pos(m4, x2, y1, z1).color(255, 255, 255, 255).tex(1.0F, 0.0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(m3, 0, -1, 0).endVertex();
            builder.pos(m4, x1, y1, z1).color(255, 255, 255, 255).tex(0.0F, 0.0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(m3, 0, -1, 0).endVertex();
        }
    }
}