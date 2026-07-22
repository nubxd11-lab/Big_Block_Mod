package com.danklin.playerevolutions.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import java.util.List;

public class AirBlast extends Item {

    public AirBlast(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        System.out.println("--> AirBlast right-click triggered! World remote: " + worldIn.isRemote());
        playerIn.swingArm(handIn);

        if (!worldIn.isRemote()) {
            double range = 10.0D;
            Vec3d lookVec = playerIn.getLookVec();
            Vec3d playerEyePos = playerIn.getEyePosition(1.0F);

            if (worldIn instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) worldIn;
                for (int i = 1; i <= 6; i++) {
                    double dist = i * 1.0D;
                    serverWorld.spawnParticle(
                            ParticleTypes.CLOUD,
                            playerEyePos.x + lookVec.x * dist,
                            playerEyePos.y + lookVec.y * dist,
                            playerEyePos.z + lookVec.z * dist,
                            4, 0.3D, 0.3D, 0.3D, 0.05D
                    );
                }
            }

            AxisAlignedBB searchArea = playerIn.getBoundingBox().grow(range);
            List<Entity> targets = worldIn.getEntitiesWithinAABB(Entity.class, searchArea);

            for (Entity target : targets) {
                if (target == playerIn) continue;

                Vec3d targetCenter = target.getBoundingBox().getCenter();
                Vec3d dirToTarget = targetCenter.subtract(playerEyePos);

                if (dirToTarget.lengthSquared() <= range * range) {
                    Vec3d targetVecNormalized = dirToTarget.normalize();

                    if (lookVec.dotProduct(targetVecNormalized) > -0.5D) {

                        if (target instanceof LivingEntity) {
                            target.attackEntityFrom(DamageSource.causePlayerDamage(playerIn), 2.0F);
                        }

                        double knockbackPower = 20.0D;
                        target.setMotion(
                                lookVec.x * knockbackPower,
                                1.0D + (lookVec.y * 0.5D),
                                lookVec.z * knockbackPower
                        );
                        target.velocityChanged = true;
                    }
                }
            }

            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
                    SoundEvents.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 1.0F, 1.5F);
        }
        return ActionResult.resultSuccess(itemstack);
    }
}