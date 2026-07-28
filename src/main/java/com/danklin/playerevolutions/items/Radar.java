package com.danklin.playerevolutions.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class Radar extends Item {

    private static final double RADAR_RADIUS = 16.0D;

    public Radar(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote && entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;

            if (isFirstRadarInInventory(player, stack) && player.ticksExisted % 20 == 0) {
                AxisAlignedBB scanArea = player.getBoundingBox().grow(RADAR_RADIUS);

                List<MobEntity> nearbyHostiles = worldIn.getEntitiesWithinAABB(
                        MobEntity.class,
                        scanArea,
                        entity -> entity instanceof IMob && entity.isAlive() && !entity.isSpectator()
                );

                if (!nearbyHostiles.isEmpty()) {
                    worldIn.playSound(
                            null,
                            player.getPosX(),
                            player.getPosY(),
                            player.getPosZ(),
                            SoundEvents.BLOCK_NOTE_BLOCK_PLING,
                            SoundCategory.PLAYERS,
                            0.6F,
                            1.8F
                    );

                    player.sendStatusMessage(
                            new TranslationTextComponent("radar.warning", nearbyHostiles.size())
                                    .applyTextStyle(TextFormatting.RED)
                                    .applyTextStyle(TextFormatting.BOLD),
                            true
                    );
                }
            }
        }
    }


    private boolean isFirstRadarInInventory(PlayerEntity player, ItemStack currentStack) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stackInSlot = player.inventory.getStackInSlot(i);
            if (stackInSlot.getItem() == this) {
                return stackInSlot == currentStack;
            }
        }
        return true;
    }
}