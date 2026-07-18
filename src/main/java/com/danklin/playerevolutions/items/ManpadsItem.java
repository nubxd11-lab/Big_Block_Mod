package com.danklin.playerevolutions.items;

import com.danklin.playerevolutions.entities.GuidedMissileEntity;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.Optional;

public class ManpadsItem extends Item {

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
    // Runs 20 times a second while the item is in the player's inventory
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        // Only run logic on the server, and only if the player is actively holding the ite
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
        // 2. See what the laser hits
        for (Entity ent : worldIn.getEntitiesWithinAABB(Entity.class, aabb, (e) -> e != player && e.isAlive())) {

            // Allow locking onto flying mobs OR projectiles
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
                    || ent instanceof net.minecraft.entity.projectile.DamagingProjectileEntity;
            if(ent instanceof net.minecraft.entity.IProjectile)
            {
                skip = true;
            }
            if (isTargetable) {
                // Expand the hitbox slightly to make aiming more forgiving
                AxisAlignedBB entBox = ent.getBoundingBox().grow(1.5D);
                Optional<Vec3d> hit = entBox.rayTrace(eyePos, endPos);

                if (hit.isPresent()) {
                    double dist = eyePos.squareDistanceTo(hit.get());
                    if (dist < closestDist) {
                        closestDist = dist;
                        currentTarget = ent;
                    }
                }
            }
        }

        // 3. Process the Lock-On Logic
        int savedTargetId = nbt.getInt("TargetID");
        int lockTicks = nbt.getInt("LockTicks");

        if (currentTarget != null) {
            if (currentTarget.getEntityId() == savedTargetId) {
                // The player is tracking the SAME target. Increment the timer.
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT nbt = stack.getOrCreateTag();
        boolean isCreative = playerIn.abilities.isCreativeMode;
        if (!worldIn.isRemote) {
            if (nbt.getInt("LockTicks") >= 10) {

                int targetId = nbt.getInt("TargetID");
                Entity target = worldIn.getEntityByID(targetId);
                if (!isCreative && !hasAmmo(playerIn)) {
                    playerIn.sendStatusMessage(new StringTextComponent("OUT OF AMMO!").applyTextStyle(TextFormatting.DARK_RED), true);
                    worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    return ActionResult.resultFail(stack);
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
                    return ActionResult.resultSuccess(stack);
                }
            } else {
                playerIn.getCooldownTracker().setCooldown(this, 10);
                playerIn.sendStatusMessage(new StringTextComponent("NO TARGET LOCKED!").applyTextStyle(TextFormatting.DARK_RED), true);
                worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }

        return ActionResult.resultFail(stack);
    }
}