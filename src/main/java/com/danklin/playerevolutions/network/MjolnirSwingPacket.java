package com.danklin.playerevolutions.network;

import com.danklin.playerevolutions.items.Mjolnir;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MjolnirSwingPacket {
    public MjolnirSwingPacket() {}
    public static void encode(MjolnirSwingPacket msg, PacketBuffer buf) {}
    public static MjolnirSwingPacket decode(PacketBuffer buf) {
        return  new MjolnirSwingPacket();
    }

    public static void handle(MjolnirSwingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null && player.isAlive()) {
                ItemStack mainHand = player.getHeldItem(Hand.MAIN_HAND);

                if (mainHand.getItem() instanceof Mjolnir) {
                    Mjolnir mjolnir = (Mjolnir) mainHand.getItem();
                    mjolnir.summonRangedLightning(player.world, player, mainHand);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
