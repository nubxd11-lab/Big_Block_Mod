package com.danklin.playerevolutions.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class NuclearBombBlock extends Block {
    public NuclearBombBlock() {
        super(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(3.0f, 3.0f)
                .sound(SoundType.WET_GRASS)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2));
    }

    public NuclearBombBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BombTileEntity();
    }

    private void triggerIgnition(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof BombTileEntity) {
            ((BombTileEntity) te).ignite();
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(handIn);
        if (heldItem.getItem() == Items.FLINT_AND_STEEL) {
            if (!worldIn.isRemote()) {
                this.triggerIgnition(worldIn, pos);
                if (!player.isCreative()) {
                    heldItem.damageItem(1, player, (playerEntity) -> playerEntity.sendBreakAnimation(handIn));
                }
            }
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!worldIn.isRemote() && worldIn.isBlockPowered(pos)) {
            this.triggerIgnition(worldIn, pos);
        }
    }

    @Override
    public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, Entity projectile) {
        if (!worldIn.isRemote() && projectile instanceof AbstractArrowEntity) {
            AbstractArrowEntity arrow = (AbstractArrowEntity) projectile;
            if (arrow.isBurning()) {
                this.triggerIgnition(worldIn, hit.getPos());
            }
        }
    }

    @Override
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote()) {
            this.triggerIgnition(worldIn, pos);
        }
    }

    // ==========================================
    // SEPARATE TICKING LOGIC
    // ==========================================
    public static class BombTileEntity extends TileEntity implements ITickableTileEntity {
        private int fuseTicks = -1;

        public BombTileEntity() {
            // Links directly to the registry entry we just placed in RegistryHandler
            super(com.danklin.playerevolutions.util.RegistryHandler.NUCLEAR_BOMB_BLOCK_TILE.get());
        }
        public void ignite() {
            if (this.fuseTicks == -1) {
                this.fuseTicks = 80; 
                if (this.world != null) {
                    this.world.playSound(null, this.pos, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        }

        @Override
        public void tick() {
            if (this.world == null || this.world.isRemote()) return;

            if (this.fuseTicks > 0) {
                this.fuseTicks--;

                if (this.world.getRandom().nextInt(4) == 0 && this.world instanceof ServerWorld) {
                    ((ServerWorld) this.world).spawnParticle(ParticleTypes.FLAME,
                            this.pos.getX() + 0.5D, this.pos.getY() + 1.1D, this.pos.getZ() + 0.5D,
                            1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            } else if (this.fuseTicks == 0) {
                this.detonate();
            }
        }

        private void detonate() {
            double x = this.pos.getX() + 0.5D;
            double y = this.pos.getY() + 0.5D;
            double z = this.pos.getZ() + 0.5D;

            this.world.removeBlock(this.pos, false);
            this.world.createExplosion(null, x, y, z, 20.0F, true, Explosion.Mode.BREAK);

            if (this.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) this.world;
                serverWorld.spawnParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 100, 5.0D, 5.0D, 5.0D, 0.2D);
                serverWorld.spawnParticle(ParticleTypes.LARGE_SMOKE, x, y + 2.0D, z, 300, 3.0D, 8.0D, 3.0D, 0.1D);
                serverWorld.spawnParticle(ParticleTypes.LAVA, x, y, z, 150, 4.0D, 4.0D, 4.0D, 0.5D);
            }
            this.world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDER_DRAGON_DEATH, SoundCategory.BLOCKS, 10.0F, 0.5F);
        }
    }
}