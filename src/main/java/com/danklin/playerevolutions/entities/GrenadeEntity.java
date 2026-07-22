package com.danklin.playerevolutions.entities;

import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IRendersAsItem.class
)
public class GrenadeEntity extends ThrowableEntity implements IRendersAsItem {

    private boolean launchedFromMortar = false;
    private int fuseTimer = 60; // 3-second fuse when thrown by hand

    // --- Constructors ---

    // Constructor 1: Forge/Minecraft EntityType registration
    public GrenadeEntity(EntityType<? extends GrenadeEntity> type, World world) {
        super(type, world);
    }

    // Constructor 2: Spawning directly at coordinates (e.g., via MortarTileEntity)
    public GrenadeEntity(World world, double x, double y, double z) {
        super(RegistryHandler.GRENADE_ENTITY.get(), x, y, z, world);
    }

    // Constructor 3: Thrown by player/mob hand
    public GrenadeEntity(World world, LivingEntity thrower) {
        super(RegistryHandler.GRENADE_ENTITY.get(), thrower, world);
    }

    // --- Mortar & Trajectory Logic ---

    public void setLaunchedFromMortar(boolean mortar) {
        this.launchedFromMortar = mortar;
    }

    /**
     * Calculates mortar launch velocity vector and sets mortar mode
     */
    public void launchToward(double targetX, double targetZ, double peakHeight) {
        this.setLaunchedFromMortar(true);

        double dx = targetX - this.getPosX();
        double dz = targetZ - this.getPosZ();

        double vy = Math.sqrt(2 * 0.05 * peakHeight);
        double totalTicks = (2 * vy) / 0.05;

        double vx = dx / totalTicks;
        double vz = dz / totalTicks;

        this.setMotion(vx, vy, vz);
    }

    // --- Entity Ticking & Impact Logic ---

    @Override
    public void tick() {
        super.tick();

        // Hand-thrown fuse countdown (Server thread only)
        if (!this.world.isRemote && !this.launchedFromMortar) {
            this.fuseTimer--;
            if (this.fuseTimer <= 0) {
                this.explode(4.0F); // Normal hand-thrown explosion power
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (this.launchedFromMortar) {
                // MORTAR MODE: Explodes instantly on impact
                this.explode(8.0F); // Large mortar explosion power
            } else {
                // HAND-THROWN MODE: Bounces off surface, waiting for fuse countdown
                this.setMotion(this.getMotion().mul(0.3D, -0.2D, 0.3D));
            }
        }
    }

    private void explode(float power) {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), power, Explosion.Mode.BREAK);
            this.remove(); // Despawn entity after explosion
        }
    }

    // --- NBT Save & Load (Prevents state loss on server save/restart) ---

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("LaunchedFromMortar", this.launchedFromMortar);
        compound.putInt("FuseTimer", this.fuseTimer);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.launchedFromMortar = compound.getBoolean("LaunchedFromMortar");
        this.fuseTimer = compound.getInt("FuseTimer");
    }

    // --- Rendering & Registration Overrides ---

    @Override
    public ItemStack getItem() {
        return new ItemStack(RegistryHandler.GRENADE.get());
    }

    @Override
    protected void registerData() {}

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}