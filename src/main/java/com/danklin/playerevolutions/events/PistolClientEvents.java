package com.danklin.playerevolutions.events;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.items.Pistol;
import com.danklin.playerevolutions.network.PistolShootPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID, value = Dist.CLIENT)
public class PistolClientEvents {

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.getButton() == 0 && event.getAction() == 1 && mc.currentScreen == null) {
            PlayerEntity player = mc.player;

            if (player != null) {
                ItemStack mainHand = player.getHeldItemMainhand();

                if (mainHand.getItem() instanceof Pistol) {
                    PlayerEvolutions.NETWORK.sendToServer(new PistolShootPacket());
                }
            }
        }
    }
}