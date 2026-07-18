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

public class GuidedMissileEntity extends SnowballEntity {
    private Entity target;

    // We updated the constructor to demand a specific target when spawned!
    public GuidedMissileEntity(World worldIn, LivingEntity throwerIn, Entity lockedTarget) {
        super(worldIn, throwerIn);
        this.setItem(new ItemStack(Items.END_ROD));
        this.target = lockedTarget;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isRemote) {
            // If the target is alive, relentlessly pursue it
            if (target != null && target.isAlive()) {
                Vec3d targetPos = new Vec3d(target.getPosX(), target.getPosY() + target.getHeight() / 2.0D, target.getPosZ());
                Vec3d currentPos = this.getPositionVec();

                Vec3d direction = targetPos.subtract(currentPos).normalize();
                this.setMotion(direction.scale(2.5D)); // 2.5 is rocket speed
            }
            // If the target dies before impact, the missile will just naturally fly straight until it hits a block.

            ((ServerWorld) this.world).spawnParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    this.getPosX(), this.getPosY(), this.getPosZ(),
                    3, 0.1D, 0.1D, 0.1D, 0.0D);
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 3.0F, Explosion.Mode.NONE);
            this.remove();
        }
    }
}