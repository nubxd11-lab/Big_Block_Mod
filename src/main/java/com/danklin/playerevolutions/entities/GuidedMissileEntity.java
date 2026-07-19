package com.danklin.playerevolutions.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class GuidedMissileEntity extends SnowballEntity {
    private Entity target;

    public GuidedMissileEntity(World worldIn, LivingEntity throwerIn, Entity lockedTarget) {
        super(worldIn, throwerIn);
        this.setItem(new ItemStack(Items.END_ROD));
        this.target = lockedTarget;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isRemote) {
            if (target != null && target.isAlive()) {
                Vec3d targetPos = new Vec3d(target.getPosX(), target.getPosY() + target.getHeight() / 2.0D, target.getPosZ());
                Vec3d currentPos = this.getPositionVec();

                if (currentPos.squareDistanceTo(targetPos) < 1.25D) {
                    this.explodeAndDestroy();
                    return;
                }

                Vec3d direction = targetPos.subtract(currentPos).normalize();
                this.setMotion(direction.scale(2.5D));
            }

            ((ServerWorld) this.world).spawnParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getPosX(), this.getPosY(), this.getPosZ(),
                    3, 0.1D, 0.1D, 0.1D, 0.0D);
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            this.explodeAndDestroy();
        }
    }

    private void explodeAndDestroy() {
        List<Entity> caughtEntities = this.world.getEntitiesWithinAABB(Entity.class, this.getBoundingBox().grow(4.0D));

        for (Entity ent : caughtEntities) {
            boolean isProjectile = ent instanceof net.minecraft.entity.IProjectile
                    || ent instanceof net.minecraft.entity.projectile.DamagingProjectileEntity;
            boolean isNotMob = !(ent instanceof net.minecraft.entity.LivingEntity);
            if (isProjectile &&isNotMob && ent != this) {
                ent.remove();
            }
        }

        this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 3.0F, Explosion.Mode.NONE);

        this.remove();
    }
}