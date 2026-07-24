package com.danklin.playerevolutions.entities;

import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BulletEntity extends AbstractArrowEntity {

    public BulletEntity(EntityType<? extends BulletEntity> type, World world) {
        super(type, world);
    }

    public BulletEntity(World world, LivingEntity shooter) {
        super(RegistryHandler.BULLET_ENTITY.get(), shooter, world);
        this.setDamage(16.0D);
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.AIR);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround) {
            this.remove();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
