package com.danklin.playerevolutions.blocks;

import com.danklin.playerevolutions.client.MortarAimGUI;
import com.danklin.playerevolutions.tileentities.MortarTileEntity;
import net.minecraft.block.Block;
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
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class Mortar extends Block {

    public Mortar(){
        super(Block.Properties.create(Material.IRON)
                .hardnessAndResistance(5.0f, 8.0f)
                .sound(SoundType.METAL)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, net.minecraft.world.IBlockReader world) {
        return new MortarTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (handIn != Hand.MAIN_HAND) {
            return ActionResultType.PASS;
        }

        if (worldIn.isRemote) { // Runs strictly on client
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof MortarTileEntity) {
                net.minecraft.client.Minecraft.getInstance().displayGuiScreen(
                        new com.danklin.playerevolutions.client.MortarAimGUI((MortarTileEntity) tileEntity)
                );
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.SUCCESS;
    }
    private static void openClientGui(MortarTileEntity mortar) {
        Minecraft.getInstance().displayGuiScreen(new MortarAimGUI(mortar));
    }
}