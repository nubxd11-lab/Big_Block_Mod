package com.danklin.playerevolutions.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class NightVisionSpyglass extends Item {

    public NightVisionSpyglass(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (!worldIn.isRemote) {
            CompoundNBT nbt = stack.getOrCreateTag();
            boolean currentState = nbt.getBoolean("active");
            boolean newState = !currentState;
            nbt.putBoolean("active", newState);

            if (newState) {
                StringTextComponent message = new StringTextComponent("Night Vision: ");
                StringTextComponent status = new StringTextComponent("ON");
                status.applyTextStyle(TextFormatting.GREEN);
                message.appendSibling(status);
                playerIn.sendMessage(message);
            } else {
                StringTextComponent message = new StringTextComponent("Night Vision: ");
                StringTextComponent status = new StringTextComponent("OFF");
                status.applyTextStyle(TextFormatting.RED);
                message.appendSibling(status);
                playerIn.sendMessage(message);

                playerIn.removePotionEffect(Effects.NIGHT_VISION);
            }
        }

        return ActionResult.resultSuccess(stack);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.hasTag() && stack.getOrCreateTag().getBoolean("active");
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote && entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            boolean isHolding = player.getHeldItemMainhand() == stack || player.getHeldItemOffhand() == stack;
            boolean isActive = stack.hasTag() && stack.getOrCreateTag().getBoolean("active");

            if (isHolding && isActive) {
                player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 20, 0, false, false));
            }
        }
    }
}