package com.danklin.playerevolutions.client;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.util.RegistryHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID, value = Dist.CLIENT)
public class NightVisionOverlay {

    private static boolean wasActive = false;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;

        if (player == null) return;

        ItemStack mainHand = player.getHeldItemMainhand();
        ItemStack offHand = player.getHeldItemOffhand();

        ItemStack activeStack = ItemStack.EMPTY;
        if (mainHand.getItem() == RegistryHandler.NIGHT_VISION_GOGGLES.get()) {
            activeStack = mainHand;
        } else if (offHand.getItem() == RegistryHandler.NIGHT_VISION_GOGGLES.get()) {
            activeStack = offHand;
        }

        boolean isCurrentlyActive = false;

        if (!activeStack.isEmpty() && activeStack.hasTag()) {
            CompoundNBT nbt = activeStack.getOrCreateTag();
            isCurrentlyActive = nbt.getBoolean("active");
        }

        if (isCurrentlyActive) {
            int width = mc.getMainWindow().getScaledWidth();
            int height = mc.getMainWindow().getScaledHeight();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();

            AbstractGui.fill(0, 0, width, height, 0x3300FF33);

            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }

        if (wasActive && !isCurrentlyActive) {
            if (mc.gameRenderer.getShaderGroup() != null) {
                mc.gameRenderer.stopUseShader();
            }
        }

        wasActive = isCurrentlyActive;
    }
}