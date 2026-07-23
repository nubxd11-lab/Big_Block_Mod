package com.danklin.playerevolutions.tileentities;

import com.danklin.playerevolutions.entities.GrenadeEntity;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class MortarTileEntity extends TileEntity {

    private double targetDistance = 20.0D;
    private double targetYaw = 0.0D;

    public MortarTileEntity() {
        super(RegistryHandler.MORTAR_TILE_ENTITY.get());
    }

    public double getTargetDistance() {
        return this.targetDistance;
    }

    public void setTargetDistance(double distance) {
        this.targetDistance = distance;
        this.markDirty();
    }

    public double getTargetYaw() {
        return this.targetYaw;
    }

    public void setTargetYaw(double yaw) {
        this.targetYaw = yaw;
        this.markDirty();
    }

    public void executeServerFire() {
        if (this.world == null || this.world.isRemote) return;

        double spawnX = this.pos.getX() + 0.5D;
        double spawnY = this.pos.getY() + 1.8D;
        double spawnZ = this.pos.getZ() + 0.5D;

        GrenadeEntity grenade = new GrenadeEntity(this.world, spawnX, spawnY, spawnZ);
        grenade.setLaunchedFromMortar(true);

        this.world.addEntity(grenade);

        double radYaw = Math.toRadians(this.targetYaw);
        double targetX = spawnX + (this.targetDistance * Math.sin(-radYaw));
        double targetZ = spawnZ + (this.targetDistance * Math.cos(-radYaw));
        double peakHeight = Math.max(10.0D, this.targetDistance * 0.25D);

        grenade.launchToward(targetX, targetZ, peakHeight);
        grenade.velocityChanged = true;

        this.world.playSound(null, this.pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0F, 0.5F);

        this.markDirty();
    }
}