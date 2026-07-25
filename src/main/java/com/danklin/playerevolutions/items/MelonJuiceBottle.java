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

    private final Item containerItem;

    public MelonJuiceBottle(Properties properties) {
        super(properties);
        this.containerItem = Items.GLASS_BOTTLE;
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
        super.onItemUseFinish(stack, worldIn, entityLiving);

        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;

            if (player.abilities.isCreativeMode) {
                return stack;
            }

            ItemStack bottleStack = new ItemStack(this.containerItem);

            if (stack.isEmpty()) {
                return bottleStack;
            } else {
                if (!player.inventory.addItemStackToInventory(bottleStack)) {
                    player.dropItem(bottleStack, false);
                }
            }
        }

        return stack;
    }
}