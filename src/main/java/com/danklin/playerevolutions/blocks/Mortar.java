package com.danklin.playerevolutions.blocks;

import com.danklin.playerevolutions.client.MortarAimGUI;
import com.danklin.playerevolutions.tileentities.MortarTileEntity;
import com.danklin.playerevolutions.util.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class Mortar extends Block {
    public Mortar() {
        super(Block.Properties.create(Material.IRON)
                .hardnessAndResistance(4.0f, 8.0f)
                .sound(SoundType.METAL)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .notSolid());
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MortarTileEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (handIn == Hand.MAIN_HAND) {
            if (worldIn.isRemote) {
                TileEntity te = worldIn.getTileEntity(pos);
                if (te instanceof MortarTileEntity) {
                    Minecraft.getInstance().displayGuiScreen(new MortarAimGUI((MortarTileEntity) te));
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}