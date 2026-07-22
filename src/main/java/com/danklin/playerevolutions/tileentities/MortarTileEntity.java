package com.danklin.playerevolutions.tileentities;

import com.danklin.playerevolutions.entities.GrenadeEntity;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class MortarTileEntity extends TileEntity {

    private double targetDistance = 20.0D;
    private double targetYaw = 0.0D;
    private int currentDurability = 10;
    private int maxDurability = 10;

    public MortarTileEntity() {
        super(RegistryHandler.MORTAR_TILE_ENTITY.get());
    }

    // --- Public Getters and Setters for MortarAimGUI ---

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

    public int getCurrentDurability() {
        return this.currentDurability;
    }

    public int getMaxDurability() {
        return this.maxDurability;
    }

    // --- Firing Logic ---

    public void fire() {
        if (this.world != null && !this.world.isRemote) {
            if (this.currentDurability <= 0) return;

            BlockPos spawnPos = this.pos.up();

            GrenadeEntity grenade = new GrenadeEntity(
                    this.world,
                    spawnPos.getX() + 0.5D,
                    spawnPos.getY() + 0.5D,
                    spawnPos.getZ() + 0.5D
            );

            double radYaw = Math.toRadians(this.targetYaw);
            double targetX = spawnPos.getX() + 0.5D + (this.targetDistance * Math.sin(-radYaw));
            double targetZ = spawnPos.getZ() + 0.5D + (this.targetDistance * Math.cos(-radYaw));
            double peakHeight = Math.max(10.0D, this.targetDistance * 0.25D);

            grenade.launchToward(targetX, targetZ, peakHeight);

            this.world.addEntity(grenade);
            this.world.playSound(null, this.pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0F, 0.5F);

            this.currentDurability--;
            this.markDirty();
        }
    }
}