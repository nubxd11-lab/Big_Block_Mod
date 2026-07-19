package com.danklin.playerevolutions.items;

import com.danklin.playerevolutions.entities.GuidedMissileEntity;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import java.awt.*;
import java.util.Optional;

public class ManpadsItem extends Item {

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.BOW;
    }
    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 7200;
    }

    public ManpadsItem(Properties properties) {
        super(properties);
    }
    private boolean hasAmmo(PlayerEntity player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            if (player.inventory.getStackInSlot(i).getItem() == RegistryHandler.ROCKET_AMMO.get()) {
                return true;
            }
        }
        return false;
    }

    // Removes exactly 1 rocket from the player's inventory
    private void consumeAmmo(PlayerEntity player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.getItem() == RegistryHandler.ROCKET_AMMO.get()) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }
                return;
            }
        }
    }
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.isRemote || !(entityIn instanceof PlayerEntity) || !isSelected) return;

        PlayerEntity player = (PlayerEntity) entityIn;
        CompoundNBT nbt = stack.getOrCreateTag();

        Vec3d eyePos = player.getEyePosition(1.0F);
        Vec3d lookVec = player.getLook(1.0F);
        Vec3d endPos = eyePos.add(lookVec.scale(100.0D));
        AxisAlignedBB aabb = player.getBoundingBox().expand(lookVec.scale(100.0D)).grow(2.0D);

        Entity currentTarget = null;
        double closestDist = Double.MAX_VALUE;

        boolean skip = false;
        for (Entity ent : worldIn.getEntitiesWithinAABB(Entity.class, aabb, (e) -> e != player && e.isAlive())) {

            boolean isTargetable = ent instanceof net.minecraft.entity.monster.PhantomEntity
                    || ent instanceof net.minecraft.entity.monster.GhastEntity
                    || ent instanceof net.minecraft.entity.monster.BlazeEntity
                    || ent instanceof net.minecraft.entity.monster.VexEntity
                    || ent instanceof net.minecraft.entity.passive.ParrotEntity
                    || ent instanceof net.minecraft.entity.boss.dragon.EnderDragonEntity
                    || ent instanceof net.minecraft.entity.boss.WitherEntity
                    || ent instanceof net.minecraft.entity.passive.BatEntity
                    || ent instanceof net.minecraft.entity.passive.BeeEntity
                    || ent instanceof net.minecraft.entity.IProjectile
                    || ent instanceof net.minecraft.entity.projectile.ShulkerBulletEntity
                    || ent instanceof net.minecraft.entity.projectile.DamagingProjectileEntity
                    ||(ent instanceof net.minecraft.entity.player.PlayerEntity && ((net.minecraft.entity.player.PlayerEntity) ent).isElytraFlying());
            if(ent instanceof net.minecraft.entity.IProjectile)
            {
                skip = true;
            }
            if (isTargetable) {
                AxisAlignedBB entBox = ent.getBoundingBox().grow(1.5D);
                Optional<Vec3d> hit = entBox.rayTrace(eyePos, endPos);

                if (hit.isPresent()) {

                    RayTraceContext context = new RayTraceContext(
                            eyePos,
                            hit.get(),
                            RayTraceContext.BlockMode.COLLIDER,
                            RayTraceContext.FluidMode.NONE,
                            player
                    );

                    BlockRayTraceResult blockHit = worldIn.rayTraceBlocks(context);

                    double dist = eyePos.squareDistanceTo(hit.get());
                    if (dist < closestDist && blockHit.getType() == RayTraceResult.Type.MISS) {
                        closestDist = dist;
                        currentTarget = ent;
                    }
                }
            }
        }

        int savedTargetId = nbt.getInt("TargetID");
        int lockTicks = nbt.getInt("LockTicks");
        if (currentTarget != null) {
            if (currentTarget.getEntityId() == savedTargetId) {
                if (lockTicks < 10) { // 10 ticks = 0.5 seconds
                    lockTicks++;

                    if (lockTicks % 2 == 0) {
                        worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BIT, SoundCategory.PLAYERS, 1.0F, 1.5F);
                    }

                    if (lockTicks == 10 || skip) {
                        worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0F, 2.0F);
                        player.sendStatusMessage(new StringTextComponent("TARGET LOCKED!").applyTextStyle(TextFormatting.RED), true);
                    }
                }
            } else {
                savedTargetId = currentTarget.getEntityId();
                lockTicks = 1;
                player.sendStatusMessage(new StringTextComponent("Locking...").applyTextStyle(TextFormatting.YELLOW), true);
            }
        } else {
            if (lockTicks > 0) {
                savedTargetId = -1;
                lockTicks = 0;
                player.sendStatusMessage(new StringTextComponent("Lock lost.").applyTextStyle(TextFormatting.GRAY), true);
            }
        }

        nbt.putInt("TargetID", savedTargetId);
        nbt.putInt("LockTicks", lockTicks);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (!worldIn.isRemote) {
            PlayerEntity playerIn = (PlayerEntity) entityLiving;
            CompoundNBT nbt = stack.getOrCreateTag();

            if (nbt.getInt("LockTicks") >= 10) {

                boolean isCreative = playerIn.isCreative();
                int targetId = nbt.getInt("TargetID");
                Entity target = worldIn.getEntityByID(targetId);
                if (!isCreative && !hasAmmo(playerIn)) {
                    playerIn.sendStatusMessage(new StringTextComponent("OUT OF AMMO!").applyTextStyle(TextFormatting.DARK_RED), true);
                    worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    return;
                }
                if (target != null && target.isAlive()) {

                    if (!isCreative) {
                        consumeAmmo(playerIn);
                    }
                    GuidedMissileEntity missile = new GuidedMissileEntity(worldIn, playerIn, target);
                    missile.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2.0F, 0.0F);
                    worldIn.addEntity(missile);

                    worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    playerIn.getCooldownTracker().setCooldown(this, 40);

                    // Reset the lock after firing
                    nbt.putInt("LockTicks", 0);
                    nbt.putInt("TargetID", -1);
                    return;
                }
            } else {
                playerIn.getCooldownTracker().setCooldown(this, 10);
                playerIn.sendStatusMessage(new StringTextComponent("NO TARGET LOCKED!").applyTextStyle(TextFormatting.DARK_RED), true);
                worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }

        return;




    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (handIn != Hand.MAIN_HAND) {
            return ActionResult.resultFail(stack);
        }
        CompoundNBT nbt = stack.getOrCreateTag();
        boolean isCreative = playerIn.abilities.isCreativeMode;
        if (!isCreative && !hasAmmo(playerIn)) {
            playerIn.sendStatusMessage(new StringTextComponent("OUT OF AMMO!").applyTextStyle(TextFormatting.DARK_RED), true);
            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return ActionResult.resultFail(stack);
        }

        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(stack);

    }

    @Override
    public String getTranslationKey() {
        return "Manpads";
    }

}