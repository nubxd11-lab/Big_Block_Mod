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


}