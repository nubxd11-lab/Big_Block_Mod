package com.danklin.playerevolutions.util;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.items.Pistol;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PistolOverlay extends AbstractGui {

    private static final ResourceLocation PISTOL_SCOPE =
            new ResourceLocation(PlayerEvolutions.MOD_ID, "textures/items/pistol_scope.png");


    @SubscribeEvent
    public static void onFovUpdate(FOVUpdateEvent event) {
        PlayerEntity player = event.getEntity();
        if (player == null) return;

        ItemStack mainItem = player.getHeldItemMainhand();
        ItemStack offItem = player.getHeldItemOffhand();

        boolean isHoldingPistol = mainItem.getItem() instanceof Pistol || offItem.getItem() instanceof Pistol;

        if (isHoldingPistol && player.isHandActive()) {
            event.setNewfov(event.getFov() * 0.5F);
        }
    }


    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HELMET) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player == null) return;

        ItemStack mainItem = player.getHeldItemMainhand();
        ItemStack offItem = player.getHeldItemOffhand();

        boolean isHoldingPistol = mainItem.getItem() instanceof Pistol || offItem.getItem() instanceof Pistol;

        if (isHoldingPistol && player.isHandActive()) {
            int width = event.getWindow().getScaledWidth();
            int height = event.getWindow().getScaledHeight();

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            mc.getTextureManager().bindTexture(PISTOL_SCOPE);

            blit(0, 0, 0, 0, width, height, width, height);

            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}