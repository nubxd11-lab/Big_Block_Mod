package com.danklin.playerevolutions.entities;

import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
public class AerosolCanEntity extends ProjectileItemEntity implements IRendersAsItem {

    // Primary constructor required for entity registration
    public AerosolCanEntity(EntityType<? extends AerosolCanEntity> type, World world) {
        super(type, world);
    }

    // Constructor used when a player throws the can
    public AerosolCanEntity(World world, LivingEntity thrower) {
        super(RegistryHandler.AEROSOL_CAN_ENTITY.get(), thrower, world);
    }

    @Override
    protected Item getDefaultItem() {
        return RegistryHandler.AEROSOL_CAN.get();
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getItem() {
        return new ItemStack(this.getDefaultItem());
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) { // Execute server-side only

            // createExplosion parameters in 1.15.2:
            // (Entity exploder, double x, double y, double z, float size, boolean causesFire, Explosion.Mode mode)
            this.world.createExplosion(
                    this,
                    this.getPosX(),
                    this.getPosY(),
                    this.getPosZ(),
                    2.5F,                 // Explosion radius/power (TNT is 4.0F)
                    false,                // false = NO FIRE
                    Explosion.Mode.BREAK  // BREAK = breaks blocks, NONE = damage/knockback only
            );

            // Remove entity from the world after detonation
            this.remove();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}