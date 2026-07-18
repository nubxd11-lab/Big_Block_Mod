package com.danklin.playerevolutions.events;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.items.ScopedCrossbow;
import com.danklin.playerevolutions.network.PacketHandler;
import com.danklin.playerevolutions.network.ScrollDistancePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID, value = Dist.CLIENT)
public class ClientInputEvents {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;

        if (player != null && player.getHeldItemMainhand().getItem() instanceof ScopedCrossbow) {

            // Require Shift + Scroll to change distance!
            if (player.isCrouching()) {
                event.setCanceled(true); // Stops the hotbar from changing

                // Send the scroll data to the server
                PacketHandler.INSTANCE.sendToServer(new ScrollDistancePacket(event.getScrollDelta()));
            }
        }
    }
}