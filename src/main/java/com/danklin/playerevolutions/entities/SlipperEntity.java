package com.danklin.playerevolutions.entities;

import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IRendersAsItem.class
)
public class SlipperEntity extends ProjectileItemEntity implements IRendersAsItem {

    public SlipperEntity(EntityType<? extends SlipperEntity> type, World world) {
        super(type, world);
    }

    public SlipperEntity(World world, LivingEntity thrower) {
        super(RegistryHandler.SLIPPER_ENTITY.get(), thrower, world);
    }

    @Override
    protected Item getDefaultItem() {
        return RegistryHandler.SLIPPER.get();
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getItem() {
        return new ItemStack(this.getDefaultItem());
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.getType() == RayTraceResult.Type.ENTITY) {
                Entity target = ((EntityRayTraceResult) result).getEntity();

                LivingEntity thrower = this.getThrower();
                target.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), 3.0F);
            }

            this.remove();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}