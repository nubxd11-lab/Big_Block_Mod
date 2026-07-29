package com.danklin.playerevolutions.events;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.items.Mjolnir;
import com.danklin.playerevolutions.network.MjolnirSwingPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID, value = Dist.CLIENT)
public class MjolnirClientEvents {
    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.currentScreen != null || mc.player == null) return;

        if (mc.gameSettings.keyBindUseItem.isKeyDown()) return;
        PlayerEntity player = mc.player;
        ItemStack mainHand = player.getHeldItem(Hand.MAIN_HAND);

            if (mainHand.getItem() instanceof Mjolnir) {
                PlayerEvolutions.NETWORK.sendToServer(new MjolnirSwingPacket());
            }
    }
}
