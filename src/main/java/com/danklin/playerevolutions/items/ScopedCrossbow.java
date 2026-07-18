package com.danklin.playerevolutions.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import java.awt.*;

public class ScopedCrossbow extends CrossbowItem {

    float power;
    public ScopedCrossbow(Properties properties) {
        super(properties);
    }
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, net.minecraft.entity.LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        float f = getChargeProgression(i, stack);

        if (f >= 0.4F && !isCharged(stack)) {
            setCharged(stack, true);
            power = f;
            worldIn.playSound(null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(),
                    SoundEvents.ITEM_CROSSBOW_LOADING_END, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);

            return;
        }

        super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
    }

    private static float getChargeProgression(int useTime, ItemStack stack) {
        float f = (float)useTime / (float)getChargeTime(stack);
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        System.out.println("Right-clicked the custom crossbow! Is it charged? " + isCharged(playerIn.getHeldItem(handIn)));
        if (isCharged(itemstack)) {
            if (!worldIn.isRemote) {

                SnowballEntity projectile = new SnowballEntity(worldIn, playerIn);

                projectile.setItem(new ItemStack(Items.TNT));

                projectile.addTag("explosive_bolt");

                projectile.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, power * 4, 1.0F);

                worldIn.addEntity(projectile);
            }

            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            setCharged(itemstack, false);
            itemstack.damageItem(1, playerIn, (entity) -> entity.sendBreakAnimation(handIn));

            return ActionResult.resultConsume(itemstack);
        } else {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
    }
    @Override
    public String getTranslationKey() {
        return "Scoped Crossbow";
    }
}