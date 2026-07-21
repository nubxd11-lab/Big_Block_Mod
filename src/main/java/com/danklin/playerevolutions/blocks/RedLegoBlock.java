package com.danklin.playerevolutions.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class RedLegoBlock extends Block {
    public RedLegoBlock(){
        super(Block.Properties.create(Material.IRON)
                .hardnessAndResistance(2.0f, 2.0f)
                .sound(SoundType.STONE)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2));
    }
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entity) {
        if (!worldIn.isRemote() && entity instanceof LivingEntity) {
            Vec3d motion = entity.getMotion();
            double horizontalSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
            if (horizontalSpeed > 0.01D) {
                entity.attackEntityFrom(DamageSource.GENERIC, 1.0F); // 0.5 hearts
            }
        }
        super.onEntityWalk(worldIn, pos, entity);
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (!worldIn.isRemote() && entityIn instanceof LivingEntity) {
            if (fallDistance > 0.2F) {
                float landingDamage = 2.0F + (fallDistance * 1.5F);
                entityIn.attackEntityFrom(DamageSource.GENERIC, landingDamage);
            }
        }
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }
}