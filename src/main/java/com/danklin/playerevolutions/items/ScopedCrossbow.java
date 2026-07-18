package com.danklin.playerevolutions.items;

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

public class ScopedCrossbow extends CrossbowItem {

    public ScopedCrossbow(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (isCharged(itemstack)) {
            if (!worldIn.isRemote) {
                // 1. Read the custom power from NBT (default to 1.5 if it hasn't been scrolled yet)
                float power = itemstack.hasTag() && itemstack.getTag().contains("LaunchPower")
                        ? itemstack.getTag().getFloat("LaunchPower")
                        : 1.5f;

                // 2. Use a Snowball for natural gravity/arcing
                SnowballEntity projectile = new SnowballEntity(worldIn, playerIn);

                // 3. Make it visually look like a flying block of TNT
                projectile.setItem(new ItemStack(Items.TNT));

                // 4. Tag it so our event handler knows this isn't a normal snowball
                projectile.addTag("explosive_bolt");

                // 5. Fire it using our custom NBT power setting
                projectile.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, power, 1.0F);

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
}