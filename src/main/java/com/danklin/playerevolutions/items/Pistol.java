package com.danklin.playerevolutions.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class Pistol extends Item {

    public static final Predicate<ItemStack> BULLET = (stack) -> stack.getItem() instanceof Bullet;

    public Pistol() {
        super(new Item.Properties()
                .group(ItemGroup.COMBAT)
                .defaultMaxDamage(500)
        );
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack pistolStack = playerIn.getHeldItem(handIn);

        ItemStack ammoStack = playerIn.findAmmo(pistolStack);

        boolean isCreative = playerIn.abilities.isCreativeMode;
        boolean hasAmmo = !ammoStack.isEmpty() && ammoStack.getItem() instanceof Bullet;

        if (isCreative || hasAmmo) {
            if (!worldIn.isRemote) {
                SnowballEntity bulletEntity = new SnowballEntity(worldIn, playerIn);
                bulletEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 3.5F, 1.0F);
                worldIn.addEntity(bulletEntity);

                pistolStack.damageItem(1, playerIn, (p) -> p.sendBreakAnimation(handIn));


                if (!isCreative) {
                    ammoStack.shrink(1);
                    if (ammoStack.isEmpty()) {
                        playerIn.inventory.deleteStack(ammoStack);
                    }
                }
            }


            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.5F, 1.8F);

            playerIn.getCooldownTracker().setCooldown(this, 10);
            playerIn.addStat(Stats.ITEM_USED.get(this));

            return ActionResult.resultSuccess(pistolStack);
        }

        worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
                SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 0.8F, 1.2F);

        return ActionResult.resultFail(pistolStack);
    }
}