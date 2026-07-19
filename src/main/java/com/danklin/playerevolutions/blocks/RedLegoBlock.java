package com.danklin.playerevolutions.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class RedLegoBlock extends Block {
    public RedLegoBlock(){
        super(Block.Properties.create(Material.IRON)
                .hardnessAndResistance(2.0f, 2.0f)
                .sound(SoundType.STONE)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2));
    }
}