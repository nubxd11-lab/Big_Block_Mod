package com.danklin.playerevolutions.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class SapphireBlock extends Block {
    public SapphireBlock() {
        super(Block.Properties.create(Material.IRON)
                .hardnessAndResistance(5.0f, 6.0f) // Matches the toughness of an iron block
                .sound(SoundType.METAL)            // Plays metal footsteps, placement, and breaking noises
                .harvestTool(ToolType.PICKAXE)     // Restricts harvesting exclusively to pickaxes
                .harvestLevel(2));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public String getTranslationKey() {
        return "Sapphire Block";
    }
}
