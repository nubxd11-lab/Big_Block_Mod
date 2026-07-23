package com.danklin.playerevolutions.network;

import com.danklin.playerevolutions.tileentities.MortarTileEntity;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFireMortar {

    private final BlockPos pos;
    private final double distance;
    private final double yaw;

    public PacketFireMortar(BlockPos pos, double distance, double yaw) {
        this.pos = pos;
        this.distance = distance;
        this.yaw = yaw;
    }

    public static void encode(PacketFireMortar msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeDouble(msg.distance);
        buf.writeDouble(msg.yaw);
    }

    public static PacketFireMortar decode(PacketBuffer buf) {
        return new PacketFireMortar(buf.readBlockPos(), buf.readDouble(), buf.readDouble());
    }

    public static void handle(PacketFireMortar msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null && player.world.isBlockLoaded(msg.pos)) {

                boolean isCreative = player.isCreative();
                boolean hasGrenade = isCreative;
                int grenadeSlot = -1;

                if (!isCreative) {
                    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                        ItemStack stack = player.inventory.getStackInSlot(i);
                        if (!stack.isEmpty() && stack.getItem() == RegistryHandler.GRENADE.get()) {
                            hasGrenade = true;
                            grenadeSlot = i;
                            break;
                        }
                    }
                }

                if (hasGrenade) {
                    TileEntity te = player.world.getTileEntity(msg.pos);
                    if (te instanceof MortarTileEntity) {
                        MortarTileEntity mortar = (MortarTileEntity) te;
                        mortar.setTargetDistance(msg.distance);
                        mortar.setTargetYaw(msg.yaw);

                        mortar.executeServerFire();

                        if (!isCreative && grenadeSlot != -1) {
                            player.inventory.decrStackSize(grenadeSlot, 1);
                        }
                    }
                } else {
                    player.sendStatusMessage(new StringTextComponent("Out of ammo! Requires a Grenade."), true);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}