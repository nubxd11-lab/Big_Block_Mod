package com.danklin.playerevolutions.entities;

import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;


public class GrenadeEntity extends ThrowableEntity implements IRendersAsItem {

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private int fuseTicks = 80;

    public GrenadeEntity(EntityType<? extends GrenadeEntity> type, World world) {
        super(type, world);
    }

    public GrenadeEntity(World world, LivingEntity thrower) {
        super(RegistryHandler.GRENADE_ENTITY.get(), thrower, world);
    }

    public GrenadeEntity(World world, double x, double y, double z) {
        super(RegistryHandler.GRENADE_ENTITY.get(), x, y, z, world);
    }

    @Override
    protected void registerData() {
    }

    @Override
    public void tick() {
        super.tick();

        if (this.world.isRemote()) {
            this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 0.2D, this.getPosZ(), 0.0D, 0.0D, 0.0D);
        } else {
            this.fuseTicks--;

            if (this.fuseTicks <= 0) {
                this.detonate();
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote()) {
            this.setMotion(this.getMotion().mul(-0.4D, -0.4D, -0.4D)); // Reverse & dampen velocity
            this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(),
                    SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.8F, 0.6F);
        }
    }

    private void detonate() {
        if (!this.world.isRemote()) {
            double x = this.getPosX();
            double y = this.getPosY();
            double z = this.getPosZ();

            this.world.createExplosion(this, x, y, z, 4.0F, true, net.minecraft.world.Explosion.Mode.BREAK);

            if (this.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) this.world;
                serverWorld.spawnParticle(ParticleTypes.EXPLOSION, x, y, z, 10, 1.0D, 1.0D, 1.0D, 0.1D);
            }

            this.remove();
        }
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(RegistryHandler.GRENADE.get());
    }
}