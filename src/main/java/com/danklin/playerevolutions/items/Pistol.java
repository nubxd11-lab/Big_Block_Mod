package com.danklin.playerevolutions.items;

import com.danklin.playerevolutions.entities.BulletEntity;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class Pistol extends Item {

    public Pistol(Item.Properties properties) {
        super(properties);
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

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            shoot(player.getEntityWorld(), player, stack);

            if (player.isHandActive()) {
                player.stopActiveHand();
            }
        }
        return true;
    }

    public void shoot(World worldIn, PlayerEntity playerIn, ItemStack pistolStack) {
        if (playerIn.getCooldownTracker().hasCooldown(this)) return;

        boolean isCreative = playerIn.abilities.isCreativeMode;
        ItemStack ammoStack = findBullet(playerIn);
        boolean hasAmmo = !ammoStack.isEmpty();

        if (isCreative || hasAmmo) {
            if (!worldIn.isRemote) {
                BulletEntity bullet = new BulletEntity(worldIn, playerIn);

                bullet.setPosition(
                        playerIn.getPosX(),
                        playerIn.getPosYEye() - 0.1D,
                        playerIn.getPosZ()
                );

                float spread = playerIn.isHandActive() ? 0.02F : 0.12F;

                bullet.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 3.5F, spread);
                bullet.pickupStatus = BulletEntity.PickupStatus.DISALLOWED;

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

    private ItemStack findBullet(PlayerEntity player) {
        if (player.getHeldItemOffhand().getItem() == RegistryHandler.BULLET.get()) {
            return player.getHeldItemOffhand();
        }

        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.getItem() == RegistryHandler.BULLET.get()) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }
}