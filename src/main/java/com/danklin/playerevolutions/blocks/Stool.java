package com.danklin.playerevolutions.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.List;

public class Stool extends Block {

    public Stool() {
        super(Block.Properties.create(Material.WOOD)
                .hardnessAndResistance(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .harvestTool(ToolType.AXE)
                .notSolid());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (hand == Hand.MAIN_HAND && !player.isSneaking()) {
            if (!world.isRemote) {

                double seatX = pos.getX() + 0.5D;
                double seatY = pos.getY() + 0.0D;
                double seatZ = pos.getZ() + 0.5D;

                AxisAlignedBB searchBox = new AxisAlignedBB(pos).grow(0.5D);
                List<ArmorStandEntity> existingSeats = world.getEntitiesWithinAABB(ArmorStandEntity.class, searchBox,
                        e -> e.getCustomName() != null && e.getCustomName().getString().equals("chair_seat"));

                if (!existingSeats.isEmpty()) {
                    if (existingSeats.get(0).isBeingRidden()) {
                        return ActionResultType.SUCCESS;
                    }
                }

                ArmorStandEntity seat = new ArmorStandEntity(world, seatX, seatY, seatZ);
                seat.setInvisible(true);
                seat.setNoGravity(true);
                seat.setInvulnerable(true);

                seat.setCustomName(new StringTextComponent("stool_seat"));
                seat.rotationYaw = player.rotationYaw;

                world.addEntity(seat);

                player.startRiding(seat, true);
            }
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }
}