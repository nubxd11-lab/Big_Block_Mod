package com.danklin.playerevolutions.items;

import com.danklin.playerevolutions.entities.BulletEntity;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class Pistol extends Item {

    public Pistol() {
        super(new Item.Properties()
                .group(ItemGroup.COMBAT)
                .defaultMaxDamage(500)
        );
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack pistolStack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(pistolStack);
    }

    public void shoot(World worldIn, PlayerEntity playerIn, ItemStack pistolStack) {
        if (playerIn.getCooldownTracker().hasCooldown(this)) return;

        ItemStack ammoStack = playerIn.findAmmo(pistolStack);
        boolean isCreative = playerIn.abilities.isCreativeMode;
        boolean hasAmmo = !ammoStack.isEmpty() && ammoStack.getItem() == RegistryHandler.BULLET.get();

        if (isCreative || hasAmmo) {
            if (!worldIn.isRemote) {
                BulletEntity bullet = new BulletEntity(worldIn, playerIn);

                bullet.setPosition(
                        playerIn.getPosX(),
                        playerIn.getPosYEye() - 0.1D,
                        playerIn.getPosZ()
                );

                // Velocity pitch, yaw, roll, speed (3.5F), inaccuracy (0.1F)
                bullet.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 3.5F, 0.1F);
                bullet.pickupStatus = BulletEntity.PickupStatus.DISALLOWED; // Prevent picking up bullet item from ground

                worldIn.addEntity(bullet);

                pistolStack.damageItem(1, playerIn, (p) -> p.sendBreakAnimation(Hand.MAIN_HAND));

                if (!isCreative) {
                    ammoStack.shrink(1);
                    if (ammoStack.isEmpty()) {
                        playerIn.inventory.deleteStack(ammoStack);
                    }
                }
            }

            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.5F, 1.8F);

            playerIn.getCooldownTracker().setCooldown(this, 5);
            playerIn.addStat(Stats.ITEM_USED.get(this));
        } else {
            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
                    SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 0.8F, 1.2F);
        }
    }
}