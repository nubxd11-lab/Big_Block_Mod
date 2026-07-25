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

    public AerosolCanEntity(EntityType<? extends AerosolCanEntity> type, World world) {
        super(type, world);
    }

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
        if (!this.world.isRemote) {

            this.world.createExplosion(
                    this,
                    this.getPosX(),
                    this.getPosY(),
                    this.getPosZ(),
                    2.5F,
                    false,
                    Explosion.Mode.BREAK
            );

            this.remove();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}