package com.danklin.playerevolutions.network;

import com.danklin.playerevolutions.items.Pistol;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PistolShootPacket {

    public PistolShootPacket() {}

    public static void encode(PistolShootPacket msg, PacketBuffer buf) {}

    public static PistolShootPacket decode(PacketBuffer buf) {
        return new PistolShootPacket();
    }

    public static void handle(PistolShootPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                ItemStack mainHand = player.getHeldItemMainhand();
                if (mainHand.getItem() instanceof Pistol) {
                    Pistol pistol = (Pistol) mainHand.getItem();
                    // Runs shoot on the SERVER side so the entity actually exists!
                    pistol.shoot(player.world, player, mainHand);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
