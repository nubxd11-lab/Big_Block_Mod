package com.danklin.playerevolutions.client;

import com.danklin.playerevolutions.PlayerEvolutions;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientColourHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        int sapphireHex = 0x0000FF;
        Minecraft.getInstance().getBlockColors().register((state, view, pos, tintIndex) -> sapphireHex, RegistryHandler.SAPPHIRE_BLOCK.get());
        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> sapphireHex, RegistryHandler.SAPPHIRE_BLOCK_ITEM.get());
    }
}
