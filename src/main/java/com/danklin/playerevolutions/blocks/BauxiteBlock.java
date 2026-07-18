package com.danklin.playerevolutions.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BauxiteBlock extends Block {
    public BauxiteBlock(){
        super(Block.Properties.create(Material.IRON)
                .hardnessAndResistance(3.0f, 3.0f)
                .sound(SoundType.METAL)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2));
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public String getTranslationKey() {
        return "Bauxite Block";
    }
}
