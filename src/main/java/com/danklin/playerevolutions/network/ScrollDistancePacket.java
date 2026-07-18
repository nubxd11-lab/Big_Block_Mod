package com.danklin.playerevolutions.network;

import com.danklin.playerevolutions.items.ScopedCrossbow;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ScrollDistancePacket {
    private final double scrollDelta;

    public ScrollDistancePacket(double scrollDelta) {
        this.scrollDelta = scrollDelta;
    }

    public static void encode(ScrollDistancePacket msg, PacketBuffer buf) {
        buf.writeDouble(msg.scrollDelta);
    }

    public static ScrollDistancePacket decode(PacketBuffer buf) {
        return new ScrollDistancePacket(buf.readDouble());
    }

    public static void handle(ScrollDistancePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                ItemStack stack = player.getHeldItemMainhand();

                if (stack.getItem() instanceof ScopedCrossbow) {
                    CompoundNBT nbt = stack.getOrCreateTag();
                    float currentPower = nbt.contains("LaunchPower") ? nbt.getFloat("LaunchPower") : 1.5f;

                    currentPower += (msg.scrollDelta > 0 ? 0.5f : -0.5f);

                    currentPower = Math.max(0.5f, Math.min(3.5f, currentPower));

                    nbt.putFloat("LaunchPower", currentPower);

                    player.sendStatusMessage(new StringTextComponent("Launch Power: " + currentPower), true);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}