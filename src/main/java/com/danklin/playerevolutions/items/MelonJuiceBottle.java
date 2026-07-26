package com.danklin.playerevolutions.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class MelonJuiceBottle extends Item {

    public MelonJuiceBottle(Properties properties) {
        super(properties);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;

            if (this.isFood()) {
                entityLiving.onFoodEaten(worldIn, stack.copy());
            }

            if (!player.abilities.isCreativeMode) {
                stack.shrink(1);

                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);

                if (stack.isEmpty()) {
                    return bottle;
                }

                if (!player.inventory.addItemStackToInventory(bottle)) {
                    player.dropItem(bottle, true);
                }
            }
        }

        return stack;
    }
}