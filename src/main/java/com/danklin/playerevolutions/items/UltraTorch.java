package com.danklin.playerevolutions.items;

import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UltraTorch extends Item {

    private BlockPos previousLightPos = null;

    public UltraTorch(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            boolean isHolding = player.getHeldItemMainhand() == stack || player.getHeldItemOffhand() == stack;

            if (!worldIn.isRemote) {
                if (isHolding) {
                    BlockPos currentPos = player.getPosition().up();

                    if (previousLightPos == null || !previousLightPos.equals(currentPos)) {
                        removeLightBlock(worldIn);

                        if (worldIn.getBlockState(currentPos).isAir()) {
                            worldIn.setBlockState(currentPos, RegistryHandler.INVISIBLE_LIGHT_BLOCK.get().getDefaultState(), 3);
                            previousLightPos = currentPos;
                        }
                    }
                } else {
                    removeLightBlock(worldIn);
                }
            }
        }
    }

    private void removeLightBlock(World worldIn) {
        if (previousLightPos != null) {
            if (worldIn.getBlockState(previousLightPos).getBlock() == RegistryHandler.INVISIBLE_LIGHT_BLOCK.get()) {
                worldIn.removeBlock(previousLightPos, false);
            }
            previousLightPos = null;
        }
    }
}